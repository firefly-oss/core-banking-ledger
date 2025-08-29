package com.firefly.core.banking.ledger.models.entities.event.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing an event in the outbox table.
 * Used for reliable event publishing using the outbox pattern.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("event_outbox")
public class EventOutbox {
    @Id
    @Column("event_id")
    private UUID eventId;

    @Column("aggregate_type")
    private String aggregateType;

    @Column("aggregate_id")
    private String aggregateId;

    @Column("event_type")
    private String eventType;

    @Column("payload")
    private String payload;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("processed")
    private Boolean processed;

    @Column("processed_at")
    private LocalDateTime processedAt;

    @Column("retry_count")
    private Integer retryCount;

    @Column("last_error")
    private String lastError;
}