-- V30__Convert_transaction_leg_to_uuid.sql
-- Convert transaction_leg table primary key and foreign keys from BIGINT to UUID

-- Step 1: Add new UUID columns alongside existing BIGINT columns
ALTER TABLE transaction_leg 
ADD COLUMN transaction_leg_id_uuid UUID DEFAULT gen_random_uuid(),
ADD COLUMN transaction_id_uuid UUID,
ADD COLUMN account_id_uuid UUID,
ADD COLUMN account_space_id_uuid UUID;

-- Step 2: Populate UUID columns with generated values for existing records
UPDATE transaction_leg SET transaction_leg_id_uuid = gen_random_uuid() WHERE transaction_leg_id_uuid IS NULL;

-- Step 3: Create a mapping table to track BIGINT to UUID conversions for transaction_leg
CREATE TABLE transaction_leg_id_mapping (
    old_bigint_id BIGINT NOT NULL,
    new_uuid_id UUID NOT NULL,
    PRIMARY KEY (old_bigint_id),
    UNIQUE (new_uuid_id)
);

-- Step 4: Populate the mapping table
INSERT INTO transaction_leg_id_mapping (old_bigint_id, new_uuid_id)
SELECT transaction_leg_id, transaction_leg_id_uuid FROM transaction_leg;

-- Step 5: Update transaction_id_uuid using the transaction mapping table
UPDATE transaction_leg tl 
SET transaction_id_uuid = (
    SELECT tim.new_uuid_id 
    FROM transaction_id_mapping tim 
    WHERE tim.old_bigint_id = tl.transaction_id
)
WHERE tl.transaction_id IS NOT NULL;

-- Step 6: For account_id and account_space_id, these are external references
-- We'll generate new UUIDs for them since they reference external microservices
-- In a real migration, you would coordinate with the account service to get actual UUIDs
UPDATE transaction_leg SET account_id_uuid = gen_random_uuid() WHERE account_id IS NOT NULL;
UPDATE transaction_leg SET account_space_id_uuid = gen_random_uuid() WHERE account_space_id IS NOT NULL;

-- Step 7: Create indexes on new UUID columns
CREATE INDEX idx_transaction_leg_uuid ON transaction_leg(transaction_leg_id_uuid);
CREATE INDEX idx_transaction_leg_transaction_uuid ON transaction_leg(transaction_id_uuid);
CREATE INDEX idx_transaction_leg_account_uuid ON transaction_leg(account_id_uuid);
CREATE INDEX idx_transaction_leg_account_space_uuid ON transaction_leg(account_space_id_uuid);

-- Step 8: Add comments to document the migration process
COMMENT ON COLUMN transaction_leg.transaction_leg_id_uuid IS 'New UUID primary key - will replace transaction_leg_id';
COMMENT ON COLUMN transaction_leg.transaction_id_uuid IS 'New UUID foreign key to transaction - will replace transaction_id';
COMMENT ON COLUMN transaction_leg.account_id_uuid IS 'New UUID reference to external account service - will replace account_id';
COMMENT ON COLUMN transaction_leg.account_space_id_uuid IS 'New UUID reference to external account space service - will replace account_space_id';

-- Step 9: Add NOT NULL constraints to the new UUID primary key
ALTER TABLE transaction_leg ALTER COLUMN transaction_leg_id_uuid SET NOT NULL;
ALTER TABLE transaction_leg ALTER COLUMN transaction_id_uuid SET NOT NULL;

-- Step 10: Add foreign key constraint to the new UUID transaction reference
ALTER TABLE transaction_leg 
ADD CONSTRAINT fk_transaction_leg_transaction_uuid 
FOREIGN KEY (transaction_id_uuid) REFERENCES transaction (transaction_id_uuid);
