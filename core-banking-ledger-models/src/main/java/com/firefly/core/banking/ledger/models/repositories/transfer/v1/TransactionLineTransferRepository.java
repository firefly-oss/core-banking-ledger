package com.firefly.core.banking.ledger.models.repositories.transfer.v1;

import com.firefly.core.banking.ledger.models.entities.transfer.v1.TransactionLineTransfer;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Repository interface for TransactionLineTransfer entity.
 */
public interface TransactionLineTransferRepository extends BaseRepository<TransactionLineTransfer, UUID> {
    /**
     * Find transfer transaction line by transaction ID.
     *
     * @param transactionId The transaction ID
     * @return A Mono containing the transfer transaction line if found
     */
    Mono<TransactionLineTransfer> findByTransactionId(UUID transactionId);

    /**
     * Find transfer transaction lines by source account ID.
     *
     * @param sourceAccountId The source account ID
     * @param pageable Pagination information
     * @return A Flux of transfer transaction lines
     */
    @Query("SELECT * FROM transaction_line_transfer " +
            "WHERE transfer_source_account_id = :sourceAccountId " +
            "ORDER BY transfer_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineTransfer> findByTransferSourceAccountId(Long sourceAccountId, Pageable pageable);

    /**
     * Count transfer transaction lines by source account ID.
     *
     * @param sourceAccountId The source account ID
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_transfer " +
            "WHERE transfer_source_account_id = :sourceAccountId")
    Mono<Long> countByTransferSourceAccountId(Long sourceAccountId);

    /**
     * Find transfer transaction lines by destination account ID.
     *
     * @param destinationAccountId The destination account ID
     * @param pageable Pagination information
     * @return A Flux of transfer transaction lines
     */
    @Query("SELECT * FROM transaction_line_transfer " +
            "WHERE transfer_destination_account_id = :destinationAccountId " +
            "ORDER BY transfer_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineTransfer> findByTransferDestinationAccountId(Long destinationAccountId, Pageable pageable);

    /**
     * Count transfer transaction lines by destination account ID.
     *
     * @param destinationAccountId The destination account ID
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_transfer " +
            "WHERE transfer_destination_account_id = :destinationAccountId")
    Mono<Long> countByTransferDestinationAccountId(Long destinationAccountId);

    /**
     * Find transfer transaction lines by custom criteria.
     *
     * @param startDate The start date for the search
     * @param endDate The end date for the search
     * @param sourceAccountId The source account ID
     * @param destinationAccountId The destination account ID
     * @param minAmount The minimum transfer amount
     * @param maxAmount The maximum transfer amount
     * @param pageable Pagination information
     * @return A Flux of transfer transaction lines
     */
    @Query("SELECT tlt.* FROM transaction_line_transfer tlt " +
            "JOIN transaction t ON t.transaction_id = tlt.transaction_id " +
            "WHERE (:startDate IS NULL OR tlt.transfer_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tlt.transfer_timestamp <= :endDate) " +
            "AND (:sourceAccountId IS NULL OR tlt.transfer_source_account_id = :sourceAccountId) " +
            "AND (:destinationAccountId IS NULL OR tlt.transfer_destination_account_id = :destinationAccountId) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount) " +
            "ORDER BY tlt.transfer_timestamp DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TransactionLineTransfer> findByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long sourceAccountId,
            Long destinationAccountId,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable);

    /**
     * Count transfer transaction lines by custom criteria.
     *
     * @param startDate The start date for the search
     * @param endDate The end date for the search
     * @param sourceAccountId The source account ID
     * @param destinationAccountId The destination account ID
     * @param minAmount The minimum transfer amount
     * @param maxAmount The maximum transfer amount
     * @return A Mono containing the count
     */
    @Query("SELECT COUNT(*) FROM transaction_line_transfer tlt " +
            "JOIN transaction t ON t.transaction_id = tlt.transaction_id " +
            "WHERE (:startDate IS NULL OR tlt.transfer_timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR tlt.transfer_timestamp <= :endDate) " +
            "AND (:sourceAccountId IS NULL OR tlt.transfer_source_account_id = :sourceAccountId) " +
            "AND (:destinationAccountId IS NULL OR tlt.transfer_destination_account_id = :destinationAccountId) " +
            "AND (:minAmount IS NULL OR t.total_amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.total_amount <= :maxAmount)")
    Mono<Long> countByCustomCriteria(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long sourceAccountId,
            Long destinationAccountId,
            BigDecimal minAmount,
            BigDecimal maxAmount);
}
