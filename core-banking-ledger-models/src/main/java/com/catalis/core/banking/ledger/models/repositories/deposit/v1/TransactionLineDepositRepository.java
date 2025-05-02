package com.catalis.core.banking.ledger.models.repositories.deposit.v1;

import com.catalis.core.banking.ledger.models.entities.deposit.v1.TransactionLineDeposit;
import com.catalis.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Repository interface for TransactionLineDeposit entity.
 */
public interface TransactionLineDepositRepository extends BaseRepository<TransactionLineDeposit, Long> {
    /**
     * Find deposit transaction line by transaction ID.
     *
     * @param transactionId The transaction ID
     * @return A Mono containing the deposit transaction line if found
     */
    Mono<TransactionLineDeposit> findByTransactionId(Long transactionId);

    /**
     * Find deposit transaction lines by deposit method.
     *
     * @param depositMethod The deposit method
     * @param pageable Pagination information
     * @return A Flux of deposit transaction lines
     */
    @Query("SELECT * FROM transaction_line_deposit " +
            "WHERE deposit_method = :depositMethod " +
            "ORDER BY deposit_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineDeposit> findByDepositMethod(String depositMethod, Pageable pageable);

    /**
     * Count deposit transaction lines by deposit method.
     *
     * @param depositMethod The deposit method
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_deposit " +
            "WHERE deposit_method = :depositMethod")
    Mono<Long> countByDepositMethod(String depositMethod);

    /**
     * Find deposit transaction lines by branch ID.
     *
     * @param branchId The branch ID
     * @param pageable Pagination information
     * @return A Flux of deposit transaction lines
     */
    @Query("SELECT * FROM transaction_line_deposit " +
            "WHERE deposit_branch_id = :branchId " +
            "ORDER BY deposit_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineDeposit> findByDepositBranchId(String branchId, Pageable pageable);

    /**
     * Count deposit transaction lines by branch ID.
     *
     * @param branchId The branch ID
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_deposit " +
            "WHERE deposit_branch_id = :branchId")
    Mono<Long> countByDepositBranchId(String branchId);

    /**
     * Find deposit transaction lines by custom criteria.
     *
     * @param startDate The start date for the search
     * @param endDate The end date for the search
     * @param depositMethod The deposit method
     * @param minAmount The minimum deposit amount
     * @param maxAmount The maximum deposit amount
     * @param pageable Pagination information
     * @return A Flux of deposit transaction lines
     */
    @Query("SELECT tld.* FROM transaction_line_deposit tld " +
            "JOIN transaction t ON t.transaction_id = tld.transaction_id " +
            "WHERE (:startDate IS NULL OR tld.deposit_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tld.deposit_timestamp <= :endDate) " +
            "AND (:depositMethod IS NULL OR tld.deposit_method = :depositMethod) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount) " +
            "ORDER BY tld.deposit_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineDeposit> findByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String depositMethod,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable);

    /**
     * Count deposit transaction lines by custom criteria.
     *
     * @param startDate The start date for the search
     * @param endDate The end date for the search
     * @param depositMethod The deposit method
     * @param minAmount The minimum deposit amount
     * @param maxAmount The maximum deposit amount
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_deposit tld " +
            "JOIN transaction t ON t.transaction_id = tld.transaction_id " +
            "WHERE (:startDate IS NULL OR tld.deposit_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tld.deposit_timestamp <= :endDate) " +
            "AND (:depositMethod IS NULL OR tld.deposit_method = :depositMethod) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount)")
    Mono<Long> countByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String depositMethod,
            BigDecimal minAmount,
            BigDecimal maxAmount);
}
