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


package com.firefly.core.banking.ledger.models.repositories.standingorder.v1;

import com.firefly.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderFrequencyEnum;
import com.firefly.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderStatusEnum;
import com.firefly.core.banking.ledger.models.entities.standingorder.v1.TransactionLineStandingOrder;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionLineStandingOrderRepository extends BaseRepository<TransactionLineStandingOrder, UUID> {
    Mono<TransactionLineStandingOrder> findByTransactionId(UUID transactionId);

    Mono<TransactionLineStandingOrder> findByStandingOrderId(String standingOrderId);

    Flux<TransactionLineStandingOrder> findByStandingOrderStatus(StandingOrderStatusEnum status, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_standing_order WHERE standing_order_status = :status")
    Mono<Long> countByStandingOrderStatus(StandingOrderStatusEnum status);

    Flux<TransactionLineStandingOrder> findByStandingOrderFrequency(StandingOrderFrequencyEnum frequency, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_standing_order WHERE standing_order_frequency = :frequency")
    Mono<Long> countByStandingOrderFrequency(StandingOrderFrequencyEnum frequency);

    @Query("SELECT * FROM transaction_line_standing_order " +
            "WHERE standing_order_status = 'ACTIVE' " +
            "AND (standing_order_suspended_until_date IS NULL OR standing_order_suspended_until_date <= :currentDate) " +
            "AND (standing_order_end_date IS NULL OR standing_order_end_date >= :currentDate) " +
            "ORDER BY :#{#pageable.sort} LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineStandingOrder> findActiveStandingOrders(LocalDate currentDate, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_standing_order " +
            "WHERE standing_order_status = 'ACTIVE' " +
            "AND (standing_order_suspended_until_date IS NULL OR standing_order_suspended_until_date <= :currentDate) " +
            "AND (standing_order_end_date IS NULL OR standing_order_end_date >= :currentDate)")
    Mono<Long> countActiveStandingOrders(LocalDate currentDate);

    @Query("SELECT tlso.* FROM transaction_line_standing_order tlso " +
            "JOIN transaction t ON t.transaction_id = tlso.transaction_id " +
            "WHERE (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount) " +
            "AND (:standingOrderId IS NULL OR tlso.standing_order_id = :standingOrderId) " +
            "AND (:recipientName IS NULL OR tlso.standing_order_recipient_name ILIKE concat('%', :recipientName, '%')) " +
            "AND (:recipientIban IS NULL OR tlso.standing_order_recipient_iban = :recipientIban) " +
            "AND (:recipientBic IS NULL OR tlso.standing_order_recipient_bic = :recipientBic) " +
            "AND (:status IS NULL OR tlso.standing_order_status = :status) " +
            "AND (:frequency IS NULL OR tlso.standing_order_frequency = :frequency) " +
            "AND (:startDateFrom IS NULL OR tlso.standing_order_start_date >= :startDateFrom) " +
            "AND (:startDateTo IS NULL OR tlso.standing_order_start_date <= :startDateTo) " +
            "AND (:endDateFrom IS NULL OR tlso.standing_order_end_date >= :endDateFrom) " +
            "AND (:endDateTo IS NULL OR tlso.standing_order_end_date <= :endDateTo) " +
            "AND (:nextExecutionDateFrom IS NULL OR tlso.standing_order_next_execution_date >= :nextExecutionDateFrom) " +
            "AND (:nextExecutionDateTo IS NULL OR tlso.standing_order_next_execution_date <= :nextExecutionDateTo) " +
            "AND (:isSuspended IS NULL OR " +
            "     CASE WHEN :isSuspended = true THEN tlso.standing_order_suspended_until_date > CURRENT_DATE " +
            "          ELSE (tlso.standing_order_suspended_until_date IS NULL OR tlso.standing_order_suspended_until_date <= CURRENT_DATE) " +
            "     END) " +
            "AND (:minTotalExecutions IS NULL OR tlso.standing_order_total_executions >= :minTotalExecutions) " +
            "AND (:maxTotalExecutions IS NULL OR tlso.standing_order_total_executions <= :maxTotalExecutions) " +
            "AND (:createdBy IS NULL OR tlso.standing_order_created_by = :createdBy) " +
            "AND (:updatedBy IS NULL OR tlso.standing_order_updated_by = :updatedBy)")
    Flux<TransactionLineStandingOrder> findByCustomCriteria(
            BigDecimal minAmount,
            BigDecimal maxAmount,
            String standingOrderId,
            String recipientName,
            String recipientIban,
            String recipientBic,
            StandingOrderStatusEnum status,
            StandingOrderFrequencyEnum frequency,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            LocalDate endDateFrom,
            LocalDate endDateTo,
            LocalDate nextExecutionDateFrom,
            LocalDate nextExecutionDateTo,
            Boolean isSuspended,
            Integer minTotalExecutions,
            Integer maxTotalExecutions,
            String createdBy,
            String updatedBy,
            Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_standing_order tlso " +
            "JOIN transaction t ON t.transaction_id = tlso.transaction_id " +
            "WHERE (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount) " +
            "AND (:standingOrderId IS NULL OR tlso.standing_order_id = :standingOrderId) " +
            "AND (:recipientName IS NULL OR tlso.standing_order_recipient_name ILIKE concat('%', :recipientName, '%')) " +
            "AND (:recipientIban IS NULL OR tlso.standing_order_recipient_iban = :recipientIban) " +
            "AND (:recipientBic IS NULL OR tlso.standing_order_recipient_bic = :recipientBic) " +
            "AND (:status IS NULL OR tlso.standing_order_status = :status) " +
            "AND (:frequency IS NULL OR tlso.standing_order_frequency = :frequency) " +
            "AND (:startDateFrom IS NULL OR tlso.standing_order_start_date >= :startDateFrom) " +
            "AND (:startDateTo IS NULL OR tlso.standing_order_start_date <= :startDateTo) " +
            "AND (:endDateFrom IS NULL OR tlso.standing_order_end_date >= :endDateFrom) " +
            "AND (:endDateTo IS NULL OR tlso.standing_order_end_date <= :endDateTo) " +
            "AND (:nextExecutionDateFrom IS NULL OR tlso.standing_order_next_execution_date >= :nextExecutionDateFrom) " +
            "AND (:nextExecutionDateTo IS NULL OR tlso.standing_order_next_execution_date <= :nextExecutionDateTo) " +
            "AND (:isSuspended IS NULL OR " +
            "     CASE WHEN :isSuspended = true THEN tlso.standing_order_suspended_until_date > CURRENT_DATE " +
            "          ELSE (tlso.standing_order_suspended_until_date IS NULL OR tlso.standing_order_suspended_until_date <= CURRENT_DATE) " +
            "     END) " +
            "AND (:minTotalExecutions IS NULL OR tlso.standing_order_total_executions >= :minTotalExecutions) " +
            "AND (:maxTotalExecutions IS NULL OR tlso.standing_order_total_executions <= :maxTotalExecutions) " +
            "AND (:createdBy IS NULL OR tlso.standing_order_created_by = :createdBy) " +
            "AND (:updatedBy IS NULL OR tlso.standing_order_updated_by = :updatedBy)")
    Mono<Long> countByCustomCriteria(
            BigDecimal minAmount,
            BigDecimal maxAmount,
            String standingOrderId,
            String recipientName,
            String recipientIban,
            String recipientBic,
            StandingOrderStatusEnum status,
            StandingOrderFrequencyEnum frequency,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            LocalDate endDateFrom,
            LocalDate endDateTo,
            LocalDate nextExecutionDateFrom,
            LocalDate nextExecutionDateTo,
            Boolean isSuspended,
            Integer minTotalExecutions,
            Integer maxTotalExecutions,
            String createdBy,
            String updatedBy);

    @Query("SELECT * FROM transaction_line_standing_order " +
            "WHERE standing_order_status = 'ACTIVE' " +
            "AND standing_order_next_execution_date = :executionDate " +
            "AND (standing_order_suspended_until_date IS NULL OR standing_order_suspended_until_date <= :executionDate) " +
            "ORDER BY :#{#pageable.sort} LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineStandingOrder> findStandingOrdersForExecution(LocalDate executionDate, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_standing_order " +
            "WHERE standing_order_status = 'ACTIVE' " +
            "AND standing_order_next_execution_date = :executionDate " +
            "AND (standing_order_suspended_until_date IS NULL OR standing_order_suspended_until_date <= :executionDate)")
    Mono<Long> countStandingOrdersForExecution(LocalDate executionDate);

    @Query("SELECT * FROM transaction_line_standing_order " +
            "WHERE standing_order_recipient_name ILIKE concat('%', :recipientName, '%') " +
            "ORDER BY :#{#pageable.sort} LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineStandingOrder> findByRecipientNameContaining(String recipientName, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_standing_order " +
            "WHERE standing_order_recipient_name ILIKE concat('%', :recipientName, '%')")
    Mono<Long> countByRecipientNameContaining(String recipientName);

    @Query("SELECT * FROM transaction_line_standing_order " +
            "WHERE standing_order_recipient_iban = :iban " +
            "AND standing_order_status = 'ACTIVE' " +
            "ORDER BY :#{#pageable.sort} LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineStandingOrder> findActiveStandingOrdersByRecipientIban(String iban, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_standing_order " +
            "WHERE standing_order_recipient_iban = :iban " +
            "AND standing_order_status = 'ACTIVE'")
    Mono<Long> countActiveStandingOrdersByRecipientIban(String iban);

    @Query("SELECT * FROM transaction_line_standing_order " +
            "WHERE standing_order_status = 'SUSPENDED' " +
            "AND standing_order_suspended_until_date < :date " +
            "ORDER BY :#{#pageable.sort} LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineStandingOrder> findExpiredSuspensions(LocalDate date, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction_line_standing_order " +
            "WHERE standing_order_status = 'SUSPENDED' " +
            "AND standing_order_suspended_until_date < :date")
    Mono<Long> countExpiredSuspensions(LocalDate date);
}