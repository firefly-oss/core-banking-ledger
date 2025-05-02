-- V6__Add_basic_transaction_line_tables.sql

-- =============================================
-- TRANSACTION_LINE_DEPOSIT (Subtype Detail)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_line_deposit (
    transaction_line_deposit_id BIGINT NOT NULL PRIMARY KEY,
    transaction_id              BIGINT NOT NULL,
    deposit_method              VARCHAR(50),
    deposit_reference           VARCHAR(100),
    deposit_location            VARCHAR(100),
    deposit_notes               VARCHAR(255),
    deposit_confirmation_code   VARCHAR(50),
    deposit_receipt_number      VARCHAR(50),
    deposit_atm_id              VARCHAR(50),
    deposit_branch_id           VARCHAR(50),
    deposit_cash_amount         DECIMAL(18,2),
    deposit_check_amount        DECIMAL(18,2),
    deposit_check_number        VARCHAR(50),
    deposit_check_date          DATE,
    deposit_check_bank          VARCHAR(100),
    deposit_timestamp           TIMESTAMP,
    deposit_processed_by        VARCHAR(100),
    date_created                TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated                TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Spanish-specific
    deposit_spanish_tax_code    VARCHAR(50),

    CONSTRAINT fk_line_deposit_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
);

-- =============================================
-- TRANSACTION_LINE_WITHDRAWAL (Subtype Detail)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_line_withdrawal (
    transaction_line_withdrawal_id BIGINT NOT NULL PRIMARY KEY,
    transaction_id                 BIGINT NOT NULL,
    withdrawal_method              VARCHAR(50),
    withdrawal_reference           VARCHAR(100),
    withdrawal_location            VARCHAR(100),
    withdrawal_notes               VARCHAR(255),
    withdrawal_confirmation_code   VARCHAR(50),
    withdrawal_receipt_number      VARCHAR(50),
    withdrawal_atm_id              VARCHAR(50),
    withdrawal_branch_id           VARCHAR(50),
    withdrawal_timestamp           TIMESTAMP,
    withdrawal_processed_by        VARCHAR(100),
    withdrawal_authorization_code  VARCHAR(50),
    withdrawal_daily_limit_check   BOOLEAN NOT NULL DEFAULT TRUE,
    withdrawal_daily_amount_used   DECIMAL(18,2),
    withdrawal_daily_limit         DECIMAL(18,2),
    date_created                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Spanish-specific
    withdrawal_spanish_tax_code    VARCHAR(50),

    CONSTRAINT fk_line_withdrawal_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
);

-- =============================================
-- TRANSACTION_LINE_TRANSFER (Subtype Detail)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_line_transfer (
    transaction_line_transfer_id   BIGINT NOT NULL PRIMARY KEY,
    transaction_id                 BIGINT NOT NULL,
    transfer_reference             VARCHAR(100),
    transfer_source_account_id     BIGINT,
    transfer_destination_account_id BIGINT,
    transfer_source_account_number VARCHAR(50),
    transfer_destination_account_number VARCHAR(50),
    transfer_source_account_name   VARCHAR(100),
    transfer_destination_account_name VARCHAR(100),
    transfer_purpose               VARCHAR(255),
    transfer_notes                 VARCHAR(255),
    transfer_timestamp             TIMESTAMP,
    transfer_processed_by          VARCHAR(100),
    transfer_fee_amount            DECIMAL(18,2),
    transfer_fee_currency          CHAR(3),
    transfer_scheduled_date        DATE,
    transfer_execution_date        DATE,
    date_created                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Spanish-specific
    transfer_spanish_tax_code      VARCHAR(50),

    CONSTRAINT fk_line_transfer_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
);

-- =============================================
-- TRANSACTION_LINE_FEE (Subtype Detail)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_line_fee (
    transaction_line_fee_id        BIGINT NOT NULL PRIMARY KEY,
    transaction_id                 BIGINT NOT NULL,
    fee_type                       VARCHAR(50),
    fee_description                VARCHAR(255),
    fee_reference                  VARCHAR(100),
    fee_related_transaction_id     BIGINT,
    fee_related_service            VARCHAR(100),
    fee_calculation_method         VARCHAR(50),
    fee_calculation_base           DECIMAL(18,2),
    fee_rate_percentage            DECIMAL(5,2),
    fee_fixed_amount               DECIMAL(18,2),
    fee_currency                   CHAR(3),
    fee_waived                     BOOLEAN NOT NULL DEFAULT FALSE,
    fee_waiver_reason              VARCHAR(255),
    fee_waiver_authorized_by       VARCHAR(100),
    fee_timestamp                  TIMESTAMP,
    fee_processed_by               VARCHAR(100),
    date_created                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Spanish-specific
    fee_spanish_tax_code           VARCHAR(50),

    CONSTRAINT fk_line_fee_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
);

-- =============================================
-- TRANSACTION_LINE_INTEREST (Subtype Detail)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_line_interest (
    transaction_line_interest_id   BIGINT NOT NULL PRIMARY KEY,
    transaction_id                 BIGINT NOT NULL,
    interest_type                  VARCHAR(50),
    interest_description           VARCHAR(255),
    interest_reference             VARCHAR(100),
    interest_related_account_id    BIGINT,
    interest_calculation_method    VARCHAR(50),
    interest_calculation_base      DECIMAL(18,2),
    interest_rate_percentage       DECIMAL(5,2),
    interest_accrual_start_date    DATE,
    interest_accrual_end_date      DATE,
    interest_days_calculated       INTEGER,
    interest_currency              CHAR(3),
    interest_tax_withheld_amount   DECIMAL(18,2),
    interest_tax_withheld_rate     DECIMAL(5,2),
    interest_gross_amount          DECIMAL(18,2),
    interest_net_amount            DECIMAL(18,2),
    interest_timestamp             TIMESTAMP,
    interest_processed_by          VARCHAR(100),
    date_created                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Spanish-specific
    interest_spanish_tax_code      VARCHAR(50),

    CONSTRAINT fk_line_interest_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
);

-- Create indexes for common queries
CREATE INDEX idx_transaction_line_deposit_transaction_id ON transaction_line_deposit(transaction_id);
CREATE INDEX idx_transaction_line_withdrawal_transaction_id ON transaction_line_withdrawal(transaction_id);
CREATE INDEX idx_transaction_line_transfer_transaction_id ON transaction_line_transfer(transaction_id);
CREATE INDEX idx_transaction_line_fee_transaction_id ON transaction_line_fee(transaction_id);
CREATE INDEX idx_transaction_line_interest_transaction_id ON transaction_line_interest(transaction_id);

-- Add comments explaining the purpose of the tables
COMMENT ON TABLE transaction_line_deposit IS 'Stores detailed information about deposit transactions';
COMMENT ON TABLE transaction_line_withdrawal IS 'Stores detailed information about withdrawal transactions';
COMMENT ON TABLE transaction_line_transfer IS 'Stores detailed information about internal transfer transactions';
COMMENT ON TABLE transaction_line_fee IS 'Stores detailed information about fee transactions';
COMMENT ON TABLE transaction_line_interest IS 'Stores detailed information about interest transactions';
