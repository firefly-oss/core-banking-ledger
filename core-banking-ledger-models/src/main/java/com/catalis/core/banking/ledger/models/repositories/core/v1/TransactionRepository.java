package com.catalis.core.banking.ledger.models.repositories.core.v1;

import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
import com.catalis.core.banking.ledger.models.entities.core.v1.Transaction;
import com.catalis.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends BaseRepository<Transaction, Long> {
    Flux<Transaction> findByAccountId(Long accountId, Pageable pageable);
    Mono<Long> countByAccountId(Long accountId);

    Flux<Transaction> findByTransactionCategoryId(Long categoryId, Pageable pageable);
    Mono<Long> countByTransactionCategoryId(Long categoryId);

    @Query("SELECT t.* FROM transaction t " +
            "WHERE (:startDate IS NULL OR t.transaction_date >= :startDate) " +
            "AND (:endDate IS NULL OR t.transaction_date <= :endDate) " +
            "AND (:currency IS NULL OR t.currency = :currency)")
    Flux<Transaction> findByDateRangeAndCurrency(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String currency,
            Pageable pageable);

    @Query("SELECT COUNT(t.*) FROM transaction t " +
            "WHERE (:startDate IS NULL OR t.transaction_date >= :startDate) " +
            "AND (:endDate IS NULL OR t.transaction_date <= :endDate) " +
            "AND (:currency IS NULL OR t.currency = :currency)")
    Mono<Long> countByDateRangeAndCurrency(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String currency);

    @Query("SELECT t.* FROM transaction t " +
            "WHERE (:startDate IS NULL OR t.transaction_date >= :startDate) " +
            "AND (:endDate IS NULL OR t.transaction_date <= :endDate) " +
            "AND (:minAmount IS NULL OR t.amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.amount <= :maxAmount) " +
            "AND (:currencies IS NULL OR t.currency IN (:currencies)) " +
            "AND (:types IS NULL OR t.transaction_type IN (:types)) " +
            "AND (:statuses IS NULL OR t.transaction_status IN (:statuses)) " +
            "AND (:categoryIds IS NULL OR t.transaction_category_id IN (:categoryIds)) " +
            "AND (:accountIds IS NULL OR t.account_id IN (:accountIds)) " +
            "AND (:referenceNumber IS NULL OR t.reference_number LIKE CONCAT('%', :referenceNumber, '%')) " +
            "AND (:description IS NULL OR t.description LIKE CONCAT('%', :description, '%')) " +
            "AND (:initiatingParty IS NULL OR t.initiating_party LIKE CONCAT('%', :initiatingParty, '%')) " +
            "AND (:includeReversed IS NULL OR t.is_reversed = :includeReversed) " +
            "AND (:onlyFailed IS NULL OR (:onlyFailed = TRUE AND t.transaction_status = 'FAILED')) " +
            "AND (:onlyPending IS NULL OR (:onlyPending = TRUE AND t.transaction_status = 'PENDING')) " +
            "ORDER BY CASE " +
            "    WHEN :sortDirection = 'ASC' THEN CASE WHEN :sortBy = 'transaction_date' THEN t.transaction_date ELSE NULL END " +
            "    ELSE NULL " + // Handle dynamic sorting if needed, fall back to default sorts
            "END ASC " +
            "LIMIT :pageSize OFFSET :pageOffset")
    Flux<Transaction> findTransactionsByFilter(
            LocalDateTime startDate,
            LocalDateTime endDate,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            List<String> currencies,
            List<TransactionTypeEnum> types,
            List<TransactionStatusEnum> statuses,
            List<Long> categoryIds,
            List<Long> accountIds,
            String referenceNumber,
            String description,
            String initiatingParty,
            Boolean includeReversed,
            Boolean onlyFailed,
            Boolean onlyPending,
            Integer pageSize,
            Integer pageOffset,
            String sortBy,
            String sortDirection);

    @Query("SELECT COUNT(*) FROM transaction t " +
            "WHERE (:startDate IS NULL OR t.transaction_date >= :startDate) " +
            "AND (:endDate IS NULL OR t.transaction_date <= :endDate) " +
            "AND (:minAmount IS NULL OR t.amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.amount <= :maxAmount) " +
            "AND (:currencies IS NULL OR t.currency IN (:currencies)) " +
            "AND (:types IS NULL OR t.transaction_type IN (:types)) " +
            "AND (:statuses IS NULL OR t.transaction_status IN (:statuses)) " +
            "AND (:categoryIds IS NULL OR t.transaction_category_id IN (:categoryIds)) " +
            "AND (:accountIds IS NULL OR t.account_id IN (:accountIds)) " +
            "AND (:referenceNumber IS NULL OR t.reference_number LIKE CONCAT('%', :referenceNumber, '%')) " +
            "AND (:description IS NULL OR t.description LIKE CONCAT('%', :description, '%')) " +
            "AND (:initiatingParty IS NULL OR t.initiating_party LIKE CONCAT('%', :initiatingParty, '%')) " +
            "AND (:includeReversed IS NULL OR t.is_reversed = :includeReversed) " +
            "AND (:onlyFailed IS NULL OR (:onlyFailed = TRUE AND t.transaction_status = 'FAILED')) " +
            "AND (:onlyPending IS NULL OR (:onlyPending = TRUE AND t.transaction_status = 'PENDING'))")
    Mono<Long> countTransactionsByFilter(
            LocalDateTime startDate,
            LocalDateTime endDate,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            List<String> currencies,
            List<TransactionTypeEnum> types,
            List<TransactionStatusEnum> statuses,
            List<Long> categoryIds,
            List<Long> accountIds,
            String referenceNumber,
            String description,
            String initiatingParty,
            Boolean includeReversed,
            Boolean onlyFailed,
            Boolean onlyPending);

}