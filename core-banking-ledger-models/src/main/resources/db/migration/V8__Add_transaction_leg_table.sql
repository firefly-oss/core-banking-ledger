-- V8__Add_transaction_leg_table.sql

-- =============================================
-- TRANSACTION_LEG (Double-Entry Accounting)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_leg (
    transaction_leg_id      BIGINT NOT NULL PRIMARY KEY,
    transaction_id          BIGINT NOT NULL,
    account_id              BIGINT NOT NULL,
    account_space_id        BIGINT,
    leg_type                VARCHAR(10) NOT NULL CHECK (leg_type IN ('DEBIT', 'CREDIT')),
    amount                  DECIMAL(19,4) NOT NULL,
    currency                CHAR(3) NOT NULL,
    description             VARCHAR(255),
    value_date              TIMESTAMP,
    booking_date            TIMESTAMP,
    date_created            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_leg_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
);

-- Add comments explaining the purpose of the table
COMMENT ON TABLE transaction_leg IS 'Stores individual debit and credit legs for each transaction, enabling double-entry accounting';
COMMENT ON COLUMN transaction_leg.leg_type IS 'Type of leg: DEBIT or CREDIT';
COMMENT ON COLUMN transaction_leg.amount IS 'Amount for this leg (always positive)';
COMMENT ON COLUMN transaction_leg.booking_date IS 'Date when the leg hit the ledger balance';

-- Create indexes for common queries
CREATE INDEX idx_transaction_leg_transaction_id ON transaction_leg(transaction_id);
CREATE INDEX idx_transaction_leg_account_id ON transaction_leg(account_id);
CREATE INDEX idx_transaction_leg_account_space_id ON transaction_leg(account_space_id);
CREATE INDEX idx_transaction_leg_booking_date ON transaction_leg(booking_date);
