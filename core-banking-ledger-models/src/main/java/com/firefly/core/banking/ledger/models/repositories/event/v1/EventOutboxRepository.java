package com.firefly.core.banking.ledger.models.repositories.event.v1;

import com.firefly.core.banking.ledger.models.entities.event.v1.EventOutbox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository interface for event outbox.
 */
public interface EventOutboxRepository extends ReactiveCrudRepository<EventOutbox, UUID> {
    /**
     * Find all unprocessed events.
     */
    Flux<EventOutbox> findByProcessedFalseOrderByCreatedAt(Pageable pageable);
    
    /**
     * Count all unprocessed events.
     */
    Mono<Long> countByProcessedFalse();
    
    /**
     * Find all events for a specific aggregate.
     */
    Flux<EventOutbox> findByAggregateTypeAndAggregateId(String aggregateType, String aggregateId, Pageable pageable);
    
    /**
     * Count all events for a specific aggregate.
     */
    Mono<Long> countByAggregateTypeAndAggregateId(String aggregateType, String aggregateId);
    
    /**
     * Mark an event as processed.
     */
    @Modifying
    @Query("UPDATE event_outbox SET processed = true, processed_at = :processedAt WHERE event_id = :eventId")
    Mono<Integer> markAsProcessed(UUID eventId, LocalDateTime processedAt);
    
    /**
     * Increment retry count and set error message.
     */
    @Modifying
    @Query("UPDATE event_outbox SET retry_count = retry_count + 1, last_error = :errorMessage WHERE event_id = :eventId")
    Mono<Integer> incrementRetryCount(UUID eventId, String errorMessage);
}
