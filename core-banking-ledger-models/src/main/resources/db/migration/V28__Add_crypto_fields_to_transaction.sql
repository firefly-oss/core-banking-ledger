-- V28__Add_crypto_fields_to_transaction.sql
-- Add crypto-related fields to the transaction table

-- Add asset_type column with default value 'FIAT'
ALTER TABLE transaction 
ADD COLUMN asset_type asset_type_enum NOT NULL DEFAULT 'FIAT';

-- Add blockchain_network_id column (nullable)
ALTER TABLE transaction
ADD COLUMN blockchain_network_id BIGINT,
ADD CONSTRAINT fk_transaction_blockchain
    FOREIGN KEY (blockchain_network_id) REFERENCES blockchain_network (blockchain_network_id);

-- Add crypto-specific compliance fields
ALTER TABLE transaction
ADD COLUMN blockchain_transaction_hash VARCHAR(255),
ADD COLUMN crypto_compliance_check_result VARCHAR(50),
ADD COLUMN crypto_address_risk_score INTEGER,
ADD COLUMN crypto_transaction_source VARCHAR(100);

-- Add index for asset_type for faster filtering
CREATE INDEX idx_transaction_asset_type ON transaction(asset_type);

-- Add index for blockchain_network_id for faster joins
CREATE INDEX idx_transaction_blockchain_network ON transaction(blockchain_network_id);

-- Add index for blockchain_transaction_hash for faster lookups
CREATE INDEX idx_transaction_blockchain_hash ON transaction(blockchain_transaction_hash);

-- Add unique constraint on blockchain_transaction_hash to prevent duplicates
ALTER TABLE transaction
ADD CONSTRAINT uq_transaction_blockchain_hash UNIQUE (blockchain_transaction_hash)
WHERE blockchain_transaction_hash IS NOT NULL;