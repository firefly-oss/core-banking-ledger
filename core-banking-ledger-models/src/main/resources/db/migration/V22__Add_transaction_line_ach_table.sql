-- V22__Add_transaction_line_ach_table.sql

-- =============================================
-- TRANSACTION_LINE_ACH (Subtype Detail)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_line_ach (
    transaction_line_ach_id       BIGINT NOT NULL PRIMARY KEY,
    transaction_id                BIGINT NOT NULL,
    ach_reference                 VARCHAR(100),
    ach_source_account_id         BIGINT,
    ach_destination_account_id    BIGINT,
    ach_source_account_number     VARCHAR(50),
    ach_destination_account_number VARCHAR(50),
    ach_source_account_name       VARCHAR(100),
    ach_destination_account_name  VARCHAR(100),
    ach_routing_number            VARCHAR(50),
    ach_transaction_code          VARCHAR(50),
    ach_purpose                   VARCHAR(255),
    ach_notes                     VARCHAR(255),
    ach_timestamp                 TIMESTAMP,
    ach_processed_by              VARCHAR(100),
    ach_fee_amount                DECIMAL(18,2),
    ach_fee_currency              CHAR(3),
    ach_scheduled_date            DATE,
    ach_execution_date            DATE,
    ach_batch_number              VARCHAR(50),
    ach_trace_number              VARCHAR(50),
    ach_entry_class_code          VARCHAR(50),
    ach_settlement_date           DATE,
    ach_return_code               VARCHAR(50),
    ach_return_reason             VARCHAR(255),
    date_created                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_line_ach_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
);

-- Create index for common queries
CREATE INDEX idx_transaction_line_ach_transaction_id ON transaction_line_ach(transaction_id);
CREATE INDEX idx_transaction_line_ach_routing_number ON transaction_line_ach(ach_routing_number);
CREATE INDEX idx_transaction_line_ach_batch_number ON transaction_line_ach(ach_batch_number);

-- Add comment explaining the purpose of the table
COMMENT ON TABLE transaction_line_ach IS 'Stores detailed information about ACH (Automated Clearing House) transactions';