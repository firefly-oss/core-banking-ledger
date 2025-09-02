package com.firefly.core.banking.ledger.models.repositories.fee.v1;

import com.firefly.core.banking.ledger.models.entities.fee.v1.TransactionLineFee;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Repository interface for TransactionLineFee entity.
 */
public interface TransactionLineFeeRepository extends BaseRepository<TransactionLineFee, UUID> {
    /**
     * Find fee transaction line by transaction ID.
     *
     * @param transactionId The transaction ID
     * @return A Mono containing the fee transaction line if found
     */
    Mono<TransactionLineFee> findByTransactionId(UUID transactionId);

    /**
     * Find fee transaction lines by fee type.
     *
     * @param feeType The fee type
     * @param pageable Pagination information
     * @return A Flux of fee transaction lines
     */
    @Query("SELECT * FROM transaction_line_fee " +
            "WHERE fee_type = :feeType " +
            "ORDER BY fee_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineFee> findByFeeType(String feeType, Pageable pageable);

    /**
     * Count fee transaction lines by fee type.
     *
     * @param feeType The fee type
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_fee " +
            "WHERE fee_type = :feeType")
    Mono<Long> countByFeeType(String feeType);

    /**
     * Find fee transaction lines by related transaction ID.
     *
     * @param relatedTransactionId The related transaction ID
     * @param pageable Pagination information
     * @return A Flux of fee transaction lines
     */
    @Query("SELECT * FROM transaction_line_fee " +
            "WHERE fee_related_transaction_id = :relatedTransactionId " +
            "ORDER BY fee_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineFee> findByFeeRelatedTransactionId(Long relatedTransactionId, Pageable pageable);

    /**
     * Count fee transaction lines by related transaction ID.
     *
     * @param relatedTransactionId The related transaction ID
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_fee " +
            "WHERE fee_related_transaction_id = :relatedTransactionId")
    Mono<Long> countByFeeRelatedTransactionId(Long relatedTransactionId);

    /**
     * Find fee transaction lines by waived status.
     *
     * @param waived The waived status
     * @param pageable Pagination information
     * @return A Flux of fee transaction lines
     */
    @Query("SELECT * FROM transaction_line_fee " +
            "WHERE fee_waived = :waived " +
            "ORDER BY fee_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineFee> findByFeeWaived(Boolean waived, Pageable pageable);

    /**
     * Count fee transaction lines by waived status.
     *
     * @param waived The waived status
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_fee " +
            "WHERE fee_waived = :waived")
    Mono<Long> countByFeeWaived(Boolean waived);

    /**
     * Find fee transaction lines by custom criteria.
     *
     * @param startDate The start date for the search
     * @param endDate The end date for the search
     * @param feeType The fee type
     * @param waived The waived status
     * @param minAmount The minimum fee amount
     * @param maxAmount The maximum fee amount
     * @param pageable Pagination information
     * @return A Flux of fee transaction lines
     */
    @Query("SELECT tlf.* FROM transaction_line_fee tlf " +
            "JOIN transaction t ON t.transaction_id = tlf.transaction_id " +
            "WHERE (:startDate IS NULL OR tlf.fee_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tlf.fee_timestamp <= :endDate) " +
            "AND (:feeType IS NULL OR tlf.fee_type = :feeType) " +
            "AND (:waived IS NULL OR tlf.fee_waived = :waived) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount) " +
            "ORDER BY tlf.fee_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineFee> findByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String feeType,
            Boolean waived,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable);

    /**
     * Count fee transaction lines by custom criteria.
     *
     * @param startDate The start date for the search
     * @param endDate The end date for the search
     * @param feeType The fee type
     * @param waived The waived status
     * @param minAmount The minimum fee amount
     * @param maxAmount The maximum fee amount
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_fee tlf " +
            "JOIN transaction t ON t.transaction_id = tlf.transaction_id " +
            "WHERE (:startDate IS NULL OR tlf.fee_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tlf.fee_timestamp <= :endDate) " +
            "AND (:feeType IS NULL OR tlf.fee_type = :feeType) " +
            "AND (:waived IS NULL OR tlf.fee_waived = :waived) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount)")
    Mono<Long> countByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String feeType,
            Boolean waived,
            BigDecimal minAmount,
            BigDecimal maxAmount);
}
