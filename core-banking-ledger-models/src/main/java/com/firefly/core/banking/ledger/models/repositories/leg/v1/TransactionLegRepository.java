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


package com.firefly.core.banking.ledger.models.repositories.leg.v1;

import com.firefly.core.banking.ledger.models.entities.leg.v1.TransactionLeg;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;
import java.time.LocalDateTime;

/**
 * Repository interface for transaction legs.
 */
public interface TransactionLegRepository extends BaseRepository<TransactionLeg, UUID> {
    /**
     * Find all legs for a specific transaction.
     */
    Flux<TransactionLeg> findByTransactionId(UUID transactionId, Pageable pageable);
    
    /**
     * Count all legs for a specific transaction.
     */
    Mono<Long> countByTransactionId(UUID transactionId);
    
    /**
     * Find all legs for a specific account.
     */
    Flux<TransactionLeg> findByAccountId(UUID accountId, Pageable pageable);
    
    /**
     * Count all legs for a specific account.
     */
    Mono<Long> countByAccountId(UUID accountId);
    
    /**
     * Find all legs for a specific account space.
     */
    Flux<TransactionLeg> findByAccountSpaceId(UUID accountSpaceId, Pageable pageable);
    
    /**
     * Count all legs for a specific account space.
     */
    Mono<Long> countByAccountSpaceId(UUID accountSpaceId);
    
    /**
     * Find all legs for a specific account within a date range.
     */
    @Query("SELECT * FROM transaction_leg WHERE account_id = :accountId AND booking_date BETWEEN :startDate AND :endDate ORDER BY booking_date DESC")
    Flux<TransactionLeg> findByAccountIdAndBookingDateBetween(UUID accountId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Count all legs for a specific account within a date range.
     */
    @Query("SELECT COUNT(*) FROM transaction_leg WHERE account_id = :accountId AND booking_date BETWEEN :startDate AND :endDate")
    Mono<Long> countByAccountIdAndBookingDateBetween(UUID accountId, LocalDateTime startDate, LocalDateTime endDate);
}
