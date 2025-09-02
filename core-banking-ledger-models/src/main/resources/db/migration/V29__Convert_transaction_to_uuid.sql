-- V29__Convert_transaction_to_uuid.sql
-- Convert transaction table primary key and foreign keys from BIGINT to UUID
-- This is a critical migration that must be executed carefully

-- Step 1: Add new UUID columns alongside existing BIGINT columns
ALTER TABLE transaction 
ADD COLUMN transaction_id_uuid UUID DEFAULT gen_random_uuid(),
ADD COLUMN related_transaction_id_uuid UUID,
ADD COLUMN account_id_uuid UUID,
ADD COLUMN account_space_id_uuid UUID,
ADD COLUMN transaction_category_id_uuid UUID,
ADD COLUMN blockchain_network_id_uuid UUID;

-- Step 2: Populate UUID columns with generated values for existing records
-- For primary key, use the default gen_random_uuid() that was already set
UPDATE transaction SET transaction_id_uuid = gen_random_uuid() WHERE transaction_id_uuid IS NULL;

-- For foreign key references, we'll need to handle these after other tables are converted
-- For now, we'll leave them NULL and populate them in subsequent migrations

-- Step 3: Create a mapping table to track BIGINT to UUID conversions for transaction
CREATE TABLE transaction_id_mapping (
    old_bigint_id BIGINT NOT NULL,
    new_uuid_id UUID NOT NULL,
    PRIMARY KEY (old_bigint_id),
    UNIQUE (new_uuid_id)
);

-- Step 4: Populate the mapping table
INSERT INTO transaction_id_mapping (old_bigint_id, new_uuid_id)
SELECT transaction_id, transaction_id_uuid FROM transaction;

-- Step 5: Update related_transaction_id_uuid using the mapping
UPDATE transaction t1 
SET related_transaction_id_uuid = (
    SELECT tim.new_uuid_id 
    FROM transaction_id_mapping tim 
    WHERE tim.old_bigint_id = t1.related_transaction_id
)
WHERE t1.related_transaction_id IS NOT NULL;

-- Step 6: Create indexes on new UUID columns
CREATE INDEX idx_transaction_uuid ON transaction(transaction_id_uuid);
CREATE INDEX idx_transaction_related_uuid ON transaction(related_transaction_id_uuid);
CREATE INDEX idx_transaction_account_uuid ON transaction(account_id_uuid);
CREATE INDEX idx_transaction_account_space_uuid ON transaction(account_space_id_uuid);
CREATE INDEX idx_transaction_category_uuid ON transaction(transaction_category_id_uuid);
CREATE INDEX idx_transaction_blockchain_network_uuid ON transaction(blockchain_network_id_uuid);

-- Step 7: Add comments to document the migration process
COMMENT ON COLUMN transaction.transaction_id_uuid IS 'New UUID primary key - will replace transaction_id';
COMMENT ON COLUMN transaction.related_transaction_id_uuid IS 'New UUID foreign key - will replace related_transaction_id';
COMMENT ON COLUMN transaction.account_id_uuid IS 'New UUID reference to external account service - will replace account_id';
COMMENT ON COLUMN transaction.account_space_id_uuid IS 'New UUID reference to external account space service - will replace account_space_id';
COMMENT ON COLUMN transaction.transaction_category_id_uuid IS 'New UUID reference to external category service - will replace transaction_category_id';
COMMENT ON COLUMN transaction.blockchain_network_id_uuid IS 'New UUID foreign key - will replace blockchain_network_id';

-- Step 8: Add NOT NULL constraints to the new UUID primary key
ALTER TABLE transaction ALTER COLUMN transaction_id_uuid SET NOT NULL;

-- Step 9: Add unique constraint to transaction_id_uuid so it can be referenced by foreign keys
ALTER TABLE transaction ADD CONSTRAINT uq_transaction_id_uuid UNIQUE (transaction_id_uuid);

-- Note: We cannot drop the old BIGINT columns yet because other tables still reference them
-- This will be done in subsequent migrations after all referencing tables are converted
