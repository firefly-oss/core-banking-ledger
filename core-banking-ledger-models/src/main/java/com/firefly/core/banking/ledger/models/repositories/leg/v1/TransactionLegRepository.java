package com.firefly.core.banking.ledger.models.repositories.leg.v1;

import com.firefly.core.banking.ledger.models.entities.leg.v1.TransactionLeg;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Repository interface for transaction legs.
 */
public interface TransactionLegRepository extends BaseRepository<TransactionLeg, Long> {
    /**
     * Find all legs for a specific transaction.
     */
    Flux<TransactionLeg> findByTransactionId(Long transactionId, Pageable pageable);
    
    /**
     * Count all legs for a specific transaction.
     */
    Mono<Long> countByTransactionId(Long transactionId);
    
    /**
     * Find all legs for a specific account.
     */
    Flux<TransactionLeg> findByAccountId(Long accountId, Pageable pageable);
    
    /**
     * Count all legs for a specific account.
     */
    Mono<Long> countByAccountId(Long accountId);
    
    /**
     * Find all legs for a specific account space.
     */
    Flux<TransactionLeg> findByAccountSpaceId(Long accountSpaceId, Pageable pageable);
    
    /**
     * Count all legs for a specific account space.
     */
    Mono<Long> countByAccountSpaceId(Long accountSpaceId);
    
    /**
     * Find all legs for a specific account within a date range.
     */
    @Query("SELECT * FROM transaction_leg WHERE account_id = :accountId AND booking_date BETWEEN :startDate AND :endDate ORDER BY booking_date DESC")
    Flux<TransactionLeg> findByAccountIdAndBookingDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Count all legs for a specific account within a date range.
     */
    @Query("SELECT COUNT(*) FROM transaction_leg WHERE account_id = :accountId AND booking_date BETWEEN :startDate AND :endDate")
    Mono<Long> countByAccountIdAndBookingDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
}
