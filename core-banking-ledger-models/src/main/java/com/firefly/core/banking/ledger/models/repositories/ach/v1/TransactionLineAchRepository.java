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


package com.firefly.core.banking.ledger.models.repositories.ach.v1;

import com.firefly.core.banking.ledger.models.entities.ach.v1.TransactionLineAch;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Repository interface for TransactionLineAch entity.
 */
public interface TransactionLineAchRepository extends BaseRepository<TransactionLineAch, UUID> {
    /**
     * Find ACH transaction line by transaction ID.
     *
     * @param transactionId The transaction ID
     * @return A Mono containing the ACH transaction line if found
     */
    Mono<TransactionLineAch> findByTransactionId(UUID transactionId);

    /**
     * Find ACH transaction lines by source account ID.
     *
     * @param sourceAccountId The source account ID
     * @param pageable Pagination information
     * @return A Flux of ACH transaction lines
     */
    @Query("SELECT * FROM transaction_line_ach " +
            "WHERE ach_source_account_id = :sourceAccountId " +
            "ORDER BY ach_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineAch> findByAchSourceAccountId(Long sourceAccountId, Pageable pageable);

    /**
     * Count ACH transaction lines by source account ID.
     *
     * @param sourceAccountId The source account ID
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_ach " +
            "WHERE ach_source_account_id = :sourceAccountId")
    Mono<Long> countByAchSourceAccountId(Long sourceAccountId);

    /**
     * Find ACH transaction lines by destination account ID.
     *
     * @param destinationAccountId The destination account ID
     * @param pageable Pagination information
     * @return A Flux of ACH transaction lines
     */
    @Query("SELECT * FROM transaction_line_ach " +
            "WHERE ach_destination_account_id = :destinationAccountId " +
            "ORDER BY ach_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineAch> findByAchDestinationAccountId(Long destinationAccountId, Pageable pageable);

    /**
     * Count ACH transaction lines by destination account ID.
     *
     * @param destinationAccountId The destination account ID
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_ach " +
            "WHERE ach_destination_account_id = :destinationAccountId")
    Mono<Long> countByAchDestinationAccountId(Long destinationAccountId);

    /**
     * Find ACH transaction lines by custom criteria.
     *
     * @param startDate The start date for the search
     * @param endDate The end date for the search
     * @param sourceAccountId The source account ID
     * @param destinationAccountId The destination account ID
     * @param minAmount The minimum transfer amount
     * @param maxAmount The maximum transfer amount
     * @param pageable Pagination information
     * @return A Flux of ACH transaction lines
     */
    @Query("SELECT tla.* FROM transaction_line_ach tla " +
            "JOIN transaction t ON t.transaction_id = tla.transaction_id " +
            "WHERE (:startDate IS NULL OR tla.ach_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tla.ach_timestamp <= :endDate) " +
            "AND (:sourceAccountId IS NULL OR tla.ach_source_account_id = :sourceAccountId) " +
            "AND (:destinationAccountId IS NULL OR tla.ach_destination_account_id = :destinationAccountId) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount) " +
            "ORDER BY tla.ach_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineAch> findByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long sourceAccountId,
            Long destinationAccountId,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable);

    /**
     * Count ACH transaction lines by custom criteria.
     *
     * @param startDate The start date for the search
     * @param endDate The end date for the search
     * @param sourceAccountId The source account ID
     * @param destinationAccountId The destination account ID
     * @param minAmount The minimum transfer amount
     * @param maxAmount The maximum transfer amount
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_ach tla " +
            "JOIN transaction t ON t.transaction_id = tla.transaction_id " +
            "WHERE (:startDate IS NULL OR tla.ach_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tla.ach_timestamp <= :endDate) " +
            "AND (:sourceAccountId IS NULL OR tla.ach_source_account_id = :sourceAccountId) " +
            "AND (:destinationAccountId IS NULL OR tla.ach_destination_account_id = :destinationAccountId) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount)")
    Mono<Long> countByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long sourceAccountId,
            Long destinationAccountId,
            BigDecimal minAmount,
            BigDecimal maxAmount);

    /**
     * Find ACH transaction lines by routing number.
     *
     * @param routingNumber The ACH routing number
     * @param pageable Pagination information
     * @return A Flux of ACH transaction lines
     */
    @Query("SELECT * FROM transaction_line_ach " +
            "WHERE ach_routing_number = :routingNumber " +
            "ORDER BY ach_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineAch> findByAchRoutingNumber(String routingNumber, Pageable pageable);

    /**
     * Find ACH transaction lines by batch number.
     *
     * @param batchNumber The ACH batch number
     * @param pageable Pagination information
     * @return A Flux of ACH transaction lines
     */
    @Query("SELECT * FROM transaction_line_ach " +
            "WHERE ach_batch_number = :batchNumber " +
            "ORDER BY ach_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineAch> findByAchBatchNumber(String batchNumber, Pageable pageable);
}