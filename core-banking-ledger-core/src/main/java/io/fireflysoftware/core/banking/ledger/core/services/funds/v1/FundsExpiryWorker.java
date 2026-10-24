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
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
public final class FundsExpiryWorker {
    private final DatabaseClient database;
    private final FundsDatabaseContext context;
    private final FundsMoneyService money;
    private final TransactionalOperator transaction;
    private final Clock clock;

    public FundsExpiryWorker(
            DatabaseClient database,
            FundsDatabaseContext context,
            FundsMoneyService money,
            ReactiveTransactionManager manager,
            ObjectProvider<Clock> clock) {
        this.database = database;
        this.context = context;
        this.money = money;
        this.transaction = TransactionalOperator.create(manager);
        this.clock = clock.getIfAvailable(Clock::systemUTC);
    }

    public Mono<List<Reservation>> expire(SecurityPrincipal p, String tenant, ExpiryBatch input) {
        return Mono.defer(
                () -> {
                    FundsPolicy.require(p, tenant, "funds:expire");
                    if (input == null
                            || input.limit() == null
                            || input.limit() < 1
                            || input.limit() > 100)
                        return Mono.error(fail(400, "BOUNDED_EXPIRY_BATCH_REQUIRED"));
                    return context.establish(p, tenant, "EXPIRE", FundsPolicy.grantedAccounts(p))
                            .thenMany(
                                    database.sql(
                                                    "SELECT reservation_id FROM"
                                                        + " ledger_funds_reservation WHERE"
                                                        + " tenant_id=:t AND state='HELD' AND"
                                                        + " expires_at<=:now ORDER BY"
                                                        + " expires_at,reservation_id LIMIT :limit")
                                            .bind("t", tenant)
                                            .bind(
                                                    "now",
                                                    OffsetDateTime.ofInstant(
                                                            clock.instant(), ZoneOffset.UTC))
                                            .bind("limit", input.limit())
                                            .map((r, m) -> r.get("reservation_id", UUID.class))
                                            .all())
                            .collectList()
                            .as(transaction::transactional)
                            .flatMapMany(
                                    ids ->
                                            Flux.fromIterable(ids)
                                                    .concatMap(id -> expireOne(p, tenant, id)))
                            .collectList();
                });
    }

    private Mono<Reservation> expireOne(SecurityPrincipal p, String tenant, UUID id) {
        UUID action =
                UUID.nameUUIDFromBytes(
                        ("funds-expiry:" + tenant + ":" + id).getBytes(StandardCharsets.UTF_8));
        return money.action(p, tenant, id, new Command(action, Action.EXPIRE))
                .onErrorResume(
                        org.springframework.web.server.ResponseStatusException.class,
                        error ->
                                error.getStatusCode().value() == 409
                                                && "RESERVATION_IS_TERMINAL"
                                                        .equals(error.getReason())
                                        ? Mono.empty()
                                        : Mono.error(error));
    }
}
