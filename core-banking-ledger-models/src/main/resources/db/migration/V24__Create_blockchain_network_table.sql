-- V24__Create_blockchain_network_table.sql
-- Create blockchain_network table to store information about supported blockchain networks

CREATE TABLE IF NOT EXISTS blockchain_network (
    blockchain_network_id BIGINT NOT NULL PRIMARY KEY,
    network_name VARCHAR(100) NOT NULL,
    network_code VARCHAR(20) NOT NULL,
    is_testnet BOOLEAN NOT NULL DEFAULT FALSE,
    blockchain_explorer_url VARCHAR(255),
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add index on network_code for faster lookups
CREATE INDEX idx_blockchain_network_code ON blockchain_network(network_code);

-- Add unique constraint on network_name to prevent duplicates
ALTER TABLE blockchain_network
ADD CONSTRAINT uq_blockchain_network_name UNIQUE (network_name);