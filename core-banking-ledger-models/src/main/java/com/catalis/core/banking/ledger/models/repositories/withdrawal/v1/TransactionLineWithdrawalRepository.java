package com.catalis.core.banking.ledger.models.repositories.withdrawal.v1;

import com.catalis.core.banking.ledger.models.entities.withdrawal.v1.TransactionLineWithdrawal;
import com.catalis.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Repository interface for TransactionLineWithdrawal entity.
 */
public interface TransactionLineWithdrawalRepository extends BaseRepository<TransactionLineWithdrawal, Long> {
    /**
     * Find withdrawal transaction line by transaction ID.
     *
     * @param transactionId The transaction ID
     * @return A Mono containing the withdrawal transaction line if found
     */
    Mono<TransactionLineWithdrawal> findByTransactionId(Long transactionId);

    /**
     * Find withdrawal transaction lines by withdrawal method.
     *
     * @param withdrawalMethod The withdrawal method
     * @param pageable Pagination information
     * @return A Flux of withdrawal transaction lines
     */
    @Query("SELECT * FROM transaction_line_withdrawal " +
            "WHERE withdrawal_method = :withdrawalMethod " +
            "ORDER BY withdrawal_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineWithdrawal> findByWithdrawalMethod(String withdrawalMethod, Pageable pageable);

    /**
     * Count withdrawal transaction lines by withdrawal method.
     *
     * @param withdrawalMethod The withdrawal method
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_withdrawal " +
            "WHERE withdrawal_method = :withdrawalMethod")
    Mono<Long> countByWithdrawalMethod(String withdrawalMethod);

    /**
     * Find withdrawal transaction lines by ATM ID.
     *
     * @param atmId The ATM ID
     * @param pageable Pagination information
     * @return A Flux of withdrawal transaction lines
     */
    @Query("SELECT * FROM transaction_line_withdrawal " +
            "WHERE withdrawal_atm_id = :atmId " +
            "ORDER BY withdrawal_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineWithdrawal> findByWithdrawalAtmId(String atmId, Pageable pageable);

    /**
     * Count withdrawal transaction lines by ATM ID.
     *
     * @param atmId The ATM ID
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_withdrawal " +
            "WHERE withdrawal_atm_id = :atmId")
    Mono<Long> countByWithdrawalAtmId(String atmId);

    /**
     * Find withdrawal transaction lines by custom criteria.
     *
     * @param startDate The start date for the search
     * @param endDate The end date for the search
     * @param withdrawalMethod The withdrawal method
     * @param minAmount The minimum withdrawal amount
     * @param maxAmount The maximum withdrawal amount
     * @param pageable Pagination information
     * @return A Flux of withdrawal transaction lines
     */
    @Query("SELECT tlw.* FROM transaction_line_withdrawal tlw " +
            "JOIN transaction t ON t.transaction_id = tlw.transaction_id " +
            "WHERE (:startDate IS NULL OR tlw.withdrawal_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tlw.withdrawal_timestamp <= :endDate) " +
            "AND (:withdrawalMethod IS NULL OR tlw.withdrawal_method = :withdrawalMethod) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount) " +
            "ORDER BY tlw.withdrawal_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineWithdrawal> findByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String withdrawalMethod,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable);

    /**
     * Count withdrawal transaction lines by custom criteria.
     *
     * @param startDate The start date for the search
     * @param endDate The end date for the search
     * @param withdrawalMethod The withdrawal method
     * @param minAmount The minimum withdrawal amount
     * @param maxAmount The maximum withdrawal amount
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_withdrawal tlw " +
            "JOIN transaction t ON t.transaction_id = tlw.transaction_id " +
            "WHERE (:startDate IS NULL OR tlw.withdrawal_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tlw.withdrawal_timestamp <= :endDate) " +
            "AND (:withdrawalMethod IS NULL OR tlw.withdrawal_method = :withdrawalMethod) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount)")
    Mono<Long> countByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String withdrawalMethod,
            BigDecimal minAmount,
            BigDecimal maxAmount);
}
