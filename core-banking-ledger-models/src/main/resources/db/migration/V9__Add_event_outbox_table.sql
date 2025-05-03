-- V9__Add_event_outbox_table.sql

-- =============================================
-- EVENT_OUTBOX (Event Sourcing / Outbox Pattern)
-- =============================================
CREATE TABLE IF NOT EXISTS event_outbox (
    event_id                UUID NOT NULL PRIMARY KEY,
    aggregate_type          VARCHAR(100) NOT NULL,
    aggregate_id            VARCHAR(100) NOT NULL,
    event_type              VARCHAR(100) NOT NULL,
    payload                 JSONB NOT NULL,
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed               BOOLEAN NOT NULL DEFAULT FALSE,
    processed_at            TIMESTAMP,
    retry_count             INT NOT NULL DEFAULT 0,
    last_error              TEXT
);

-- Add comments explaining the purpose of the table
COMMENT ON TABLE event_outbox IS 'Stores domain events for reliable event publishing using the outbox pattern';
COMMENT ON COLUMN event_outbox.aggregate_type IS 'Type of the aggregate (e.g., TRANSACTION, ACCOUNT)';
COMMENT ON COLUMN event_outbox.aggregate_id IS 'ID of the aggregate (e.g., transaction_id)';
COMMENT ON COLUMN event_outbox.event_type IS 'Type of the event (e.g., TRANSACTION_CREATED, TRANSACTION_STATUS_CHANGED)';
COMMENT ON COLUMN event_outbox.payload IS 'JSON payload of the event';
COMMENT ON COLUMN event_outbox.processed IS 'Whether the event has been processed';
COMMENT ON COLUMN event_outbox.processed_at IS 'When the event was processed';
COMMENT ON COLUMN event_outbox.retry_count IS 'Number of retry attempts';
COMMENT ON COLUMN event_outbox.last_error IS 'Last error message if processing failed';

-- Create indexes for common queries
CREATE INDEX idx_event_outbox_aggregate_type_id ON event_outbox(aggregate_type, aggregate_id);
CREATE INDEX idx_event_outbox_processed ON event_outbox(processed);
CREATE INDEX idx_event_outbox_created_at ON event_outbox(created_at);
