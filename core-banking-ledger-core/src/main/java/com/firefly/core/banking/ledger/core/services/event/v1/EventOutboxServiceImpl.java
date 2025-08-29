package com.firefly.core.banking.ledger.core.services.event.v1;

import com.firefly.core.banking.ledger.models.entities.event.v1.EventOutbox;
import com.firefly.core.banking.ledger.models.repositories.event.v1.EventOutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of the EventOutboxService interface.
 */
@Service
@Transactional
public class EventOutboxServiceImpl implements EventOutboxService {

    @Autowired
    private EventOutboxRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Mono<EventOutbox> publishEvent(String aggregateType, String aggregateId, String eventType, Object payload) {
        try {
            String payloadJson = objectMapper.writeValueAsString(payload);
            
            EventOutbox event = new EventOutbox();
            event.setEventId(UUID.randomUUID());
            event.setAggregateType(aggregateType);
            event.setAggregateId(aggregateId);
            event.setEventType(eventType);
            event.setPayload(payloadJson);
            event.setCreatedAt(LocalDateTime.now());
            event.setProcessed(false);
            event.setRetryCount(0);
            
            return repository.save(event);
        } catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException("Failed to serialize event payload", e));
        }
    }

    @Override
    public Flux<EventOutbox> getUnprocessedEvents(int batchSize) {
        return repository.findByProcessedFalseOrderByCreatedAt(PageRequest.of(0, batchSize));
    }

    @Override
    public Mono<Integer> markAsProcessed(UUID eventId) {
        return repository.markAsProcessed(eventId, LocalDateTime.now());
    }

    @Override
    public Mono<Integer> recordError(UUID eventId, String errorMessage) {
        return repository.incrementRetryCount(eventId, errorMessage);
    }
}
