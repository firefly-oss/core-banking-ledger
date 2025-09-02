-- V48__Convert_transaction_line_ach_to_uuid.sql
-- Convert transaction_line_ach table primary key and foreign keys from BIGINT to UUID

-- Step 1: Add new UUID columns alongside existing BIGINT columns
ALTER TABLE transaction_line_ach 
ADD COLUMN transaction_line_ach_id_uuid UUID DEFAULT gen_random_uuid(),
ADD COLUMN transaction_id_uuid UUID,
ADD COLUMN ach_source_account_id_uuid UUID,
ADD COLUMN ach_destination_account_id_uuid UUID;

-- Step 2: Populate UUID columns with generated values for existing records
UPDATE transaction_line_ach SET transaction_line_ach_id_uuid = gen_random_uuid() WHERE transaction_line_ach_id_uuid IS NULL;

-- Step 3: Create a mapping table to track BIGINT to UUID conversions for transaction_line_ach
CREATE TABLE transaction_line_ach_id_mapping (
    old_bigint_id BIGINT NOT NULL,
    new_uuid_id UUID NOT NULL,
    PRIMARY KEY (old_bigint_id),
    UNIQUE (new_uuid_id)
);

-- Step 4: Populate the mapping table
INSERT INTO transaction_line_ach_id_mapping (old_bigint_id, new_uuid_id)
SELECT transaction_line_ach_id, transaction_line_ach_id_uuid FROM transaction_line_ach;

-- Step 5: Update transaction_id_uuid using the transaction mapping table
UPDATE transaction_line_ach tla 
SET transaction_id_uuid = (
    SELECT tim.new_uuid_id 
    FROM transaction_id_mapping tim 
    WHERE tim.old_bigint_id = tla.transaction_id
)
WHERE tla.transaction_id IS NOT NULL;

-- Step 6: For account references, generate new UUIDs (these are external references)
UPDATE transaction_line_ach SET ach_source_account_id_uuid = gen_random_uuid() WHERE ach_source_account_id IS NOT NULL;
UPDATE transaction_line_ach SET ach_destination_account_id_uuid = gen_random_uuid() WHERE ach_destination_account_id IS NOT NULL;

-- Step 7: Create indexes on new UUID columns
CREATE INDEX idx_transaction_line_ach_uuid ON transaction_line_ach(transaction_line_ach_id_uuid);
CREATE INDEX idx_transaction_line_ach_transaction_uuid ON transaction_line_ach(transaction_id_uuid);
CREATE INDEX idx_transaction_line_ach_source_account_uuid ON transaction_line_ach(ach_source_account_id_uuid);
CREATE INDEX idx_transaction_line_ach_dest_account_uuid ON transaction_line_ach(ach_destination_account_id_uuid);

-- Step 8: Add comments to document the migration process
COMMENT ON COLUMN transaction_line_ach.transaction_line_ach_id_uuid IS 'New UUID primary key - will replace transaction_line_ach_id';
COMMENT ON COLUMN transaction_line_ach.transaction_id_uuid IS 'New UUID foreign key to transaction - will replace transaction_id';
COMMENT ON COLUMN transaction_line_ach.ach_source_account_id_uuid IS 'New UUID reference to external account service - will replace ach_source_account_id';
COMMENT ON COLUMN transaction_line_ach.ach_destination_account_id_uuid IS 'New UUID reference to external account service - will replace ach_destination_account_id';

-- Step 9: Add NOT NULL constraints to the new UUID columns
ALTER TABLE transaction_line_ach ALTER COLUMN transaction_line_ach_id_uuid SET NOT NULL;
ALTER TABLE transaction_line_ach ALTER COLUMN transaction_id_uuid SET NOT NULL;

-- Step 10: Add foreign key constraint to the new UUID transaction reference
ALTER TABLE transaction_line_ach 
ADD CONSTRAINT fk_transaction_line_ach_transaction_uuid 
FOREIGN KEY (transaction_id_uuid) REFERENCES transaction (transaction_id_uuid);
