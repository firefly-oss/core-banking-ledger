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

package io.fireflysoftware.core.banking.ledger.sdk.api;

import io.fireflysoftware.core.banking.ledger.sdk.invoker.ApiClient;
import io.fireflysoftware.core.banking.ledger.sdk.model.TransactionDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

/**
 * Reads {@code GET /api/v1/transactions/by-account/{accountId}} from core-banking-ledger.
 */
public class AccountTransactionsApi {

    private final WebClient webClient;
    private final String basePath;

    public AccountTransactionsApi(ApiClient apiClient) {
        this.webClient = apiClient.getWebClient();
        this.basePath = trim(apiClient.getBasePath());
    }

    public Flux<TransactionDTO> listByAccount(UUID accountId) {
        if (accountId == null) {
            return Flux.empty();
        }
        return webClient.get()
                .uri(basePath + "/api/v1/transactions/by-account/{accountId}", accountId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<TransactionDTO>>() {
                })
                .flatMapMany(Flux::fromIterable);
    }

    private static String trim(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalStateException("PROPERTY_NOT_SET");
        }
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}
