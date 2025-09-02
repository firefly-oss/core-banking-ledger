-- V49__Update_cross_table_uuid_references.sql
-- Update cross-table UUID references that couldn't be populated during individual table conversions

-- Step 1: Update money table crypto_asset_id_uuid references
-- Check if crypto_asset_id column exists before updating
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_name = 'money' AND column_name = 'crypto_asset_id') THEN
        UPDATE money m
        SET crypto_asset_id_uuid = (
            SELECT caim.new_uuid_id
            FROM crypto_asset_id_mapping caim
            WHERE caim.old_bigint_id = m.crypto_asset_id
        )
        WHERE m.crypto_asset_id IS NOT NULL;
    END IF;
END $$;

-- Step 2: Update transaction table blockchain_network_id_uuid references
UPDATE transaction t 
SET blockchain_network_id_uuid = (
    SELECT bnim.new_uuid_id 
    FROM blockchain_network_id_mapping bnim 
    WHERE bnim.old_bigint_id = t.blockchain_network_id
)
WHERE t.blockchain_network_id IS NOT NULL;

-- Step 3: Add foreign key constraints for cross-table references
ALTER TABLE money 
ADD CONSTRAINT fk_money_crypto_asset_uuid 
FOREIGN KEY (crypto_asset_id_uuid) REFERENCES crypto_asset (crypto_asset_id_uuid);

ALTER TABLE transaction 
ADD CONSTRAINT fk_transaction_blockchain_network_uuid 
FOREIGN KEY (blockchain_network_id_uuid) REFERENCES blockchain_network (blockchain_network_id_uuid);

-- Step 4: Update any remaining fee-related transaction references
-- This step is now handled in V42 migration, so we can skip it or add a comment
-- The fee_related_transaction_id_uuid column is populated in V42__Convert_transaction_line_fee_to_uuid.sql

-- Step 5: Create indexes for the newly populated foreign key columns
CREATE INDEX idx_money_crypto_asset_uuid_fk ON money(crypto_asset_id_uuid) WHERE crypto_asset_id_uuid IS NOT NULL;
CREATE INDEX idx_transaction_blockchain_network_uuid_fk ON transaction(blockchain_network_id_uuid) WHERE blockchain_network_id_uuid IS NOT NULL;

-- Step 6: Add comments for the updated references
COMMENT ON CONSTRAINT fk_money_crypto_asset_uuid ON money IS 'Foreign key constraint for UUID reference to crypto_asset table';
COMMENT ON CONSTRAINT fk_transaction_blockchain_network_uuid ON transaction IS 'Foreign key constraint for UUID reference to blockchain_network table';
