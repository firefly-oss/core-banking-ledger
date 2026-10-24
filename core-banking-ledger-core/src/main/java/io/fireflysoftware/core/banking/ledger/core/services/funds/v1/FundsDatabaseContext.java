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

import org.fireflyframework.security.api.domain.SecurityPrincipal;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public final class FundsDatabaseContext {
    private final DatabaseClient db;

    public FundsDatabaseContext(DatabaseClient db) {
        this.db = db;
    }

    Mono<Void> establish(SecurityPrincipal p, String tenant, String purpose, UUID... accounts) {
        String scope =
                switch (purpose) {
                    case "ENROLL" -> "funds:enroll";
                    case "READ" -> "funds:read";
                    case "CONTROL" -> "funds:control";
                    case "FUNDING" -> "funds:fund";
                    case "RESERVE" -> "funds:reserve";
                    case "COMMIT" -> "funds:commit";
                    case "RELEASE" -> "funds:release";
                    case "EXPIRE" -> "funds:expire";
                    case "REVERSE" -> "funds:reverse";
                    case "RECONCILE" -> "funds:reconcile";
                    default -> throw fail(403, "SERVER_FUNDS_PURPOSE_REQUIRED");
                };
        FundsPolicy.require(p, tenant, scope, accounts);
        if (accounts.length < 1 || accounts.length > 1000)
            return Mono.error(fail(403, "BOUNDED_FUNDS_RESOURCES_REQUIRED"));
        String resourceJson =
                Arrays.stream(accounts)
                        .distinct()
                        .map(a -> "\"" + a + "\"")
                        .collect(Collectors.joining(",", "[", "]"));
        return db.sql(
                        """
                        SELECT NOT r.rolsuper AND NOT r.rolbypassrls
                          AND NOT pg_catalog.has_schema_privilege(current_user,'public','CREATE')
                          AND NOT pg_catalog.has_schema_privilege(current_user,'funds_private','USAGE')
                          AND NOT EXISTS(SELECT 1 FROM pg_catalog.pg_class c JOIN pg_catalog.pg_namespace n ON n.oid=c.relnamespace
                            WHERE n.nspname='public' AND c.relrowsecurity AND pg_catalog.pg_get_userbyid(c.relowner)=current_user)
                          AND EXISTS(SELECT 1 FROM pg_catalog.pg_proc p JOIN pg_catalog.pg_namespace n ON n.oid=p.pronamespace
                            JOIN pg_catalog.pg_roles owner ON owner.oid=p.proowner WHERE n.nspname='public' AND p.proname='funds_reference_allowed'
                            AND p.prosecdef AND NOT owner.rolsuper AND NOT owner.rolbypassrls AND NOT owner.rolcanlogin) AS allowed
                        FROM pg_catalog.pg_roles r WHERE r.rolname=current_user
                        """)
                .map((row, m) -> Boolean.TRUE.equals(row.get("allowed", Boolean.class)))
                .one()
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(fail(503, "NONPRIVILEGED_FUNDS_RUNTIME_REQUIRED")))
                .then(
                        db.sql(
                                        "SELECT"
                                            + " set_config('firefly.funds.tenant',:t,true),set_config('firefly.funds.accounts',:a,true),set_config('firefly.funds.purpose',:p,true)")
                                .bind("t", tenant)
                                .bind("a", resourceJson)
                                .bind("p", purpose)
                                .fetch()
                                .rowsUpdated())
                .then();
    }
}
