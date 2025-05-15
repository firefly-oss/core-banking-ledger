-- V20__Remove_transaction_category_table.sql

-- Remove the foreign key constraint from transaction table
ALTER TABLE transaction DROP CONSTRAINT IF EXISTS fk_transaction_category;

-- Drop the transaction_category table
DROP TABLE IF EXISTS transaction_category;

-- Add comment to transaction_category_id column to indicate it's now a reference to external service
COMMENT ON COLUMN transaction.transaction_category_id IS 'Reference to category ID in external master data microservice';

-- Drop the category_type_enum and its cast
DROP CAST IF EXISTS (varchar AS category_type_enum);
DROP TYPE IF EXISTS category_type_enum CASCADE;
