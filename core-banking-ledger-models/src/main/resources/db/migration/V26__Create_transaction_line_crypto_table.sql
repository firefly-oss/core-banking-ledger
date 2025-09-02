-- V26__Create_transaction_line_crypto_table.sql
-- Create transaction_line_crypto table to store crypto-specific transaction details

CREATE TABLE IF NOT EXISTS transaction_line_crypto (
    transaction_line_crypto_id BIGINT NOT NULL PRIMARY KEY,
    transaction_id BIGINT NOT NULL,
    crypto_asset_id BIGINT NOT NULL,
    blockchain_transaction_hash VARCHAR(255),
    sender_address VARCHAR(255),
    recipient_address VARCHAR(255),
    block_number BIGINT,
    block_timestamp TIMESTAMP,
    confirmation_count INTEGER,
    gas_price DECIMAL(36,18),
    gas_used BIGINT,
    transaction_fee DECIMAL(36,18),
    fee_currency VARCHAR(20),
    network_status VARCHAR(50),
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_line_crypto_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id),
    
    CONSTRAINT fk_line_crypto_asset
    FOREIGN KEY (crypto_asset_id) REFERENCES crypto_asset (crypto_asset_id)
);

-- Add indexes for faster lookups
CREATE INDEX idx_transaction_line_crypto_transaction ON transaction_line_crypto(transaction_id);
CREATE INDEX idx_transaction_line_crypto_asset ON transaction_line_crypto(crypto_asset_id);
CREATE INDEX idx_transaction_line_crypto_hash ON transaction_line_crypto(blockchain_transaction_hash);
CREATE INDEX idx_transaction_line_crypto_sender ON transaction_line_crypto(sender_address);
CREATE INDEX idx_transaction_line_crypto_recipient ON transaction_line_crypto(recipient_address);

-- Add partial unique index on blockchain_transaction_hash to prevent duplicates (only for non-null values)
CREATE UNIQUE INDEX uq_transaction_line_crypto_hash
ON transaction_line_crypto(blockchain_transaction_hash)
WHERE blockchain_transaction_hash IS NOT NULL;