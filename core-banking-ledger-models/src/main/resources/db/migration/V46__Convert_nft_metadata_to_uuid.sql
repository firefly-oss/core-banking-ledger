-- V46__Convert_nft_metadata_to_uuid.sql
-- Convert nft_metadata table primary key and foreign keys from BIGINT to UUID

-- Step 1: Add new UUID columns alongside existing BIGINT columns
ALTER TABLE nft_metadata 
ADD COLUMN nft_metadata_id_uuid UUID DEFAULT gen_random_uuid(),
ADD COLUMN crypto_asset_id_uuid UUID;

-- Step 2: Populate UUID columns with generated values for existing records
UPDATE nft_metadata SET nft_metadata_id_uuid = gen_random_uuid() WHERE nft_metadata_id_uuid IS NULL;

-- Step 3: Create a mapping table to track BIGINT to UUID conversions for nft_metadata
CREATE TABLE nft_metadata_id_mapping (
    old_bigint_id BIGINT NOT NULL,
    new_uuid_id UUID NOT NULL,
    PRIMARY KEY (old_bigint_id),
    UNIQUE (new_uuid_id)
);

-- Step 4: Populate the mapping table
INSERT INTO nft_metadata_id_mapping (old_bigint_id, new_uuid_id)
SELECT nft_metadata_id, nft_metadata_id_uuid FROM nft_metadata;

-- Step 5: Update crypto_asset_id_uuid using the crypto_asset mapping table
UPDATE nft_metadata nm 
SET crypto_asset_id_uuid = (
    SELECT caim.new_uuid_id 
    FROM crypto_asset_id_mapping caim 
    WHERE caim.old_bigint_id = nm.crypto_asset_id
)
WHERE nm.crypto_asset_id IS NOT NULL;

-- Step 6: Create indexes on new UUID columns
CREATE INDEX idx_nft_metadata_uuid ON nft_metadata(nft_metadata_id_uuid);
CREATE INDEX idx_nft_metadata_crypto_asset_uuid ON nft_metadata(crypto_asset_id_uuid);

-- Step 7: Add comments to document the migration process
COMMENT ON COLUMN nft_metadata.nft_metadata_id_uuid IS 'New UUID primary key - will replace nft_metadata_id';
COMMENT ON COLUMN nft_metadata.crypto_asset_id_uuid IS 'New UUID foreign key to crypto_asset - will replace crypto_asset_id';

-- Step 8: Add NOT NULL constraints to the new UUID columns
ALTER TABLE nft_metadata ALTER COLUMN nft_metadata_id_uuid SET NOT NULL;
ALTER TABLE nft_metadata ALTER COLUMN crypto_asset_id_uuid SET NOT NULL;

-- Step 9: Add foreign key constraint to the new UUID crypto_asset reference
ALTER TABLE nft_metadata 
ADD CONSTRAINT fk_nft_metadata_crypto_asset_uuid 
FOREIGN KEY (crypto_asset_id_uuid) REFERENCES crypto_asset (crypto_asset_id_uuid);
