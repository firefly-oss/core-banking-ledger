-- V44__Convert_blockchain_network_to_uuid.sql
-- Convert blockchain_network table primary key from BIGINT to UUID

-- Step 1: Add new UUID column alongside existing BIGINT column
ALTER TABLE blockchain_network 
ADD COLUMN blockchain_network_id_uuid UUID DEFAULT gen_random_uuid();

-- Step 2: Populate UUID column with generated values for existing records
UPDATE blockchain_network SET blockchain_network_id_uuid = gen_random_uuid() WHERE blockchain_network_id_uuid IS NULL;

-- Step 3: Create a mapping table to track BIGINT to UUID conversions for blockchain_network
CREATE TABLE blockchain_network_id_mapping (
    old_bigint_id BIGINT NOT NULL,
    new_uuid_id UUID NOT NULL,
    PRIMARY KEY (old_bigint_id),
    UNIQUE (new_uuid_id)
);

-- Step 4: Populate the mapping table
INSERT INTO blockchain_network_id_mapping (old_bigint_id, new_uuid_id)
SELECT blockchain_network_id, blockchain_network_id_uuid FROM blockchain_network;

-- Step 5: Create indexes on new UUID column
CREATE INDEX idx_blockchain_network_uuid ON blockchain_network(blockchain_network_id_uuid);

-- Step 6: Add comments to document the migration process
COMMENT ON COLUMN blockchain_network.blockchain_network_id_uuid IS 'New UUID primary key - will replace blockchain_network_id';

-- Step 7: Add NOT NULL constraint to the new UUID primary key
ALTER TABLE blockchain_network ALTER COLUMN blockchain_network_id_uuid SET NOT NULL;

-- Step 8: Add unique constraint to blockchain_network_id_uuid so it can be referenced by foreign keys
ALTER TABLE blockchain_network ADD CONSTRAINT uq_blockchain_network_id_uuid UNIQUE (blockchain_network_id_uuid);
