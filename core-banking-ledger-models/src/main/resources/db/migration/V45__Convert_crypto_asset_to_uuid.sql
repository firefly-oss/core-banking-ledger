-- V45__Convert_crypto_asset_to_uuid.sql
-- Convert crypto_asset table primary key and foreign keys from BIGINT to UUID

-- Step 1: Add new UUID columns alongside existing BIGINT columns
ALTER TABLE crypto_asset 
ADD COLUMN crypto_asset_id_uuid UUID DEFAULT gen_random_uuid(),
ADD COLUMN blockchain_network_id_uuid UUID;

-- Step 2: Populate UUID columns with generated values for existing records
UPDATE crypto_asset SET crypto_asset_id_uuid = gen_random_uuid() WHERE crypto_asset_id_uuid IS NULL;

-- Step 3: Create a mapping table to track BIGINT to UUID conversions for crypto_asset
CREATE TABLE crypto_asset_id_mapping (
    old_bigint_id BIGINT NOT NULL,
    new_uuid_id UUID NOT NULL,
    PRIMARY KEY (old_bigint_id),
    UNIQUE (new_uuid_id)
);

-- Step 4: Populate the mapping table
INSERT INTO crypto_asset_id_mapping (old_bigint_id, new_uuid_id)
SELECT crypto_asset_id, crypto_asset_id_uuid FROM crypto_asset;

-- Step 5: Update blockchain_network_id_uuid using the blockchain_network mapping table
UPDATE crypto_asset ca 
SET blockchain_network_id_uuid = (
    SELECT bnim.new_uuid_id 
    FROM blockchain_network_id_mapping bnim 
    WHERE bnim.old_bigint_id = ca.blockchain_network_id
)
WHERE ca.blockchain_network_id IS NOT NULL;

-- Step 6: Create indexes on new UUID columns
CREATE INDEX idx_crypto_asset_uuid ON crypto_asset(crypto_asset_id_uuid);
CREATE INDEX idx_crypto_asset_blockchain_network_uuid ON crypto_asset(blockchain_network_id_uuid);

-- Step 7: Add comments to document the migration process
COMMENT ON COLUMN crypto_asset.crypto_asset_id_uuid IS 'New UUID primary key - will replace crypto_asset_id';
COMMENT ON COLUMN crypto_asset.blockchain_network_id_uuid IS 'New UUID foreign key to blockchain_network - will replace blockchain_network_id';

-- Step 8: Add NOT NULL constraints to the new UUID columns
ALTER TABLE crypto_asset ALTER COLUMN crypto_asset_id_uuid SET NOT NULL;
ALTER TABLE crypto_asset ALTER COLUMN blockchain_network_id_uuid SET NOT NULL;

-- Step 9: Add unique constraint to crypto_asset_id_uuid so it can be referenced by foreign keys
ALTER TABLE crypto_asset ADD CONSTRAINT uq_crypto_asset_id_uuid UNIQUE (crypto_asset_id_uuid);

-- Step 10: Add foreign key constraint to the new UUID blockchain_network reference
ALTER TABLE crypto_asset
ADD CONSTRAINT fk_crypto_asset_blockchain_network_uuid
FOREIGN KEY (blockchain_network_id_uuid) REFERENCES blockchain_network (blockchain_network_id_uuid);
