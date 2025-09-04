/*
 * Copyright 2025 Firefly Software Solutions Inc
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


package com.firefly.core.banking.ledger.models.repositories.card.v1;

import com.firefly.core.banking.ledger.models.entities.card.v1.TransactionLineCard;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionLineCardRepository extends BaseRepository<TransactionLineCard, UUID> {
    Mono<TransactionLineCard> findByTransactionId(UUID transactionId);

    @Query("SELECT * FROM transaction_line_card " +
            "WHERE card_merchant_category_code = :merchantCode " +
            "ORDER BY card_transaction_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineCard> findByCardMerchantCode(String merchantCode, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_card " +
            "WHERE card_merchant_category_code = :merchantCode")
    Mono<Long> countByCardMerchantCode(String merchantCode);

    @Query("SELECT * FROM transaction_line_card " +
            "WHERE card_terminal_id = :terminalId " +
            "ORDER BY card_transaction_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineCard> findByCardTerminalId(String terminalId, Pageable pageable);

    Mono<Long> countByCardTerminalId(String terminalId);

    @Query("SELECT * FROM transaction_line_card " +
            "WHERE card_fraud_flag = :fraudFlag " +
            "ORDER BY card_transaction_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineCard> findByCardFraudFlag(Boolean fraudFlag, Pageable pageable);

    @Query("SELECT * FROM transaction_line_card " +
            "WHERE card_merchant_name ILIKE concat('%', :merchantName, '%') " +
            "ORDER BY card_transaction_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineCard> findByMerchantNameContaining(String merchantName, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_card " +
            "WHERE card_merchant_name ILIKE concat('%', :merchantName, '%')")
    Mono<Long> countByMerchantNameContaining(String merchantName);

    @Query("SELECT tlc.* FROM transaction_line_card tlc " +
            "JOIN transaction t ON t.transaction_id = tlc.transaction_id " +
            "WHERE (:startDate IS NULL OR tlc.card_transaction_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tlc.card_transaction_timestamp <= :endDate) " +
            "AND (:merchantCode IS NULL OR tlc.card_merchant_category_code = :merchantCode) " +
            "AND (:cardPresent IS NULL OR tlc.card_present_flag = :cardPresent) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount)")
    Flux<TransactionLineCard> findByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String merchantCode,
            Boolean cardPresent,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_card tlc " +
            "JOIN transaction t ON t.transaction_id = tlc.transaction_id " +
            "WHERE (:startDate IS NULL OR tlc.card_transaction_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tlc.card_transaction_timestamp <= :endDate) " +
            "AND (:merchantCode IS NULL OR tlc.card_merchant_category_code = :merchantCode) " +
            "AND (:cardPresent IS NULL OR tlc.card_present_flag = :cardPresent) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount)")
    Mono<Long> countByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String merchantCode,
            Boolean cardPresent,
            BigDecimal minAmount,
            BigDecimal maxAmount);

    @Query("SELECT * FROM transaction_line_card " +
            "WHERE card_holder_country = :country " +
            "AND card_fraud_flag = true " +
            "ORDER BY card_transaction_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineCard> findFraudulentTransactionsByCountry(String country, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_card " +
            "WHERE card_holder_country = :country " +
            "AND card_fraud_flag = true")
    Mono<Long> countFraudulentTransactionsByCountry(String country);
}