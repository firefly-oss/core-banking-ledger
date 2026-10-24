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

import io.fireflysoftware.core.banking.ledger.interfaces.dtos.funds.v1.FundsContracts.*;

import org.fireflyframework.security.api.domain.SecurityPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Service
public class FundsAuthority {
    private final DatabaseClient database;
    private final String currencyPolicy;
    private final AccountEnrollmentVerifier verifier;
    private final FundsDatabaseContext context;

    public FundsAuthority(
            DatabaseClient database,
            @Value("${funds.currency-policy.version:}") String currencyPolicy,
            AccountEnrollmentVerifier verifier,
            FundsDatabaseContext context) {
        this.database = database;
        this.verifier = verifier;
        this.context = context;
        this.currencyPolicy = currencyPolicy;
    }

    @Transactional
    public Mono<Account> enroll(
            SecurityPrincipal principal, String tenant, UUID accountId, Enrollment input) {
        return Mono.defer(
                () -> {
                    FundsPolicy.require(principal, tenant, "funds:enroll", accountId);
                    if (input == null
                            || input.enrollmentId() == null
                            || input.contractId() == null
                            || input.currency() == null
                            || !input.currency().matches("[A-Z]{3}")
                            || input.kind() == null
                            || currencyPolicy.isBlank()
                            || currencyPolicy.length() > 100)
                        return Mono.error(fail(400, "EXPLICIT_FUNDS_ENROLLMENT_REQUIRED"));
                    FundsPolicy.enrollment(
                            principal,
                            tenant,
                            accountId,
                            input.contractId(),
                            input.currency(),
                            input.kind().name());
                    int scale;
                    try {
                        scale = Currency.getInstance(input.currency()).getDefaultFractionDigits();
                    } catch (IllegalArgumentException e) {
                        return Mono.error(fail(400, "UNSUPPORTED_FUNDS_CURRENCY"));
                    }
                    if (scale < 0 || scale > 18)
                        return Mono.error(fail(400, "UNSUPPORTED_FUNDS_PRECISION"));
                    if (input.kind() == Kind.BANK_CONTROL)
                        FundsPolicy.require(principal, tenant, "funds:bank-enroll", accountId);
                    return context.establish(principal, tenant, "ENROLL", accountId)
                            .then(gate(accountId))
                            .then(
                                    database.sql(
                                                    """
                                                    INSERT INTO ledger_funds_account(tenant_id,account_id,enrollment_id,contract_id,currency,currency_scale,currency_policy,kind)
                                                    VALUES (:tenant,:account,:enrollment,:contract,:currency,:scale,:policy,:kind)
                                                    ON CONFLICT(account_id) DO NOTHING
                                                    """)
                                            .bind("tenant", tenant)
                                            .bind("account", accountId)
                                            .bind("enrollment", input.enrollmentId())
                                            .bind("contract", input.contractId())
                                            .bind("currency", input.currency())
                                            .bind("scale", scale)
                                            .bind("policy", currencyPolicy)
                                            .bind("kind", input.kind().name())
                                            .fetch()
                                            .rowsUpdated())
                            .then(readAccount(tenant, accountId, true))
                            .flatMap(
                                    account -> {
                                        if (!account.enrollmentId().equals(input.enrollmentId())
                                                || !account.contractId().equals(input.contractId())
                                                || !account.currency().equals(input.currency())
                                                || account.kind() != input.kind())
                                            return Mono.error(
                                                    fail(409, "FUNDS_ENROLLMENT_INTENT_CONFLICT"));
                                        return Mono.just(account);
                                    });
                });
    }

