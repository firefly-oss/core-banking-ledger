-- V47__Convert_transaction_line_crypto_to_uuid.sql
-- Convert transaction_line_crypto table primary key and foreign keys from BIGINT to UUID

-- Step 1: Add new UUID columns alongside existing BIGINT columns
ALTER TABLE transaction_line_crypto 
ADD COLUMN transaction_line_crypto_id_uuid UUID DEFAULT gen_random_uuid(),
ADD COLUMN transaction_id_uuid UUID,
ADD COLUMN crypto_asset_id_uuid UUID;

-- Step 2: Populate UUID columns with generated values for existing records
UPDATE transaction_line_crypto SET transaction_line_crypto_id_uuid = gen_random_uuid() WHERE transaction_line_crypto_id_uuid IS NULL;

-- Step 3: Create a mapping table to track BIGINT to UUID conversions for transaction_line_crypto
CREATE TABLE transaction_line_crypto_id_mapping (
    old_bigint_id BIGINT NOT NULL,
    new_uuid_id UUID NOT NULL,
    PRIMARY KEY (old_bigint_id),
    UNIQUE (new_uuid_id)
);

-- Step 4: Populate the mapping table
INSERT INTO transaction_line_crypto_id_mapping (old_bigint_id, new_uuid_id)
SELECT transaction_line_crypto_id, transaction_line_crypto_id_uuid FROM transaction_line_crypto;

-- Step 5: Update transaction_id_uuid using the transaction mapping table
UPDATE transaction_line_crypto tlc 
SET transaction_id_uuid = (
    SELECT tim.new_uuid_id 
    FROM transaction_id_mapping tim 
    WHERE tim.old_bigint_id = tlc.transaction_id
)
WHERE tlc.transaction_id IS NOT NULL;

-- Step 6: Update crypto_asset_id_uuid using the crypto_asset mapping table
UPDATE transaction_line_crypto tlc 
SET crypto_asset_id_uuid = (
    SELECT caim.new_uuid_id 
    FROM crypto_asset_id_mapping caim 
    WHERE caim.old_bigint_id = tlc.crypto_asset_id
)
WHERE tlc.crypto_asset_id IS NOT NULL;

-- Step 7: Create indexes on new UUID columns
CREATE INDEX idx_transaction_line_crypto_uuid ON transaction_line_crypto(transaction_line_crypto_id_uuid);
CREATE INDEX idx_transaction_line_crypto_transaction_uuid ON transaction_line_crypto(transaction_id_uuid);
CREATE INDEX idx_transaction_line_crypto_asset_uuid ON transaction_line_crypto(crypto_asset_id_uuid);

-- Step 8: Add comments to document the migration process
COMMENT ON COLUMN transaction_line_crypto.transaction_line_crypto_id_uuid IS 'New UUID primary key - will replace transaction_line_crypto_id';
COMMENT ON COLUMN transaction_line_crypto.transaction_id_uuid IS 'New UUID foreign key to transaction - will replace transaction_id';
COMMENT ON COLUMN transaction_line_crypto.crypto_asset_id_uuid IS 'New UUID foreign key to crypto_asset - will replace crypto_asset_id';

-- Step 9: Add NOT NULL constraints to the new UUID columns
ALTER TABLE transaction_line_crypto ALTER COLUMN transaction_line_crypto_id_uuid SET NOT NULL;
ALTER TABLE transaction_line_crypto ALTER COLUMN transaction_id_uuid SET NOT NULL;
ALTER TABLE transaction_line_crypto ALTER COLUMN crypto_asset_id_uuid SET NOT NULL;

-- Step 10: Add foreign key constraints to the new UUID references
ALTER TABLE transaction_line_crypto 
ADD CONSTRAINT fk_transaction_line_crypto_transaction_uuid 
FOREIGN KEY (transaction_id_uuid) REFERENCES transaction (transaction_id_uuid);

ALTER TABLE transaction_line_crypto 
ADD CONSTRAINT fk_transaction_line_crypto_asset_uuid 
FOREIGN KEY (crypto_asset_id_uuid) REFERENCES crypto_asset (crypto_asset_id_uuid);
