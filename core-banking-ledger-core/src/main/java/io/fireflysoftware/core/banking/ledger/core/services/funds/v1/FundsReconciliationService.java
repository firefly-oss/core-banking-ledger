/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.fireflysoftware.core.banking.ledger.core.services.funds.v1;

import static io.fireflysoftware.core.banking.ledger.core.services.funds.v1.FundsAuthority.fail;

import io.fireflysoftware.core.banking.ledger.interfaces.dtos.funds.v1.FundsContracts.*;

import org.fireflyframework.security.api.domain.SecurityPrincipal;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Service
public class FundsReconciliationService {
    private final DatabaseClient database;
    private final FundsAuthority authority;
    private final FundsMoneyService money;
    private final Clock clock;

    public FundsReconciliationService(
            DatabaseClient database,
            FundsAuthority authority,
            FundsMoneyService money,
            ObjectProvider<Clock> clock) {
        this.database = database;
        this.authority = authority;
        this.money = money;
        this.clock = clock.getIfAvailable(Clock::systemUTC);
    }

    private record Existing(
            String kind,
            String fingerprint,
            UUID reservation,
            boolean closed,
            UUID receipt,
            Instant observed) {}

    @Transactional
    public Mono<Reconciliation> reconcile(SecurityPrincipal p, String tenant, Reserve input) {
        return Mono.defer(
                () -> {
                    FundsMoneyService.validateReservation(input);
                    FundsPolicy.require(
                            p,
                            tenant,
                            "funds:reconcile",
                            input.sourceAccountId(),
                            input.destinationAccountId());
                    if (input.notAfterEpochMillis() == null)
                        return Mono.error(fail(400, "PINNED_RECONCILIATION_DEADLINE_REQUIRED"));
                    String fingerprint = FundsMoneyService.reservationFingerprint(input);
                    return authority
                            .context(
                                    p,
                                    tenant,
                                    "RECONCILE",
                                    input.sourceAccountId(),
                                    input.destinationAccountId())
                            .then(money.operationGate(tenant, input.operationId()))
                            .then(existing(tenant, input.operationId()))
                            .flatMap(
                                    found ->
                                            found.isPresent()
                                                    ? observe(
                                                            tenant, input, fingerprint, found.get())
                                                    : validateAccounts(tenant, input)
                                                            .then(
                                                                    Mono.defer(
                                                                            () ->
                                                                                    close(
                                                                                            p,
                                                                                            tenant,
                                                                                            input,
                                                                                            fingerprint))));
                });
    }

    private Mono<Void> validateAccounts(String tenant, Reserve input) {
        return authority
                .readAccount(tenant, input.sourceAccountId(), false)
                .flatMap(
                        source ->
                                authority
                                        .readAccount(tenant, input.destinationAccountId(), false)
                                        .map(
                                                destination -> {
                                                    for (Account account :
                                                            java.util.List.of(
                                                                    source, destination)) {
                                                        if (account.kind() != Kind.CUSTOMER
                                                                || !account.currency()
                                                                        .equals(input.currency()))
                                                            throw fail(
                                                                    409,
                                                                    "CUSTOMER_INTERNAL_TRANSFER_REQUIRED");
                                                        if (input.amount()
                                                                        .stripTrailingZeros()
                                                                        .scale()
                                                                > account.currencyScale())
                                                            throw fail(
                                                                    400,
                                                                    "CURRENCY_PRECISION_EXCEEDED");
                                                    }
                                                    return true;
                                                }))
                .then();
    }

