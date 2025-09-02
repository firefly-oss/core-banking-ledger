-- V42__Convert_transaction_line_fee_to_uuid.sql
-- Convert transaction_line_fee table primary key and foreign keys from BIGINT to UUID

-- Step 1: Add new UUID columns alongside existing BIGINT columns
ALTER TABLE transaction_line_fee
ADD COLUMN transaction_line_fee_id_uuid UUID DEFAULT gen_random_uuid(),
ADD COLUMN transaction_id_uuid UUID,
ADD COLUMN fee_related_transaction_id_uuid UUID;

-- Step 2: Populate UUID columns with generated values for existing records
UPDATE transaction_line_fee SET transaction_line_fee_id_uuid = gen_random_uuid() WHERE transaction_line_fee_id_uuid IS NULL;

-- Step 3: Create a mapping table to track BIGINT to UUID conversions for transaction_line_fee
CREATE TABLE transaction_line_fee_id_mapping (
    old_bigint_id BIGINT NOT NULL,
    new_uuid_id UUID NOT NULL,
    PRIMARY KEY (old_bigint_id),
    UNIQUE (new_uuid_id)
);

-- Step 4: Populate the mapping table
INSERT INTO transaction_line_fee_id_mapping (old_bigint_id, new_uuid_id)
SELECT transaction_line_fee_id, transaction_line_fee_id_uuid FROM transaction_line_fee;

-- Step 5: Update transaction_id_uuid using the transaction mapping table
UPDATE transaction_line_fee tlf
SET transaction_id_uuid = (
    SELECT tim.new_uuid_id
    FROM transaction_id_mapping tim
    WHERE tim.old_bigint_id = tlf.transaction_id
)
WHERE tlf.transaction_id IS NOT NULL;

-- Step 5a: Update fee_related_transaction_id_uuid using the transaction mapping table
UPDATE transaction_line_fee tlf
SET fee_related_transaction_id_uuid = (
    SELECT tim.new_uuid_id
    FROM transaction_id_mapping tim
    WHERE tim.old_bigint_id = tlf.fee_related_transaction_id
)
WHERE tlf.fee_related_transaction_id IS NOT NULL;

-- Step 6: Create indexes on new UUID columns
CREATE INDEX idx_transaction_line_fee_uuid ON transaction_line_fee(transaction_line_fee_id_uuid);
CREATE INDEX idx_transaction_line_fee_transaction_uuid ON transaction_line_fee(transaction_id_uuid);
CREATE INDEX idx_transaction_line_fee_related_transaction_uuid ON transaction_line_fee(fee_related_transaction_id_uuid);

-- Step 7: Add comments to document the migration process
COMMENT ON COLUMN transaction_line_fee.transaction_line_fee_id_uuid IS 'New UUID primary key - will replace transaction_line_fee_id';
COMMENT ON COLUMN transaction_line_fee.transaction_id_uuid IS 'New UUID foreign key to transaction - will replace transaction_id';
COMMENT ON COLUMN transaction_line_fee.fee_related_transaction_id_uuid IS 'New UUID foreign key to related transaction - will replace fee_related_transaction_id';

-- Step 8: Add NOT NULL constraints to the new UUID columns
ALTER TABLE transaction_line_fee ALTER COLUMN transaction_line_fee_id_uuid SET NOT NULL;
ALTER TABLE transaction_line_fee ALTER COLUMN transaction_id_uuid SET NOT NULL;

-- Step 9: Add foreign key constraint to the new UUID transaction reference
ALTER TABLE transaction_line_fee 
ADD CONSTRAINT fk_transaction_line_fee_transaction_uuid 
FOREIGN KEY (transaction_id_uuid) REFERENCES transaction (transaction_id_uuid);
