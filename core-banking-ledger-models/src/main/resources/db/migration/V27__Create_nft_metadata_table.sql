-- V27__Create_nft_metadata_table.sql
-- Create nft_metadata table to store metadata for NFT tokens

CREATE TABLE IF NOT EXISTS nft_metadata (
    nft_metadata_id BIGINT NOT NULL PRIMARY KEY,
    crypto_asset_id BIGINT NOT NULL,
    token_id VARCHAR(255) NOT NULL,
    token_standard VARCHAR(20) NOT NULL, -- ERC-721, ERC-1155, etc.
    metadata_uri VARCHAR(255),
    token_name VARCHAR(255),
    token_description TEXT,
    creator_address VARCHAR(255),
    creation_date TIMESTAMP,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_nft_crypto_asset
    FOREIGN KEY (crypto_asset_id) REFERENCES crypto_asset (crypto_asset_id)
);

-- Add indexes for faster lookups
CREATE INDEX idx_nft_metadata_asset ON nft_metadata(crypto_asset_id);
CREATE INDEX idx_nft_metadata_token_id ON nft_metadata(token_id);
CREATE INDEX idx_nft_metadata_standard ON nft_metadata(token_standard);
CREATE INDEX idx_nft_metadata_creator ON nft_metadata(creator_address);

-- Add unique constraint on crypto_asset_id and token_id to prevent duplicates
ALTER TABLE nft_metadata
ADD CONSTRAINT uq_nft_metadata_asset_token UNIQUE (crypto_asset_id, token_id);