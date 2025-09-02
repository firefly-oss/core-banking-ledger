-- V31__Convert_money_to_uuid.sql
-- Convert money table primary key and foreign keys from BIGINT to UUID

-- Step 1: Add new UUID columns alongside existing BIGINT columns
ALTER TABLE money 
ADD COLUMN money_id_uuid UUID DEFAULT gen_random_uuid(),
ADD COLUMN crypto_asset_id_uuid UUID;

-- Step 2: Populate UUID columns with generated values for existing records
UPDATE money SET money_id_uuid = gen_random_uuid() WHERE money_id_uuid IS NULL;

-- Step 3: Create a mapping table to track BIGINT to UUID conversions for money
CREATE TABLE money_id_mapping (
    old_bigint_id BIGINT NOT NULL,
    new_uuid_id UUID NOT NULL,
    PRIMARY KEY (old_bigint_id),
    UNIQUE (new_uuid_id)
);

-- Step 4: Populate the mapping table
INSERT INTO money_id_mapping (old_bigint_id, new_uuid_id)
SELECT money_id, money_id_uuid FROM money;

-- Step 5: For crypto_asset_id, we'll handle this after crypto_asset table is converted
-- For now, leave it NULL and populate in subsequent migrations

-- Step 6: Create indexes on new UUID columns
CREATE INDEX idx_money_uuid ON money(money_id_uuid);
CREATE INDEX idx_money_crypto_asset_uuid ON money(crypto_asset_id_uuid);

-- Step 7: Add comments to document the migration process
COMMENT ON COLUMN money.money_id_uuid IS 'New UUID primary key - will replace money_id';
COMMENT ON COLUMN money.crypto_asset_id_uuid IS 'New UUID foreign key to crypto_asset - will replace crypto_asset_id';

-- Step 8: Add NOT NULL constraint to the new UUID primary key
ALTER TABLE money ALTER COLUMN money_id_uuid SET NOT NULL;
