-- V11__Create_money_type.sql

-- Create a composite type for money
CREATE TYPE money_type AS (
    amount DECIMAL(19,4),
    currency CHAR(3)
);

-- Add comment explaining the purpose of the type
COMMENT ON TYPE money_type IS 'Composite type for representing money with amount and currency';

-- Create a table for storing money values
CREATE TABLE IF NOT EXISTS money (
    money_id BIGINT NOT NULL PRIMARY KEY,
    amount DECIMAL(19,4) NOT NULL,
    currency CHAR(3) NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add comment explaining the purpose of the table
COMMENT ON TABLE money IS 'Table for storing money values with amount and currency';

-- Create an index for common queries
CREATE INDEX idx_money_currency ON money(currency);
