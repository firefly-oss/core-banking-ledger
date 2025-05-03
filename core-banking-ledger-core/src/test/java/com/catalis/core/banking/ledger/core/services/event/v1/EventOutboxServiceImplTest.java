package com.catalis.core.banking.ledger.core.services.event.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.models.entities.event.v1.EventOutbox;
import com.catalis.core.banking.ledger.models.repositories.event.v1.EventOutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventOutboxServiceImplTest {

    @Mock
    private EventOutboxRepository repository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EventOutboxServiceImpl service;

    private TransactionDTO transactionDTO;
    private EventOutbox eventOutbox;
    private final String aggregateType = "TRANSACTION";
    private final String aggregateId = "1";
    private final String eventType = "TRANSACTION_CREATED";
    private final UUID eventId = UUID.randomUUID();
    private final String payloadJson = "{\"transactionId\":1,\"totalAmount\":100.00,\"currency\":\"EUR\"}";

    @BeforeEach
    void setUp() {
        // Initialize test data
        transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(1L);
        transactionDTO.setTotalAmount(new BigDecimal("100.00"));
        transactionDTO.setCurrency("EUR");

        eventOutbox = new EventOutbox();
        eventOutbox.setEventId(eventId);
        eventOutbox.setAggregateType(aggregateType);
        eventOutbox.setAggregateId(aggregateId);
        eventOutbox.setEventType(eventType);
        eventOutbox.setPayload(payloadJson);
        eventOutbox.setCreatedAt(LocalDateTime.now());
        eventOutbox.setProcessed(false);
        eventOutbox.setRetryCount(0);
    }

    @Test
    void publishEvent_Success() throws JsonProcessingException {
        // Arrange
        when(objectMapper.writeValueAsString(any())).thenReturn(payloadJson);
        when(repository.save(any(EventOutbox.class))).thenReturn(Mono.just(eventOutbox));

        // Act & Assert
        StepVerifier.create(service.publishEvent(aggregateType, aggregateId, eventType, transactionDTO))
                .expectNext(eventOutbox)
                .verifyComplete();

        verify(objectMapper).writeValueAsString(transactionDTO);
        verify(repository).save(any(EventOutbox.class));
    }

    @Test
    void publishEvent_SerializationError() throws JsonProcessingException {
        // Arrange
        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Test error") {});

        // Act & Assert
        StepVerifier.create(service.publishEvent(aggregateType, aggregateId, eventType, transactionDTO))
                .expectError(RuntimeException.class)
                .verify();

        verify(objectMapper).writeValueAsString(transactionDTO);
        verify(repository, never()).save(any(EventOutbox.class));
    }

    @Test
    void getUnprocessedEvents_Success() {
        // Arrange
        int batchSize = 10;
        when(repository.findByProcessedFalseOrderByCreatedAt(any(Pageable.class)))
                .thenReturn(Flux.just(eventOutbox));

        // Act & Assert
        StepVerifier.create(service.getUnprocessedEvents(batchSize))
                .expectNext(eventOutbox)
                .verifyComplete();

        verify(repository).findByProcessedFalseOrderByCreatedAt(PageRequest.of(0, batchSize));
    }

    @Test
    void markAsProcessed_Success() {
        // Arrange
        when(repository.markAsProcessed(eq(eventId), any(LocalDateTime.class)))
                .thenReturn(Mono.just(1));

        // Act & Assert
        StepVerifier.create(service.markAsProcessed(eventId))
                .expectNext(1)
                .verifyComplete();

        verify(repository).markAsProcessed(eq(eventId), any(LocalDateTime.class));
    }

    @Test
    void recordError_Success() {
        // Arrange
        String errorMessage = "Test error";
        when(repository.incrementRetryCount(eventId, errorMessage))
                .thenReturn(Mono.just(1));

        // Act & Assert
        StepVerifier.create(service.recordError(eventId, errorMessage))
                .expectNext(1)
                .verifyComplete();

        verify(repository).incrementRetryCount(eventId, errorMessage);
    }
}
