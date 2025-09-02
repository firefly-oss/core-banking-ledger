package com.firefly.core.banking.ledger.models.repositories.core.v1;

import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
import com.firefly.core.banking.ledger.models.entities.core.v1.Transaction;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends BaseRepository<Transaction, UUID> {
    Flux<Transaction> findByAccountId(UUID accountId, Pageable pageable);
    Mono<Long> countByAccountId(UUID accountId);

    Flux<Transaction> findByAccountSpaceId(UUID accountSpaceId, Pageable pageable);
    Mono<Long> countByAccountSpaceId(UUID accountSpaceId);

    Flux<Transaction> findByTransactionCategoryId(UUID categoryId, Pageable pageable);
    Mono<Long> countByTransactionCategoryId(UUID categoryId);

    // Geotag query methods
    @Query("SELECT t.* FROM transaction t " +
           "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(t.latitude)) * " +
           "cos(radians(t.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
           "sin(radians(t.latitude)))) <= :radiusInKm " +
           "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(t.latitude)) * " +
           "cos(radians(t.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
           "sin(radians(t.latitude)))) ASC")
    Flux<Transaction> findTransactionsWithinRadius(Double latitude, Double longitude, Double radiusInKm, Pageable pageable);

    @Query("SELECT COUNT(*) FROM transaction t " +
           "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(t.latitude)) * " +
           "cos(radians(t.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
           "sin(radians(t.latitude)))) <= :radiusInKm")
    Mono<Long> countTransactionsWithinRadius(Double latitude, Double longitude, Double radiusInKm);
    Flux<Transaction> findByCountry(String country, Pageable pageable);
    Mono<Long> countByCountry(String country);

    Flux<Transaction> findByCity(String city, Pageable pageable);
    Mono<Long> countByCity(String city);

    Flux<Transaction> findByPostalCode(String postalCode, Pageable pageable);
    Mono<Long> countByPostalCode(String postalCode);

    Mono<Transaction> findByExternalReference(String externalReference);

    /**
     * Find a transaction by blockchain transaction hash.
     *
     * @param blockchainTransactionHash The blockchain transaction hash
     * @return A Mono emitting the transaction if found, or an empty Mono if not found
     */
    Mono<Transaction> findByBlockchainTransactionHash(String blockchainTransactionHash);

    /**
     * Find all transactions for an account.
     *
     * @param accountId The account ID
     * @return A Flux emitting all transactions for the account
     */
    Flux<Transaction> findByAccountId(UUID accountId);

    /**
     * Find all transactions for an account space.
     *
     * @param accountSpaceId The account space ID
     * @return A Flux emitting all transactions for the account space
     */
    Flux<Transaction> findByAccountSpaceId(UUID accountSpaceId);

    Flux<Transaction> findByLocationNameContainingIgnoreCase(String locationName, Pageable pageable);
    Mono<Long> countByLocationNameContainingIgnoreCase(String locationName);

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
            List<UUID> categoryIds,
            List<UUID> accountIds,
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
            List<UUID> categoryIds,
            List<UUID> accountIds,
            String referenceNumber,
            String description,
            String initiatingParty,
            Boolean includeReversed,
            Boolean onlyFailed,
            Boolean onlyPending);

}
