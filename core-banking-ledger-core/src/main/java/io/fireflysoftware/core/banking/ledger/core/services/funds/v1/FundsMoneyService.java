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

import io.fireflysoftware.core.banking.ledger.core.services.core.v1.LedgerPostingService;
import io.fireflysoftware.core.banking.ledger.interfaces.dtos.core.v1.*;
import io.fireflysoftware.core.banking.ledger.interfaces.dtos.funds.v1.FundsContracts.*;
import io.fireflysoftware.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;

import org.fireflyframework.security.api.domain.SecurityPrincipal;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class FundsMoneyService {
    private final DatabaseClient db;
    private final FundsAuthority authority;
    private final LedgerPostingService postings;
    private final Clock clock;

    public FundsMoneyService(
            DatabaseClient db,
            FundsAuthority authority,
            LedgerPostingService postings,
            ObjectProvider<Clock> clock) {
        this.db = db;
        this.authority = authority;
        this.postings = postings;
        this.clock = clock.getIfAvailable(Clock::systemUTC);
    }

    @Transactional
    public Mono<FundingReceipt> fund(SecurityPrincipal p, String tenant, Funding input) {
        return Mono.defer(
                () -> {
                    if (input == null) return Mono.error(fail(400, "FUNDING_INPUT_REQUIRED"));
                    FundsPolicy.require(
                            p,
                            tenant,
                            "funds:fund",
                            input.bankControlAccountId(),
                            input.customerAccountId());
                    validate(
                            input.operationId(),
                            input.bankControlAccountId(),
                            input.customerAccountId(),
                            input.amount(),
                            input.currency());
                    String fingerprint =
                            hash(
                                    "FUNDING",
                                    input.bankControlAccountId(),
                                    input.customerAccountId(),
                                    canonical(input.amount()),
                                    input.currency());
                    return authority
                            .context(
                                    p,
                                    tenant,
                                    "FUNDING",
                                    input.bankControlAccountId(),
                                    input.customerAccountId())
                            .then(operationGate(tenant, input.operationId()))
                            .then(
                                    lockAccounts(
                                            tenant,
                                            input.bankControlAccountId(),
                                            input.customerAccountId()))
                            .flatMap(
                                    accounts ->
                                            claim(
                                                            tenant,
                                                            input.operationId(),
                                                            "FUNDING",
                                                            fingerprint,
                                                            input.bankControlAccountId(),
                                                            input.customerAccountId())
                                                    .flatMap(
                                                            claim -> {
                                                                if (claim.result() != null)
                                                                    return Mono.just(
                                                                            new FundingReceipt(
                                                                                    input
                                                                                            .operationId(),
                                                                                    claim
                                                                                            .result()));
                                                                Account
                                                                        bank =
                                                                                accounts.get(
                                                                                        input
                                                                                                .bankControlAccountId()),
                                                                        customer =
                                                                                accounts.get(
                                                                                        input
                                                                                                .customerAccountId());
                                                                active(bank);
                                                                active(customer);
                                                                if (bank.kind() != Kind.BANK_CONTROL
                                                                        || customer.kind()
                                                                                != Kind.CUSTOMER)
                                                                    return Mono.error(
                                                                            fail(
                                                                                    409,
                                                                                    "EXPLICIT_BANK_CONTROL_FUNDING_REQUIRED"));
                                                                amount(input.amount(), bank);
                                                                amount(input.amount(), customer);
                                                                if (!bank.currency()
                                                                                .equals(
                                                                                        input
                                                                                                .currency())
                                                                        || !customer.currency()
                                                                                .equals(
                                                                                        input
                                                                                                .currency()))
                                                                    return Mono.error(
                                                                            fail(
                                                                                    409,
                                                                                    "FUNDS_CURRENCY_MISMATCH"));
                                                                BigDecimal
                                                                        bankNext =
                                                                                bank.booked()
                                                                                        .subtract(
                                                                                                input
                                                                                                        .amount()),
                                                                        customerNext =
                                                                                customer.booked()
                                                                                        .add(
                                                                                                input
                                                                                                        .amount());
                                                                numeric(bankNext);
                                                                numeric(customerNext);
                                                                return createPosting(
                                                                                tenant,
                                                                                input.operationId(),
                                                                                bank.accountId(),
                                                                                customer
                                                                                        .accountId(),
                                                                                input.amount(),
                                                                                input.currency(),
                                                                                false)
                                                                        .flatMap(
                                                                                posting ->
                                                                                        update(
                                                                                                        bank,
                                                                                                        bankNext,
                                                                                                        bank
                                                                                                                .held())
                                                                                                .then(
                                                                                                        update(
                                                                                                                customer,
                                                                                                                customerNext,
                                                                                                                customer
                                                                                                                        .held()))
                                                                                                .then(
                                                                                                        completeOperation(
                                                                                                                tenant,
                                                                                                                input
                                                                                                                        .operationId(),
                                                                                                                null,
                                                                                                                posting))
                                                                                                .then(
                                                                                                        event(
                                                                                                                p,
                                                                                                                tenant,
                                                                                                                input
                                                                                                                        .operationId(),
                                                                                                                "BANK_CONTROL_FUNDING",
                                                                                                                posting))
                                                                                                .thenReturn(
                                                                                                        new FundingReceipt(
                                                                                                                input
                                                                                                                        .operationId(),
                                                                                                                posting)));
                                                            }));
                });
    }

    @Transactional
    public Mono<Reservation> reserve(SecurityPrincipal p, String tenant, Reserve input) {
        return Mono.defer(
                () -> {
                    if (input == null) return Mono.error(fail(400, "RESERVATION_INPUT_REQUIRED"));
                    FundsPolicy.require(
                            p,
                            tenant,
                            "funds:reserve",
                            input.sourceAccountId(),
                            input.destinationAccountId());
                    validateReservation(input);
                    String fingerprint = reservationFingerprint(input);
                    return authority
                            .context(
                                    p,
                                    tenant,
                                    "RESERVE",
                                    input.sourceAccountId(),
                                    input.destinationAccountId())
                            .then(operationGate(tenant, input.operationId()))
                            .then(
                                    lockAccounts(
                                            tenant,
                                            input.sourceAccountId(),
                                            input.destinationAccountId()))
                            .flatMap(
                                    accounts ->
                                            claim(
                                                            tenant,
                                                            input.operationId(),
                                                            "RESERVE",
                                                            fingerprint,
                                                            input.sourceAccountId(),
                                                            input.destinationAccountId())
                                                    .flatMap(
                                                            claim -> {
                                                                if (claim.result() != null)
                                                                    return read(
                                                                            tenant,
                                                                            claim.result(),
                                                                            false);
                                                                Instant now = clock.instant();
                                                                Instant deadline =
                                                                        input.notAfterEpochMillis()
                                                                                        == null
                                                                                ? null
                                                                                : Instant
                                                                                        .ofEpochMilli(
                                                                                                input
                                                                                                        .notAfterEpochMillis());
                                                                if (deadline != null
                                                                        && !now.isBefore(deadline))
                                                                    return Mono.error(
                                                                            fail(
                                                                                    409,
                                                                                    "RESERVATION_DEADLINE_ELAPSED"));
                                                                Account
                                                                        source =
                                                                                accounts.get(
                                                                                        input
                                                                                                .sourceAccountId()),
                                                                        destination =
                                                                                accounts.get(
                                                                                        input
                                                                                                .destinationAccountId());
                                                                active(source);
                                                                active(destination);
                                                                amount(input.amount(), source);
                                                                amount(input.amount(), destination);
                                                                if (source.kind() != Kind.CUSTOMER
                                                                        || destination.kind()
                                                                                != Kind.CUSTOMER)
                                                                    return Mono.error(
                                                                            fail(
                                                                                    409,
                                                                                    "CUSTOMER_INTERNAL_TRANSFER_REQUIRED"));
                                                                if (!source.currency()
                                                                                .equals(
                                                                                        input
                                                                                                .currency())
                                                                        || !destination
                                                                                .currency()
                                                                                .equals(
                                                                                        input
                                                                                                .currency()))
                                                                    return Mono.error(
                                                                            fail(
                                                                                    409,
                                                                                    "FUNDS_CURRENCY_MISMATCH"));
                                                                if (source.available()
                                                                                .compareTo(
                                                                                        input
                                                                                                .amount())
                                                                        < 0)
                                                                    return Mono.error(
                                                                            fail(
                                                                                    409,
                                                                                    "INSUFFICIENT_AVAILABLE_FUNDS"));
                                                                BigDecimal held =
                                                                        source.held()
                                                                                .add(
                                                                                        input
                                                                                                .amount());
                                                                numeric(held);
                                                                UUID id = UUID.randomUUID();
                                                                Instant relative =
                                                                        now.plusSeconds(
                                                                                input.ttlSeconds());
                                                                Instant expiry =
                                                                        deadline != null
                                                                                        && deadline
                                                                                                .isBefore(
                                                                                                        relative)
                                                                                ? deadline
                                                                                : relative;
                                                                return createPosting(
                                                                                tenant,
                                                                                input.operationId(),
                                                                                source.accountId(),
                                                                                destination
                                                                                        .accountId(),
                                                                                input.amount(),
                                                                                input.currency(),
                                                                                true)
                                                                        .flatMap(
                                                                                posting ->
                                                                                        db.sql(
                                                                                                        """
                                                                                                        INSERT INTO ledger_funds_reservation(reservation_id,tenant_id,operation_id,source_account_id,destination_account_id,amount,currency,state,expires_at,posting_id)
                                                                                                        VALUES(:id,:t,:op,:s,:d,:amount,:currency,'HELD',:expiry,:posting)
                                                                                                        """)
                                                                                                .bind(
                                                                                                        "id",
                                                                                                        id)
                                                                                                .bind(
                                                                                                        "t",
                                                                                                        tenant)
                                                                                                .bind(
                                                                                                        "op",
                                                                                                        input
                                                                                                                .operationId())
                                                                                                .bind(
                                                                                                        "s",
                                                                                                        source
                                                                                                                .accountId())
                                                                                                .bind(
                                                                                                        "d",
                                                                                                        destination
                                                                                                                .accountId())
                                                                                                .bind(
                                                                                                        "amount",
                                                                                                        input
                                                                                                                .amount())
                                                                                                .bind(
                                                                                                        "currency",
                                                                                                        input
                                                                                                                .currency())
                                                                                                .bind(
                                                                                                        "expiry",
                                                                                                        OffsetDateTime
                                                                                                                .ofInstant(
                                                                                                                        expiry,
                                                                                                                        java
                                                                                                                                .time
                                                                                                                                .ZoneOffset
                                                                                                                                .UTC))
                                                                                                .bind(
                                                                                                        "posting",
                                                                                                        posting)
                                                                                                .fetch()
                                                                                                .rowsUpdated()
                                                                                                .then(
                                                                                                        update(
                                                                                                                source,
                                                                                                                source
                                                                                                                        .booked(),
                                                                                                                held))
                                                                                                .then(
                                                                                                        completeOperation(
                                                                                                                tenant,
                                                                                                                input
                                                                                                                        .operationId(),
                                                                                                                id,
                                                                                                                null))
                                                                                                .then(
                                                                                                        event(
                                                                                                                p,
                                                                                                                tenant,
                                                                                                                id,
                                                                                                                "FUNDS_RESERVED",
                                                                                                                posting))
                                                                                                .then(
                                                                                                        read(
                                                                                                                tenant,
                                                                                                                id,
                                                                                                                false)));
                                                            }));
                });
    }

    @Transactional(readOnly = true)
    public Mono<Reservation> reservation(SecurityPrincipal p, String tenant, UUID id) {
        return Mono.defer(
                () -> {
                    FundsPolicy.require(p, tenant, "funds:read");
                    return authority
                            .context(p, tenant, "READ", FundsPolicy.grantedAccounts(p))
                            .then(read(tenant, id, false))
                            .map(
                                    row -> {
                                        FundsPolicy.require(
                                                p,
                                                tenant,
                                                "funds:read",
                                                row.sourceAccountId(),
                                                row.destinationAccountId());
                                        return row;
                                    });
                });
    }

    @Transactional(readOnly = true)
    public Mono<Reservation> reservationByOperation(
            SecurityPrincipal p, String tenant, UUID operation) {
        return Mono.defer(
                () -> {
                    FundsPolicy.require(p, tenant, "funds:read");
                    return authority
                            .context(p, tenant, "READ", FundsPolicy.grantedAccounts(p))
                            .then(operationGate(tenant, operation))
                            .then(
                                    db.sql(
                                                    "SELECT reservation_id FROM"
                                                        + " ledger_funds_operation WHERE"
                                                        + " tenant_id=:t AND operation_id=:op AND"
                                                        + " kind='RESERVE' AND reservation_id IS"
                                                        + " NOT NULL")
                                            .bind("t", tenant)
                                            .bind("op", operation)
                                            .map((r, m) -> r.get("reservation_id", UUID.class))
                                            .one())
                            .switchIfEmpty(Mono.error(fail(404, "FUNDS_RESERVATION_NOT_FOUND")))
                            .flatMap(id -> read(tenant, id, false))
                            .map(
                                    row -> {
                                        FundsPolicy.require(
                                                p,
                                                tenant,
                                                "funds:read",
                                                row.sourceAccountId(),
                                                row.destinationAccountId());
                                        return row;
                                    });
                });
    }

    @Transactional
    public Mono<Reservation> action(SecurityPrincipal p, String tenant, UUID id, Command input) {
        return Mono.defer(
                () -> {
                    if (input == null || input.actionId() == null || input.action() == null)
                        return Mono.error(fail(400, "FUNDS_ACTION_REQUIRED"));
                    String purpose = input.action().name();
                    FundsPolicy.require(p, tenant, "funds:" + purpose.toLowerCase(Locale.ROOT));
                    return authority
                            .context(p, tenant, purpose, FundsPolicy.grantedAccounts(p))
                            .then(read(tenant, id, false))
                            .flatMap(
                                    observed -> {
                                        FundsPolicy.require(
                                                p,
                                                tenant,
                                                "funds:"
                                                        + input.action()
                                                                .name()
                                                                .toLowerCase(Locale.ROOT),
                                                observed.sourceAccountId(),
                                                observed.destinationAccountId());
                                        return authority
                                                .context(
                                                        p,
                                                        tenant,
                                                        purpose,
                                                        observed.sourceAccountId(),
                                                        observed.destinationAccountId())
                                                .then(operationGate(tenant, observed.operationId()))
                                                .then(
                                                        lockAccounts(
                                                                tenant,
                                                                observed.sourceAccountId(),
                                                                observed.destinationAccountId()))
                                                .flatMap(
                                                        accounts ->
                                                                read(tenant, id, true)
                                                                        .flatMap(
                                                                                current ->
                                                                                        actionClaim(
                                                                                                        tenant,
                                                                                                        id,
                                                                                                        input)
                                                                                                .flatMap(
                                                                                                        replay -> {
                                                                                                            if (replay
                                                                                                                    || already(
                                                                                                                            current,
                                                                                                                            input
                                                                                                                                    .action()))
                                                                                                                return Mono
                                                                                                                        .just(
                                                                                                                                current);
                                                                                                            Account
                                                                                                                    source =
                                                                                                                            accounts
                                                                                                                                    .get(
                                                                                                                                            current
                                                                                                                                                    .sourceAccountId()),
                                                                                                                    destination =
                                                                                                                            accounts
                                                                                                                                    .get(
                                                                                                                                            current
                                                                                                                                                    .destinationAccountId());
                                                                                                            return transition(
                                                                                                                    p,
                                                                                                                    tenant,
                                                                                                                    current,
                                                                                                                    input
                                                                                                                            .action(),
                                                                                                                    source,
                                                                                                                    destination);
                                                                                                        })));
                                    });
                });
    }

    private Mono<Reservation> transition(
            SecurityPrincipal p,
            String tenant,
            Reservation current,
            Action action,
            Account source,
            Account destination) {
        ReservationState target;
        if (action == Action.REVERSE) {
            if (current.state() != ReservationState.COMMITTED)
                return Mono.error(fail(409, "ONLY_COMMITTED_FUNDS_CAN_REVERSE"));
            active(source);
            active(destination);
            if (destination.available().compareTo(current.amount()) < 0)
                return Mono.error(fail(409, "REVERSAL_REQUIRES_AVAILABLE_RECIPIENT_FUNDS"));
            BigDecimal sourceNext = source.booked().add(current.amount()),
                    destinationNext = destination.booked().subtract(current.amount());
            numeric(sourceNext);
            numeric(destinationNext);
            return postings.reverse(
                            current.postingId(),
                            postingKey(tenant, current.operationId(), "REVERSE"))
                    .flatMap(
                            posting ->
                                    update(source, sourceNext, source.held())
                                            .then(
                                                    update(
                                                            destination,
                                                            destinationNext,
                                                            destination.held()))
                                            .then(
                                                    db.sql(
                                                                    "UPDATE"
                                                                        + " ledger_funds_reservation"
                                                                        + " SET state='REVERSED',reversal_id=:r,version=version+1"
                                                                        + " WHERE"
                                                                        + " reservation_id=:id")
                                                            .bind(
                                                                    "r",
                                                                    posting.getTransaction()
                                                                            .getTransactionId())
                                                            .bind("id", current.reservationId())
                                                            .fetch()
                                                            .rowsUpdated())
                                            .then(
                                                    event(
                                                            p,
                                                            tenant,
                                                            current.reservationId(),
                                                            "FUNDS_REVERSED",
                                                            posting.getTransaction()
                                                                    .getTransactionId()))
                                            .then(read(tenant, current.reservationId(), false)));
        }
        if (current.state() != ReservationState.HELD)
            return Mono.error(fail(409, "RESERVATION_IS_TERMINAL"));
        if (action == Action.COMMIT) {
            if (!clock.instant().isBefore(current.expiresAt()))
                return Mono.error(fail(409, "RESERVATION_EXPIRED"));
            active(source);
            active(destination);
            target = ReservationState.COMMITTED;
        } else if (action == Action.EXPIRE) {
            if (clock.instant().isBefore(current.expiresAt()))
                return Mono.error(fail(409, "RESERVATION_NOT_DUE"));
            target = ReservationState.EXPIRED;
        } else target = ReservationState.RELEASED;
        BigDecimal held = source.held().subtract(current.amount());
        if (held.signum() < 0) return Mono.error(fail(409, "FUNDS_HELD_INVARIANT_VIOLATION"));
        BigDecimal sourceNext = source.booked(), destinationNext = destination.booked();
        if (target == ReservationState.COMMITTED) {
            sourceNext = sourceNext.subtract(current.amount());
            destinationNext = destinationNext.add(current.amount());
        }
        numeric(sourceNext);
        numeric(destinationNext);
        Mono<?> ledgerEffect =
                target == ReservationState.COMMITTED
                        ? postings.confirm(current.postingId())
                        : postings.release(current.postingId());
        return ledgerEffect
                .then(update(source, sourceNext, held))
                .then(
                        target == ReservationState.COMMITTED
                                ? update(destination, destinationNext, destination.held())
                                : Mono.empty())
                .then(
                        db.sql(
                                        "UPDATE ledger_funds_reservation SET"
                                                + " state=:state,version=version+1 WHERE"
                                                + " reservation_id=:id")
                                .bind("state", target.name())
                                .bind("id", current.reservationId())
                                .fetch()
                                .rowsUpdated())
                .then(
                        event(
                                p,
                                tenant,
                                current.reservationId(),
                                "FUNDS_" + target.name(),
                                current.postingId()))
                .then(read(tenant, current.reservationId(), false));
    }

    private boolean already(Reservation row, Action action) {
        return switch (action) {
            case COMMIT -> row.state() == ReservationState.COMMITTED;
            case RELEASE ->
                    row.state() == ReservationState.RELEASED
                            || row.state() == ReservationState.EXPIRED;
            case EXPIRE -> row.state() == ReservationState.EXPIRED;
            case REVERSE -> row.state() == ReservationState.REVERSED;
        };
    }

    private Mono<Boolean> actionClaim(String tenant, UUID id, Command input) {
        return db.sql(
                        "INSERT INTO ledger_funds_action(tenant_id,action_id,reservation_id,action)"
                                + " VALUES(:t,:action,:id,:kind) ON CONFLICT DO NOTHING")
                .bind("t", tenant)
                .bind("action", input.actionId())
                .bind("id", id)
                .bind("kind", input.action().name())
                .fetch()
                .rowsUpdated()
                .flatMap(
                        inserted ->
                                db.sql(
                                                "SELECT reservation_id,action FROM"
                                                    + " ledger_funds_action WHERE tenant_id=:t AND"
                                                    + " action_id=:action")
                                        .bind("t", tenant)
                                        .bind("action", input.actionId())
                                        .map(
                                                (r, m) -> {
                                                    if (!id.equals(
                                                                    r.get(
                                                                            "reservation_id",
                                                                            UUID.class))
                                                            || !input.action()
                                                                    .name()
                                                                    .equals(
                                                                            r.get(
                                                                                    "action",
                                                                                    String.class)))
                                                        throw fail(
                                                                409,
                                                                "FUNDS_ACTION_INTENT_CONFLICT");
                                                    return inserted == 0;
                                                })
                                        .one()
                                        .switchIfEmpty(
                                                Mono.error(
                                                        fail(409, "FUNDS_ACTION_SCOPE_CONFLICT"))));
    }

    private Mono<Map<UUID, Account>> lockAccounts(String tenant, UUID... ids) {
        var ordered =
                Arrays.stream(ids).distinct().sorted(Comparator.comparing(UUID::toString)).toList();
        return Flux.fromIterable(ordered)
                .concatMap(authority::gate)
                .thenMany(
                        Flux.fromIterable(ordered)
                                .concatMap(id -> authority.readAccount(tenant, id, true)))
                .collectMap(Account::accountId);
    }

    Mono<Void> operationGate(String tenant, UUID operation) {
        return db.sql("SELECT pg_advisory_xact_lock(hashtextextended(:key,55602))")
                .bind("key", tenant + ":" + operation)
                .fetch()
                .rowsUpdated()
                .then();
    }

    private record Claim(UUID result) {}

    private Mono<Claim> claim(
            String tenant,
            UUID operation,
            String kind,
            String fingerprint,
            UUID source,
            UUID destination) {
        return db.sql(
                        "INSERT INTO"
                            + " ledger_funds_operation(tenant_id,operation_id,kind,fingerprint,source_account_id,destination_account_id)"
                            + " VALUES(:t,:op,:kind,:fp,:source,:destination) ON CONFLICT DO"
                            + " NOTHING")
                .bind("t", tenant)
                .bind("op", operation)
                .bind("kind", kind)
                .bind("fp", fingerprint)
                .bind("source", source)
                .bind("destination", destination)
                .fetch()
                .rowsUpdated()
                .then(
                        db.sql(
                                        "SELECT * FROM ledger_funds_operation WHERE tenant_id=:t"
                                                + " AND operation_id=:op FOR UPDATE")
                                .bind("t", tenant)
                                .bind("op", operation)
                                .map(
                                        (r, m) -> {
                                            if (!kind.equals(r.get("kind", String.class))
                                                    || !fingerprint.equals(
                                                            r.get("fingerprint", String.class)))
                                                throw fail(409, "FUNDS_OPERATION_INTENT_CONFLICT");
                                            if (Boolean.TRUE.equals(
                                                    r.get("closed_without_effect", Boolean.class)))
                                                throw fail(
                                                        409,
                                                        "FUNDS_OPERATION_CLOSED_WITHOUT_EFFECT");
                                            return new Claim(
                                                    r.get(
                                                            kind.equals("RESERVE")
                                                                    ? "reservation_id"
                                                                    : "posting_id",
                                                            UUID.class));
                                        })
                                .one()
                                .switchIfEmpty(
                                        Mono.error(fail(409, "FUNDS_OPERATION_SCOPE_CONFLICT"))));
    }

    private Mono<Void> completeOperation(
            String tenant, UUID operation, UUID reservation, UUID posting) {
        var statement =
                db.sql(
                                "UPDATE ledger_funds_operation SET "
                                        + (reservation != null ? "reservation_id" : "posting_id")
                                        + "=:id WHERE tenant_id=:t AND operation_id=:op")
                        .bind("id", reservation != null ? reservation : posting)
                        .bind("t", tenant)
                        .bind("op", operation);
        return statement.fetch().rowsUpdated().then();
    }

    private Mono<Void> update(Account account, BigDecimal booked, BigDecimal held) {
        return db.sql(
                        "UPDATE ledger_funds_account SET booked=:b,held=:h,version=version+1 WHERE"
                                + " account_id=:a")
                .bind("b", booked)
                .bind("h", held)
                .bind("a", account.accountId())
                .fetch()
                .rowsUpdated()
                .then();
    }

    private Mono<UUID> createPosting(
            String tenant,
            UUID operation,
            UUID debit,
            UUID credit,
            BigDecimal amount,
            String currency,
            boolean pending) {
        var header = new LedgerPostingHeaderDTO();
        header.setAccountId(debit);
        header.setCurrency(currency);
        header.setTotalAmount(amount);
        header.setTransactionType(TransactionTypeEnum.TRANSFER);
        header.setTransactionStatus(
                pending
                        ? LedgerPostingHeaderDTO.InitialStatus.PENDING
                        : LedgerPostingHeaderDTO.InitialStatus.POSTED);
        var request = new LedgerPostingRequestDTO();
        request.setIdempotencyKey(postingKey(tenant, operation, pending ? "RESERVE" : "FUNDING"));
        request.setTransaction(header);
        List<LedgerPostingLegDTO> legs = new ArrayList<>();
        for (var entry : List.of(Map.entry(debit, "DEBIT"), Map.entry(credit, "CREDIT"))) {
            var leg = new LedgerPostingLegDTO();
            leg.setAccountId(entry.getKey());
            leg.setLegType(entry.getValue());
            leg.setAmount(amount);
            leg.setCurrency(currency);
            legs.add(leg);
        }
        request.setLegs(legs);
        return postings.create(request).map(row -> row.getTransaction().getTransactionId());
    }

    Mono<Reservation> read(String tenant, UUID id, boolean lock) {
        return db.sql(
                        "SELECT * FROM ledger_funds_reservation WHERE tenant_id=:t AND"
                                + " reservation_id=:id"
                                + (lock ? " FOR UPDATE" : ""))
                .bind("t", tenant)
                .bind("id", id)
                .map(
                        (r, m) ->
                                new Reservation(
                                        id,
                                        r.get("operation_id", UUID.class),
                                        r.get("source_account_id", UUID.class),
                                        r.get("destination_account_id", UUID.class),
                                        r.get("amount", BigDecimal.class),
                                        r.get("currency", String.class),
                                        ReservationState.valueOf(r.get("state", String.class)),
                                        r.get("expires_at", OffsetDateTime.class).toInstant(),
                                        r.get("posting_id", UUID.class),
                                        r.get("reversal_id", UUID.class),
                                        r.get("version", Long.class)))
                .one()
                .switchIfEmpty(Mono.error(fail(404, "FUNDS_RESERVATION_NOT_FOUND")));
    }

    private Mono<Void> event(
            SecurityPrincipal p, String tenant, UUID resource, String type, UUID posting) {
        UUID event = UUID.randomUUID();
        String payload = "{\"resourceId\":\"" + resource + "\",\"postingId\":\"" + posting + "\"}";
        return db.sql(
                        "INSERT INTO"
                            + " ledger_funds_event(event_id,tenant_id,resource_id,actor,event_type,payload)"
                            + " VALUES(:id,:t,:r,:actor,:type,CAST(:payload AS jsonb))")
                .bind("id", event)
                .bind("t", tenant)
                .bind("r", resource)
                .bind("actor", p.subject())
                .bind("type", type)
                .bind("payload", payload)
                .fetch()
                .rowsUpdated()
                .then(
                        db.sql(
                                        "INSERT INTO"
                                            + " event_outbox(event_id,aggregate_type,aggregate_id,event_type,payload,funds_tenant_id)"
                                            + " VALUES(:id,'MANAGED_FUNDS',:r,:type,CAST(:payload"
                                            + " AS jsonb),:t)")
                                .bind("id", event)
                                .bind("r", resource.toString())
                                .bind("type", type)
                                .bind("payload", payload)
                                .bind("t", tenant)
                                .fetch()
                                .rowsUpdated())
                .then();
    }

    private static void active(Account account) {
        if (account.state() != AccountState.ACTIVE) throw fail(409, "FUNDS_ACCOUNT_BLOCKED");
    }

    private static void amount(BigDecimal amount, Account account) {
        if (amount.stripTrailingZeros().scale() > account.currencyScale())
            throw fail(400, "CURRENCY_PRECISION_EXCEEDED");
    }

    static void numeric(BigDecimal value) {
        if (value.signum() != 0 && (long) value.precision() - value.scale() > 20)
            throw fail(400, "FUNDS_NUMERIC_RANGE_EXCEEDED");
        BigDecimal normalized = value.stripTrailingZeros();
        if ((long) normalized.precision() - normalized.scale() > 20 || normalized.scale() > 18)
            throw fail(400, "FUNDS_NUMERIC_RANGE_EXCEEDED");
    }

    private static void validate(
            UUID operation, UUID source, UUID destination, BigDecimal amount, String currency) {
        if (operation == null
                || source == null
                || destination == null
                || source.equals(destination)
                || amount == null
                || amount.signum() <= 0
                || currency == null
                || !currency.matches("[A-Z]{3}")) throw fail(400, "INVALID_FUNDS_INTENT");
        numeric(amount);
    }

    static void validateReservation(Reserve input) {
        if (input == null) throw fail(400, "RESERVATION_INPUT_REQUIRED");
        validate(
                input.operationId(),
                input.sourceAccountId(),
                input.destinationAccountId(),
                input.amount(),
                input.currency());
        if (input.ttlSeconds() < 1 || input.ttlSeconds() > 900)
            throw fail(400, "RESERVATION_TTL_OUT_OF_RANGE");
        if (input.notAfterEpochMillis() != null
                && (input.notAfterEpochMillis() <= 0
                        || input.notAfterEpochMillis() > 253402300799999L))
            throw fail(400, "RESERVATION_DEADLINE_OUT_OF_RANGE");
    }

    static String reservationFingerprint(Reserve input) {
        return hash(
                "RESERVE",
                input.sourceAccountId(),
                input.destinationAccountId(),
                canonical(input.amount()),
                input.currency(),
                input.ttlSeconds(),
                input.notAfterEpochMillis());
    }

    static String reservationIntentJson(Reserve input) {
        return "{\"operationId\":\""
                + input.operationId()
                + "\",\"sourceAccountId\":\""
                + input.sourceAccountId()
                + "\",\"destinationAccountId\":\""
                + input.destinationAccountId()
                + "\",\"amount\":"
                + canonical(input.amount())
                + ",\"currency\":\""
                + input.currency()
                + "\",\"ttlSeconds\":"
                + input.ttlSeconds()
                + ",\"notAfterEpochMillis\":"
                + input.notAfterEpochMillis()
                + "}";
    }

    private static String canonical(BigDecimal amount) {
        return amount.stripTrailingZeros().toPlainString();
    }

    static String postingKey(String tenant, UUID operation, String kind) {
        return "fnd:" + hash(tenant, operation, kind);
    }

    private static String hash(Object... values) {
        try {
            return HexFormat.of()
                    .formatHex(
                            MessageDigest.getInstance("SHA-256")
                                    .digest(
                                            Arrays.stream(values)
                                                    .map(String::valueOf)
                                                    .collect(
                                                            java.util.stream.Collectors.joining(
                                                                    "|"))
                                                    .getBytes(StandardCharsets.UTF_8)));
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
