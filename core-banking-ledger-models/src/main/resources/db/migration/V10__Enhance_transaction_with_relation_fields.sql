-- V10__Enhance_transaction_with_relation_fields.sql

-- Add relation fields to the transaction table
ALTER TABLE transaction
ADD COLUMN related_transaction_id BIGINT,
ADD COLUMN relation_type VARCHAR(20) CHECK (relation_type IN ('REVERSAL', 'ADJUSTMENT', 'CHARGEBACK', 'CORRECTION')),
ADD COLUMN request_id VARCHAR(100),
ADD COLUMN batch_id VARCHAR(100),
ADD COLUMN booking_date TIMESTAMP,
ADD COLUMN row_version BIGINT NOT NULL DEFAULT 0,
ADD COLUMN aml_risk_score INT,
ADD COLUMN aml_screening_result VARCHAR(50),
ADD COLUMN aml_large_txn_flag BOOLEAN DEFAULT FALSE,
ADD COLUMN sca_method VARCHAR(50),
ADD COLUMN sca_result VARCHAR(50),
ADD COLUMN instant_flag BOOLEAN DEFAULT FALSE,
ADD COLUMN confirmation_of_payee_result VARCHAR(20) CHECK (confirmation_of_payee_result IN ('OK', 'MISMATCH', 'UNAVAILABLE'));

-- Add comments explaining the purpose of the columns
COMMENT ON COLUMN transaction.related_transaction_id IS 'Reference to a related transaction (e.g., original transaction for a reversal)';
COMMENT ON COLUMN transaction.relation_type IS 'Type of relation to the related transaction';
COMMENT ON COLUMN transaction.request_id IS 'UUID received from the channel for idempotency';
COMMENT ON COLUMN transaction.batch_id IS 'ID of the batch for bulk operations';
COMMENT ON COLUMN transaction.booking_date IS 'Date when the transaction hit the ledger balance';
COMMENT ON COLUMN transaction.row_version IS 'Version number for optimistic locking';
COMMENT ON COLUMN transaction.aml_risk_score IS 'Anti-Money Laundering risk score';
COMMENT ON COLUMN transaction.aml_screening_result IS 'Result of AML screening';
COMMENT ON COLUMN transaction.aml_large_txn_flag IS 'Flag indicating a large transaction that requires additional scrutiny';
COMMENT ON COLUMN transaction.sca_method IS 'Strong Customer Authentication method used';
COMMENT ON COLUMN transaction.sca_result IS 'Result of Strong Customer Authentication';
COMMENT ON COLUMN transaction.instant_flag IS 'Flag indicating an instant payment (e.g., SCT-Inst)';
COMMENT ON COLUMN transaction.confirmation_of_payee_result IS 'Result of confirmation of payee check';

-- Add foreign key constraint for related_transaction_id
ALTER TABLE transaction
ADD CONSTRAINT fk_transaction_related
FOREIGN KEY (related_transaction_id) REFERENCES transaction (transaction_id);

-- Create indexes for common queries
CREATE INDEX idx_transaction_related_id ON transaction(related_transaction_id);
CREATE INDEX idx_transaction_request_id ON transaction(request_id);
CREATE INDEX idx_transaction_batch_id ON transaction(batch_id);
CREATE INDEX idx_transaction_booking_date ON transaction(booking_date);
CREATE UNIQUE INDEX idx_transaction_external_reference ON transaction(external_reference) WHERE external_reference IS NOT NULL;
