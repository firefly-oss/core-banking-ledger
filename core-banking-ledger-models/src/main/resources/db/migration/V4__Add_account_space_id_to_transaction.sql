-- V4__Add_account_space_id_to_transaction.sql

-- Add account_space_id column to transaction table
ALTER TABLE transaction
ADD COLUMN account_space_id BIGINT;

-- Add comment explaining the purpose of the column
COMMENT ON COLUMN transaction.account_space_id IS 'Reference to the account space associated with this transaction';

-- Update existing transactions if needed (this is a placeholder and might need to be adjusted based on business requirements)
-- UPDATE transaction SET account_space_id = ... WHERE ...;