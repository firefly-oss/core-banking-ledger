-- V12__Add_transaction_attachment_table.sql

-- =============================================
-- TRANSACTION_ATTACHMENT (Attachments)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_attachment (
    transaction_attachment_id BIGINT NOT NULL PRIMARY KEY,
    transaction_id            BIGINT NOT NULL,
    attachment_type           VARCHAR(50) NOT NULL,
    attachment_name           VARCHAR(255) NOT NULL,
    attachment_description    VARCHAR(255),
    object_storage_url        VARCHAR(1000) NOT NULL,
    content_type              VARCHAR(100),
    size_bytes                BIGINT,
    hash_sha256               VARCHAR(64),
    uploaded_by               VARCHAR(100),
    upload_date               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_created              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_attachment_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
);

-- Add comments explaining the purpose of the table
COMMENT ON TABLE transaction_attachment IS 'Stores attachments related to transactions, such as invoices, receipts, etc.';
COMMENT ON COLUMN transaction_attachment.attachment_type IS 'Type of attachment (e.g., INVOICE, RECEIPT, CONTRACT)';
COMMENT ON COLUMN transaction_attachment.object_storage_url IS 'URL to the attachment in object storage';
COMMENT ON COLUMN transaction_attachment.hash_sha256 IS 'SHA-256 hash of the attachment for integrity verification';

-- Create indexes for common queries
CREATE INDEX idx_transaction_attachment_transaction_id ON transaction_attachment(transaction_id);
CREATE INDEX idx_transaction_attachment_type ON transaction_attachment(attachment_type);
