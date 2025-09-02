-- V32__Convert_statement_to_uuid.sql
-- Convert statement table primary key and foreign keys from BIGINT to UUID

-- Step 1: Add new UUID columns alongside existing BIGINT columns
ALTER TABLE statement 
ADD COLUMN statement_id_uuid UUID DEFAULT gen_random_uuid(),
ADD COLUMN account_id_uuid UUID,
ADD COLUMN account_space_id_uuid UUID;

-- Step 2: Populate UUID columns with generated values for existing records
UPDATE statement SET statement_id_uuid = gen_random_uuid() WHERE statement_id_uuid IS NULL;

-- Step 3: Create a mapping table to track BIGINT to UUID conversions for statement
CREATE TABLE statement_id_mapping (
    old_bigint_id BIGINT NOT NULL,
    new_uuid_id UUID NOT NULL,
    PRIMARY KEY (old_bigint_id),
    UNIQUE (new_uuid_id)
);

-- Step 4: Populate the mapping table
INSERT INTO statement_id_mapping (old_bigint_id, new_uuid_id)
SELECT statement_id, statement_id_uuid FROM statement;

-- Step 5: For account_id and account_space_id, these are external references
-- We'll generate new UUIDs for them since they reference external microservices
-- In a real migration, you would coordinate with the account service to get actual UUIDs
UPDATE statement SET account_id_uuid = gen_random_uuid() WHERE account_id IS NOT NULL;
UPDATE statement SET account_space_id_uuid = gen_random_uuid() WHERE account_space_id IS NOT NULL;

-- Step 6: Create indexes on new UUID columns
CREATE INDEX idx_statement_uuid ON statement(statement_id_uuid);
CREATE INDEX idx_statement_account_uuid ON statement(account_id_uuid);
CREATE INDEX idx_statement_account_space_uuid ON statement(account_space_id_uuid);

-- Step 7: Add comments to document the migration process
COMMENT ON COLUMN statement.statement_id_uuid IS 'New UUID primary key - will replace statement_id';
COMMENT ON COLUMN statement.account_id_uuid IS 'New UUID reference to external account service - will replace account_id';
COMMENT ON COLUMN statement.account_space_id_uuid IS 'New UUID reference to external account space service - will replace account_space_id';

-- Step 8: Add NOT NULL constraint to the new UUID primary key
ALTER TABLE statement ALTER COLUMN statement_id_uuid SET NOT NULL;

-- Step 9: Update the check constraint to work with UUID columns
ALTER TABLE statement DROP CONSTRAINT IF EXISTS check_statement_target;
ALTER TABLE statement 
ADD CONSTRAINT check_statement_target_uuid
CHECK ((account_id_uuid IS NOT NULL AND account_space_id_uuid IS NULL) OR
       (account_id_uuid IS NULL AND account_space_id_uuid IS NOT NULL));
