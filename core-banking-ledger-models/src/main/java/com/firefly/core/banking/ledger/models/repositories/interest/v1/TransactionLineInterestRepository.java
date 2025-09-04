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


package com.firefly.core.banking.ledger.models.repositories.interest.v1;

import com.firefly.core.banking.ledger.models.entities.interest.v1.TransactionLineInterest;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Repository interface for TransactionLineInterest entity.
 */
public interface TransactionLineInterestRepository extends BaseRepository<TransactionLineInterest, UUID> {
    /**
     * Find interest transaction line by transaction ID.
     *
     * @param transactionId The transaction ID
     * @return A Mono containing the interest transaction line if found
     */
    Mono<TransactionLineInterest> findByTransactionId(UUID transactionId);

    /**
     * Find interest transaction lines by interest type.
     *
     * @param interestType The interest type
     * @param pageable Pagination information
     * @return A Flux of interest transaction lines
     */
    @Query("SELECT * FROM transaction_line_interest " +
            "WHERE interest_type = :interestType " +
            "ORDER BY interest_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineInterest> findByInterestType(String interestType, Pageable pageable);

    /**
     * Count interest transaction lines by interest type.
     *
     * @param interestType The interest type
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_interest " +
            "WHERE interest_type = :interestType")
    Mono<Long> countByInterestType(String interestType);

    /**
     * Find interest transaction lines by related account ID.
     *
     * @param relatedAccountId The related account ID
     * @param pageable Pagination information
     * @return A Flux of interest transaction lines
     */
    @Query("SELECT * FROM transaction_line_interest " +
            "WHERE interest_related_account_id = :relatedAccountId " +
            "ORDER BY interest_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineInterest> findByInterestRelatedAccountId(Long relatedAccountId, Pageable pageable);

    /**
     * Count interest transaction lines by related account ID.
     *
     * @param relatedAccountId The related account ID
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_interest " +
            "WHERE interest_related_account_id = :relatedAccountId")
    Mono<Long> countByInterestRelatedAccountId(Long relatedAccountId);

    /**
     * Find interest transaction lines by accrual period.
     *
     * @param startDate The accrual start date
     * @param endDate The accrual end date
     * @param pageable Pagination information
     * @return A Flux of interest transaction lines
     */
    @Query("SELECT * FROM transaction_line_interest " +
            "WHERE interest_accrual_start_date >= :startDate " +
            "AND interest_accrual_end_date <= :endDate " +
            "ORDER BY interest_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineInterest> findByAccrualPeriod(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Count interest transaction lines by accrual period.
     *
     * @param startDate The accrual start date
     * @param endDate The accrual end date
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_interest " +
            "WHERE interest_accrual_start_date >= :startDate " +
            "AND interest_accrual_end_date <= :endDate")
    Mono<Long> countByAccrualPeriod(LocalDate startDate, LocalDate endDate);

    /**
     * Find interest transaction lines by custom criteria.
     *
     * @param startDate The start date for the search
     * @param endDate The end date for the search
     * @param interestType The interest type
     * @param relatedAccountId The related account ID
     * @param minRate The minimum interest rate
     * @param maxRate The maximum interest rate
     * @param pageable Pagination information
     * @return A Flux of interest transaction lines
     */
    @Query("SELECT tli.* FROM transaction_line_interest tli " +
            "WHERE (:startDate IS NULL OR tli.interest_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tli.interest_timestamp <= :endDate) " +
            "AND (:interestType IS NULL OR tli.interest_type = :interestType) " +
            "AND (:relatedAccountId IS NULL OR tli.interest_related_account_id = :relatedAccountId) " +
            "AND (:minRate IS NULL OR tli.interest_rate_percentage >= :minRate) " +
            "AND (:maxRate IS NULL OR tli.interest_rate_percentage <= :maxRate) " +
            "ORDER BY tli.interest_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineInterest> findByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String interestType,
            Long relatedAccountId,
            BigDecimal minRate,
            BigDecimal maxRate,
            Pageable pageable);

    /**
     * Count interest transaction lines by custom criteria.
     *
     * @param startDate The start date for the search
     * @param endDate The end date for the search
     * @param interestType The interest type
     * @param relatedAccountId The related account ID
     * @param minRate The minimum interest rate
     * @param maxRate The maximum interest rate
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_interest tli " +
            "WHERE (:startDate IS NULL OR tli.interest_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tli.interest_timestamp <= :endDate) " +
            "AND (:interestType IS NULL OR tli.interest_type = :interestType) " +
            "AND (:relatedAccountId IS NULL OR tli.interest_related_account_id = :relatedAccountId) " +
            "AND (:minRate IS NULL OR tli.interest_rate_percentage >= :minRate) " +
            "AND (:maxRate IS NULL OR tli.interest_rate_percentage <= :maxRate)")
    Mono<Long> countByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String interestType,
            Long relatedAccountId,
            BigDecimal minRate,
            BigDecimal maxRate);
}