    private Mono<Optional<Existing>> existing(String tenant, UUID operation) {
        return database.sql(
                        "SELECT * FROM ledger_funds_operation WHERE tenant_id=:t AND"
                                + " operation_id=:op")
                .bind("t", tenant)
                .bind("op", operation)
                .map(
                        (r, m) ->
                                new Existing(
                                        r.get("kind", String.class),
                                        r.get("fingerprint", String.class),
                                        r.get("reservation_id", UUID.class),
                                        Boolean.TRUE.equals(
                                                r.get("closed_without_effect", Boolean.class)),
                                        r.get("closure_receipt_id", UUID.class),
                                        Optional.ofNullable(
                                                        r.get(
                                                                "closure_observed_at",
                                                                OffsetDateTime.class))
                                                .map(OffsetDateTime::toInstant)
                                                .orElse(null)))
                .one()
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty());
    }

    private Mono<Reconciliation> observe(
            String tenant, Reserve input, String fingerprint, Existing found) {
        if (!"RESERVE".equals(found.kind()) || !fingerprint.equals(found.fingerprint()))
            return Mono.error(fail(409, "FUNDS_OPERATION_INTENT_CONFLICT"));
        if (found.closed())
            return Mono.just(
                    new Reconciliation(
                            ReconciliationOutcome.ABSENT_CLOSED,
                            found.receipt(),
                            found.observed(),
                            input.notAfterEpochMillis(),
                            input,
                            null));
        if (found.reservation() == null) return Mono.error(fail(503, "INCOMPLETE_FUNDS_OPERATION"));
        return money.read(tenant, found.reservation(), false)
                .map(
                        row ->
                                new Reconciliation(
                                        ReconciliationOutcome.FOUND,
                                        null,
                                        clock.instant(),
                                        input.notAfterEpochMillis(),
                                        input,
                                        row));
    }

    private Mono<Reconciliation> close(
            SecurityPrincipal p, String tenant, Reserve input, String fingerprint) {
        Instant observed = clock.instant();
        if (observed.isBefore(Instant.ofEpochMilli(input.notAfterEpochMillis())))
            return Mono.error(fail(409, "RECONCILIATION_DEADLINE_NOT_DUE"));
        UUID receipt = UUID.randomUUID();
        String intent = FundsMoneyService.reservationIntentJson(input);
        return database.sql(
                        """
                        INSERT INTO ledger_funds_operation(tenant_id,operation_id,kind,fingerprint,source_account_id,destination_account_id,
                          closed_without_effect,closure_receipt_id,closure_observed_at,not_after_epoch_millis,canonical_intent,closed_posting_key)
                        VALUES(:t,:op,'RESERVE',:fp,:source,:destination,TRUE,:receipt,:observed,:deadline,CAST(:intent AS jsonb),:key)
                        ON CONFLICT DO NOTHING
                        """)
                .bind("t", tenant)
                .bind("op", input.operationId())
                .bind("fp", fingerprint)
                .bind("source", input.sourceAccountId())
                .bind("destination", input.destinationAccountId())
                .bind("receipt", receipt)
                .bind("observed", OffsetDateTime.ofInstant(observed, ZoneOffset.UTC))
                .bind("deadline", input.notAfterEpochMillis())
                .bind("intent", intent)
                .bind("key", FundsMoneyService.postingKey(tenant, input.operationId(), "RESERVE"))
                .fetch()
                .rowsUpdated()
                .flatMap(
                        inserted ->
                                inserted == 1
                                        ? audit(
                                                        p,
                                                        tenant,
                                                        input.operationId(),
                                                        receipt,
                                                        observed,
                                                        intent)
                                                .thenReturn(
                                                        new Reconciliation(
                                                                ReconciliationOutcome.ABSENT_CLOSED,
                                                                receipt,
                                                                observed,
                                                                input.notAfterEpochMillis(),
                                                                input,
                                                                null))
                                        : Mono.error(fail(409, "FUNDS_OPERATION_SCOPE_CONFLICT")));
    }

    private Mono<Void> audit(
            SecurityPrincipal p,
            String tenant,
            UUID operation,
            UUID receipt,
            Instant observed,
            String intent) {
        UUID event = UUID.randomUUID();
        String payload =
                "{\"receiptId\":\""
                        + receipt
                        + "\",\"observedAt\":\""
                        + observed
                        + "\",\"intent\":"
                        + intent
                        + "}";
        return database.sql(
                        "INSERT INTO"
                            + " ledger_funds_event(event_id,tenant_id,resource_id,actor,event_type,payload)"
                            + " VALUES(:event,:t,:op,:actor,'FUNDS_ABSENT_CLOSED',CAST(:payload AS"
                            + " jsonb))")
                .bind("event", event)
                .bind("t", tenant)
                .bind("op", operation)
                .bind("actor", p.subject())
                .bind("payload", payload)
                .fetch()
                .rowsUpdated()
                .then(
                        database.sql(
                                        "INSERT INTO"
                                            + " event_outbox(event_id,aggregate_type,aggregate_id,event_type,payload,funds_tenant_id)"
                                            + " VALUES(:event,'MANAGED_FUNDS',:op,'FUNDS_ABSENT_CLOSED',CAST(:payload"
                                            + " AS jsonb),:t)")
                                .bind("event", event)
                                .bind("t", tenant)
                                .bind("op", operation.toString())
                                .bind("payload", payload)
                                .fetch()
                                .rowsUpdated())
                .then()
                .onErrorMap(
                        org.springframework.dao.DataAccessException.class,
                        error -> fail(503, "FUNDS_RECONCILIATION_AUDIT_UNAVAILABLE"));
    }
}
