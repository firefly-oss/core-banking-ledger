package com.catalis.core.banking.ledger.core.services.event.v1;

import com.catalis.core.banking.ledger.models.entities.event.v1.EventOutbox;
import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for managing the event outbox.
 */
public interface EventOutboxService {
    /**
     * Publish an event to the outbox.
     *
     * @param aggregateType Type of the aggregate (e.g., TRANSACTION, ACCOUNT).
     * @param aggregateId ID of the aggregate (e.g., transaction_id).
     * @param eventType Type of the event (e.g., TRANSACTION_CREATED, TRANSACTION_STATUS_CHANGED).
     * @param payload Event payload object (will be serialized to JSON).
     * @return The created event.
     */
    Mono<EventOutbox> publishEvent(String aggregateType, String aggregateId, String eventType, Object payload);

    /**
     * Get unprocessed events.
     *
     * @param batchSize Maximum number of events to retrieve.
     * @return A flux of unprocessed events.
     */
    Flux<EventOutbox> getUnprocessedEvents(int batchSize);

    /**
     * Mark an event as processed.
     *
     * @param eventId ID of the event.
     * @return Number of rows affected (should be 1 if successful).
     */
    Mono<Integer> markAsProcessed(UUID eventId);

    /**
     * Record a processing error.
     *
     * @param eventId ID of the event.
     * @param errorMessage Error message.
     * @return Number of rows affected (should be 1 if successful).
     */
    Mono<Integer> recordError(UUID eventId, String errorMessage);
}
