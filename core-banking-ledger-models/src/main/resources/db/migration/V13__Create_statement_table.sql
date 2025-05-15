-- V10__Create_statement_table.sql

-- Create statement table
CREATE TABLE IF NOT EXISTS statement (
    statement_id BIGSERIAL PRIMARY KEY,
    account_id BIGINT,
    account_space_id BIGINT,
    period_type VARCHAR(20) NOT NULL,

    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    generation_date TIMESTAMP NOT NULL,
    transaction_count INTEGER NOT NULL,

    included_pending BOOLEAN NOT NULL DEFAULT FALSE,
    included_details BOOLEAN NOT NULL DEFAULT TRUE,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    CONSTRAINT fk_statement_account
        FOREIGN KEY (account_id)
        REFERENCES account (account_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_statement_account_space
        FOREIGN KEY (account_space_id)
        REFERENCES account_space (account_space_id)
        ON DELETE CASCADE,

    CONSTRAINT check_statement_target
        CHECK ((account_id IS NOT NULL AND account_space_id IS NULL) OR
               (account_id IS NULL AND account_space_id IS NOT NULL))
);

-- Add indexes for efficient querying
CREATE INDEX idx_statement_account_id ON statement(account_id);
CREATE INDEX idx_statement_account_space_id ON statement(account_space_id);
CREATE INDEX idx_statement_date_range ON statement(start_date, end_date);
CREATE INDEX idx_statement_generation_date ON statement(generation_date);

-- Add comments
COMMENT ON TABLE statement IS 'Stores metadata about generated account and account space statements';
COMMENT ON COLUMN statement.statement_id IS 'Unique identifier for the statement';
COMMENT ON COLUMN statement.account_id IS 'Reference to the account associated with this statement (null if for account space)';
COMMENT ON COLUMN statement.account_space_id IS 'Reference to the account space associated with this statement (null if for account)';
COMMENT ON COLUMN statement.period_type IS 'Type of period for the statement (MONTHLY, QUARTERLY, YEARLY, CUSTOM)';
COMMENT ON COLUMN statement.start_date IS 'Start date of the statement period';
COMMENT ON COLUMN statement.end_date IS 'End date of the statement period';
COMMENT ON COLUMN statement.generation_date IS 'Date when the statement was generated';
COMMENT ON COLUMN statement.transaction_count IS 'Number of transactions included in the statement';
COMMENT ON COLUMN statement.included_pending IS 'Whether pending transactions were included';
COMMENT ON COLUMN statement.included_details IS 'Whether transaction details were included';