    @Transactional
    public Mono<Account> control(
            SecurityPrincipal principal, String tenant, UUID accountId, Control input) {
        return Mono.defer(
                () -> {
                    FundsPolicy.require(principal, tenant, "funds:control", accountId);
                    if (input == null || input.actionId() == null || input.state() == null)
                        return Mono.error(fail(400, "FUNDS_CONTROL_REQUIRED"));
                    return context.establish(principal, tenant, "CONTROL", accountId)
                            .then(gate(accountId))
                            .then(readAccount(tenant, accountId, true))
                            .flatMap(
                                    current ->
                                            database.sql(
                                                            "INSERT INTO"
                                                                + " ledger_funds_control(tenant_id,action_id,account_id,state)"
                                                                + " VALUES(:t,:action,:a,:state) ON"
                                                                + " CONFLICT DO NOTHING")
                                                    .bind("t", tenant)
                                                    .bind("action", input.actionId())
                                                    .bind("a", accountId)
                                                    .bind("state", input.state().name())
                                                    .fetch()
                                                    .rowsUpdated()
                                                    .flatMap(
                                                            inserted ->
                                                                    database.sql(
                                                                                    "SELECT"
                                                                                        + " account_id,state"
                                                                                        + " FROM"
                                                                                        + " ledger_funds_control"
                                                                                        + " WHERE"
                                                                                        + " tenant_id=:t"
                                                                                        + " AND action_id=:action")
                                                                            .bind("t", tenant)
                                                                            .bind(
                                                                                    "action",
                                                                                    input
                                                                                            .actionId())
                                                                            .map(
                                                                                    (r, m) -> {
                                                                                        if (!accountId
                                                                                                        .equals(
                                                                                                                r
                                                                                                                        .get(
                                                                                                                                "account_id",
                                                                                                                                UUID.class))
                                                                                                || !input.state()
                                                                                                        .name()
                                                                                                        .equals(
                                                                                                                r
                                                                                                                        .get(
                                                                                                                                "state",
                                                                                                                                String
                                                                                                                                        .class)))
                                                                                            throw fail(
                                                                                                    409,
                                                                                                    "FUNDS_CONTROL_INTENT_CONFLICT");
                                                                                        return inserted
                                                                                                == 0;
                                                                                    })
                                                                            .one()
                                                                            .switchIfEmpty(
                                                                                    Mono.error(
                                                                                            fail(
                                                                                                    409,
                                                                                                    "FUNDS_CONTROL_SCOPE_CONFLICT"))))
                                                    .flatMap(
                                                            replayed -> {
                                                                if (replayed)
                                                                    return Mono.just(current);
                                                                if (current.state()
                                                                                == AccountState
                                                                                        .CLOSED
                                                                        && input.state()
                                                                                != AccountState
                                                                                        .CLOSED)
                                                                    return Mono.error(
                                                                            fail(
                                                                                    409,
                                                                                    "CLOSED_FUNDS_ACCOUNT_IS_FINAL"));
                                                                if (input.state()
                                                                                == AccountState
                                                                                        .CLOSED
                                                                        && (current.state()
                                                                                        != AccountState
                                                                                                .BLOCKED
                                                                                || current.booked()
                                                                                                .signum()
                                                                                        != 0
                                                                                || current.held()
                                                                                                .signum()
                                                                                        != 0))
                                                                    return Mono.error(
                                                                            fail(
                                                                                    409,
                                                                                    "CLOSURE_REQUIRES_BLOCKED_ZERO_FUNDS"));
                                                                Mono<Void> verify =
                                                                        input.state()
                                                                                        == AccountState
                                                                                                .ACTIVE
                                                                                ? verifier.verify(
                                                                                        tenant,
                                                                                        current)
                                                                                : Mono.empty();
                                                                return verify.then(
                                                                        Mono.defer(
                                                                                () -> {
                                                                                    FundsPolicy
                                                                                            .require(
                                                                                                    principal,
                                                                                                    tenant,
                                                                                                    "funds:control",
                                                                                                    accountId);
                                                                                    return database.sql(
                                                                                                    "UPDATE"
                                                                                                        + " ledger_funds_account"
                                                                                                        + " SET state=:state,version=version+1"
                                                                                                        + " WHERE"
                                                                                                        + " account_id=:a")
                                                                                            .bind(
                                                                                                    "state",
                                                                                                    input.state()
                                                                                                            .name())
                                                                                            .bind(
                                                                                                    "a",
                                                                                                    accountId)
                                                                                            .fetch()
                                                                                            .rowsUpdated()
                                                                                            .then(
                                                                                                    readAccount(
                                                                                                            tenant,
                                                                                                            accountId,
                                                                                                            false));
                                                                                }));
                                                            }));
                });
    }

    @Transactional(readOnly = true)
    public Mono<Account> account(SecurityPrincipal principal, String tenant, UUID accountId) {
        return Mono.defer(
                () -> {
                    FundsPolicy.require(principal, tenant, "funds:read", accountId);
                    return context.establish(principal, tenant, "READ", accountId)
                            .then(readAccount(tenant, accountId, false));
                });
    }

    Mono<Void> context(SecurityPrincipal p, String tenant, String purpose, UUID... accounts) {
        return context.establish(p, tenant, purpose, accounts);
    }

    Mono<Void> gate(UUID account) {
        return database.sql("SELECT pg_advisory_xact_lock(hashtextextended(:account,55601))")
                .bind("account", account.toString())
                .fetch()
                .rowsUpdated()
                .then();
    }

    Mono<Account> readAccount(String tenant, UUID accountId, boolean locked) {
        return database.sql(
                        "SELECT * FROM ledger_funds_account WHERE tenant_id=:tenant AND"
                                + " account_id=:account"
                                + (locked ? " FOR UPDATE" : ""))
                .bind("tenant", tenant)
                .bind("account", accountId)
                .map(
                        (r, m) ->
                                new Account(
                                        r.get("account_id", UUID.class),
                                        r.get("enrollment_id", UUID.class),
                                        r.get("contract_id", UUID.class),
                                        r.get("currency", String.class),
                                        r.get("currency_scale", Integer.class),
                                        r.get("currency_policy", String.class),
                                        Kind.valueOf(r.get("kind", String.class)),
                                        AccountState.valueOf(r.get("state", String.class)),
                                        r.get("booked", BigDecimal.class),
                                        r.get("held", BigDecimal.class),
                                        r.get("booked", BigDecimal.class)
                                                .subtract(r.get("held", BigDecimal.class)),
                                        r.get("version", Long.class)))
                .one()
                .switchIfEmpty(Mono.error(fail(404, "FUNDS_ACCOUNT_NOT_FOUND")));
    }

    static ResponseStatusException fail(int status, String reason) {
        return new ResponseStatusException(HttpStatus.valueOf(status), reason);
    }
}
