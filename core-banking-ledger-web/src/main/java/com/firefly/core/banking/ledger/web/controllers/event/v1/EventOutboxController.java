package com.firefly.core.banking.ledger.web.controllers.event.v1;

import com.firefly.core.banking.ledger.core.services.event.v1.EventOutboxService;
import com.firefly.core.banking.ledger.models.entities.event.v1.EventOutbox;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

/**
 * REST controller for managing the event outbox.
 * Provides endpoints for publishing events, retrieving unprocessed events,
 * marking events as processed, and recording processing errors.
 */
@RestController
@RequestMapping("/api/v1/events/outbox")
@Tag(name = "Event Outbox", description = "Event outbox management API")
public class EventOutboxController {

    private final EventOutboxService eventOutboxService;

    @Autowired
    public EventOutboxController(EventOutboxService eventOutboxService) {
        this.eventOutboxService = eventOutboxService;
    }

    /**
     * Publishes an event to the outbox.
     *
     * @param eventRequest The event data
     * @return The created event
     */
    @PostMapping
    @Operation(summary = "Publish event", description = "Publishes an event to the outbox")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event published successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EventOutbox.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<EventOutbox>> publishEvent(
            @Parameter(description = "Event data", required = true) @RequestBody EventRequest eventRequest) {
        
        return eventOutboxService.publishEvent(
                eventRequest.getAggregateType(),
                eventRequest.getAggregateId(),
                eventRequest.getEventType(),
                eventRequest.getPayload()
            )
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Gets unprocessed events.
     *
     * @param batchSize Maximum number of events to retrieve
     * @return A flux of unprocessed events
     */
    @GetMapping("/unprocessed")
    @Operation(summary = "Get unprocessed events", description = "Gets unprocessed events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EventOutbox.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<EventOutbox> getUnprocessedEvents(
            @Parameter(description = "Maximum number of events to retrieve") @RequestParam(defaultValue = "100") int batchSize) {
        
        return eventOutboxService.getUnprocessedEvents(batchSize);
    }

    /**
     * Marks an event as processed.
     *
     * @param eventId ID of the event
     * @return Number of rows affected (should be 1 if successful)
     */
    @PutMapping("/{eventId}/processed")
    @Operation(summary = "Mark as processed", description = "Marks an event as processed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event marked as processed successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<Integer>> markAsProcessed(
            @Parameter(description = "ID of the event", required = true) @PathVariable UUID eventId) {
        
        return eventOutboxService.markAsProcessed(eventId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Records a processing error.
     *
     * @param eventId ID of the event
     * @param errorRequest Error message
     * @return Number of rows affected (should be 1 if successful)
     */
    @PutMapping("/{eventId}/error")
    @Operation(summary = "Record error", description = "Records a processing error")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Error recorded successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<Integer>> recordError(
            @Parameter(description = "ID of the event", required = true) @PathVariable UUID eventId,
            @Parameter(description = "Error message", required = true) @RequestBody ErrorRequest errorRequest) {
        
        return eventOutboxService.recordError(eventId, errorRequest.getErrorMessage())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Request class for publishing events.
     */
    public static class EventRequest {
        private String aggregateType;
        private String aggregateId;
        private String eventType;
        private Object payload;

        public String getAggregateType() {
            return aggregateType;
        }

        public void setAggregateType(String aggregateType) {
            this.aggregateType = aggregateType;
        }

        public String getAggregateId() {
            return aggregateId;
        }

        public void setAggregateId(String aggregateId) {
            this.aggregateId = aggregateId;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public Object getPayload() {
            return payload;
        }

        public void setPayload(Object payload) {
            this.payload = payload;
        }
    }

    /**
     * Request class for recording errors.
     */
    public static class ErrorRequest {
        private String errorMessage;

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}