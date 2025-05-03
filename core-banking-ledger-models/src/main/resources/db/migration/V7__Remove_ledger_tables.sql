-- V7__Remove_ledger_tables.sql
-- Migration script to remove ledger tables from the data model

-- First, drop the foreign key constraints that reference the ledger tables
ALTER TABLE ledger_entry DROP CONSTRAINT IF EXISTS fk_entry_transaction;
ALTER TABLE ledger_entry DROP CONSTRAINT IF EXISTS fk_entry_ledger_account;

-- Drop the ledger tables
DROP TABLE IF EXISTS ledger_entry;
DROP TABLE IF EXISTS ledger_account;

-- Drop the ledger-related enum types that are no longer needed
DROP TYPE IF EXISTS ledger_account_type_enum CASCADE;
DROP TYPE IF EXISTS debit_credit_indicator_enum CASCADE;

-- Drop any casts related to the removed enum types
DROP CAST IF EXISTS (varchar AS ledger_account_type_enum);
DROP CAST IF EXISTS (varchar AS debit_credit_indicator_enum);