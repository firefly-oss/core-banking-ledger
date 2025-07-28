-- V25__Create_crypto_asset_table.sql
-- Create crypto_asset table to store information about supported cryptocurrencies and tokens

CREATE TABLE IF NOT EXISTS crypto_asset (
    crypto_asset_id BIGINT NOT NULL PRIMARY KEY,
    asset_symbol VARCHAR(20) NOT NULL,
    asset_name VARCHAR(100) NOT NULL,
    asset_type asset_type_enum NOT NULL,
    blockchain_network_id BIGINT NOT NULL,
    contract_address VARCHAR(255),
    decimals INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_crypto_asset_blockchain
    FOREIGN KEY (blockchain_network_id) REFERENCES blockchain_network (blockchain_network_id)
);

-- Add indexes for faster lookups
CREATE INDEX idx_crypto_asset_symbol ON crypto_asset(asset_symbol);
CREATE INDEX idx_crypto_asset_type ON crypto_asset(asset_type);
CREATE INDEX idx_crypto_asset_blockchain ON crypto_asset(blockchain_network_id);

-- Add unique constraint on asset_symbol and blockchain_network_id to prevent duplicates
ALTER TABLE crypto_asset
ADD CONSTRAINT uq_crypto_asset_symbol_network UNIQUE (asset_symbol, blockchain_network_id);

-- Add unique constraint on contract_address and blockchain_network_id for tokens
ALTER TABLE crypto_asset
ADD CONSTRAINT uq_crypto_asset_contract_network UNIQUE (contract_address, blockchain_network_id)
WHERE contract_address IS NOT NULL;