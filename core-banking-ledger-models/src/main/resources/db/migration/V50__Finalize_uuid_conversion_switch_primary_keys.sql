-- V50__Finalize_uuid_conversion_switch_primary_keys.sql
-- Final migration to switch primary keys from BIGINT to UUID and drop old columns
-- WARNING: This is a destructive migration - ensure all applications are updated to use UUID columns

-- Step 1: Drop all foreign key constraints that reference old BIGINT primary keys
ALTER TABLE transaction DROP CONSTRAINT IF EXISTS fk_transaction_related;
ALTER TABLE transaction DROP CONSTRAINT IF EXISTS fk_transaction_blockchain;
ALTER TABLE transaction_leg DROP CONSTRAINT IF EXISTS fk_leg_transaction;
ALTER TABLE transaction_attachment DROP CONSTRAINT IF EXISTS fk_transaction_attachment_transaction;
ALTER TABLE transaction_attachment DROP CONSTRAINT IF EXISTS fk_attachment_transaction;
ALTER TABLE transaction_status_history DROP CONSTRAINT IF EXISTS fk_status_history_transaction;
ALTER TABLE transaction_line_card DROP CONSTRAINT IF EXISTS fk_line_card_transaction;
ALTER TABLE transaction_line_sepa_transfer DROP CONSTRAINT IF EXISTS fk_line_sepa_transaction;
ALTER TABLE transaction_line_wire_transfer DROP CONSTRAINT IF EXISTS fk_line_wire_transaction;
ALTER TABLE transaction_line_standing_order DROP CONSTRAINT IF EXISTS fk_line_so_transaction;
ALTER TABLE transaction_line_direct_debit DROP CONSTRAINT IF EXISTS fk_line_dd_transaction;
ALTER TABLE transaction_line_deposit DROP CONSTRAINT IF EXISTS fk_line_deposit_transaction;
ALTER TABLE transaction_line_withdrawal DROP CONSTRAINT IF EXISTS fk_line_withdrawal_transaction;
ALTER TABLE transaction_line_transfer DROP CONSTRAINT IF EXISTS fk_line_transfer_transaction;
ALTER TABLE transaction_line_fee DROP CONSTRAINT IF EXISTS fk_line_fee_transaction;
ALTER TABLE transaction_line_interest DROP CONSTRAINT IF EXISTS fk_line_interest_transaction;
ALTER TABLE transaction_line_ach DROP CONSTRAINT IF EXISTS fk_line_ach_transaction;
ALTER TABLE transaction_line_crypto DROP CONSTRAINT IF EXISTS fk_line_crypto_transaction;
ALTER TABLE transaction_line_crypto DROP CONSTRAINT IF EXISTS fk_line_crypto_asset;
ALTER TABLE crypto_asset DROP CONSTRAINT IF EXISTS fk_crypto_asset_blockchain;
ALTER TABLE nft_metadata DROP CONSTRAINT IF EXISTS fk_nft_crypto_asset;
ALTER TABLE money DROP CONSTRAINT IF EXISTS fk_money_crypto_asset;

-- Step 2: Drop old primary key constraints
ALTER TABLE transaction DROP CONSTRAINT IF EXISTS transaction_pkey;
ALTER TABLE transaction_leg DROP CONSTRAINT IF EXISTS transaction_leg_pkey;
ALTER TABLE money DROP CONSTRAINT IF EXISTS money_pkey;
ALTER TABLE statement DROP CONSTRAINT IF EXISTS statement_pkey;
ALTER TABLE transaction_attachment DROP CONSTRAINT IF EXISTS transaction_attachment_pkey;
ALTER TABLE transaction_line_card DROP CONSTRAINT IF EXISTS transaction_line_card_pkey;
ALTER TABLE transaction_line_sepa_transfer DROP CONSTRAINT IF EXISTS transaction_line_sepa_transfer_pkey;
ALTER TABLE transaction_line_wire_transfer DROP CONSTRAINT IF EXISTS transaction_line_wire_transfer_pkey;
ALTER TABLE transaction_line_standing_order DROP CONSTRAINT IF EXISTS transaction_line_standing_order_pkey;
ALTER TABLE transaction_line_direct_debit DROP CONSTRAINT IF EXISTS transaction_line_direct_debit_pkey;
ALTER TABLE transaction_line_deposit DROP CONSTRAINT IF EXISTS transaction_line_deposit_pkey;
ALTER TABLE transaction_line_withdrawal DROP CONSTRAINT IF EXISTS transaction_line_withdrawal_pkey;
ALTER TABLE transaction_line_transfer DROP CONSTRAINT IF EXISTS transaction_line_transfer_pkey;
ALTER TABLE transaction_line_fee DROP CONSTRAINT IF EXISTS transaction_line_fee_pkey;
ALTER TABLE transaction_line_interest DROP CONSTRAINT IF EXISTS transaction_line_interest_pkey;
ALTER TABLE transaction_line_ach DROP CONSTRAINT IF EXISTS transaction_line_ach_pkey;
ALTER TABLE transaction_line_crypto DROP CONSTRAINT IF EXISTS transaction_line_crypto_pkey;
ALTER TABLE blockchain_network DROP CONSTRAINT IF EXISTS blockchain_network_pkey;
ALTER TABLE crypto_asset DROP CONSTRAINT IF EXISTS crypto_asset_pkey;
ALTER TABLE nft_metadata DROP CONSTRAINT IF EXISTS nft_metadata_pkey;

-- Step 3: Add new UUID primary key constraints
ALTER TABLE transaction ADD CONSTRAINT transaction_pkey PRIMARY KEY (transaction_id_uuid);
ALTER TABLE transaction_leg ADD CONSTRAINT transaction_leg_pkey PRIMARY KEY (transaction_leg_id_uuid);
ALTER TABLE money ADD CONSTRAINT money_pkey PRIMARY KEY (money_id_uuid);
ALTER TABLE statement ADD CONSTRAINT statement_pkey PRIMARY KEY (statement_id_uuid);
ALTER TABLE transaction_attachment ADD CONSTRAINT transaction_attachment_pkey PRIMARY KEY (transaction_attachment_id_uuid);
ALTER TABLE transaction_line_card ADD CONSTRAINT transaction_line_card_pkey PRIMARY KEY (transaction_line_card_id_uuid);
ALTER TABLE transaction_line_sepa_transfer ADD CONSTRAINT transaction_line_sepa_transfer_pkey PRIMARY KEY (transaction_line_sepa_id_uuid);
ALTER TABLE transaction_line_wire_transfer ADD CONSTRAINT transaction_line_wire_transfer_pkey PRIMARY KEY (transaction_line_wire_transfer_id_uuid);
ALTER TABLE transaction_line_standing_order ADD CONSTRAINT transaction_line_standing_order_pkey PRIMARY KEY (transaction_line_standing_order_id_uuid);
ALTER TABLE transaction_line_direct_debit ADD CONSTRAINT transaction_line_direct_debit_pkey PRIMARY KEY (transaction_line_direct_debit_id_uuid);
ALTER TABLE transaction_line_deposit ADD CONSTRAINT transaction_line_deposit_pkey PRIMARY KEY (transaction_line_deposit_id_uuid);
ALTER TABLE transaction_line_withdrawal ADD CONSTRAINT transaction_line_withdrawal_pkey PRIMARY KEY (transaction_line_withdrawal_id_uuid);
ALTER TABLE transaction_line_transfer ADD CONSTRAINT transaction_line_transfer_pkey PRIMARY KEY (transaction_line_transfer_id_uuid);
ALTER TABLE transaction_line_fee ADD CONSTRAINT transaction_line_fee_pkey PRIMARY KEY (transaction_line_fee_id_uuid);
ALTER TABLE transaction_line_interest ADD CONSTRAINT transaction_line_interest_pkey PRIMARY KEY (transaction_line_interest_id_uuid);
ALTER TABLE transaction_line_ach ADD CONSTRAINT transaction_line_ach_pkey PRIMARY KEY (transaction_line_ach_id_uuid);
ALTER TABLE transaction_line_crypto ADD CONSTRAINT transaction_line_crypto_pkey PRIMARY KEY (transaction_line_crypto_id_uuid);
ALTER TABLE blockchain_network ADD CONSTRAINT blockchain_network_pkey PRIMARY KEY (blockchain_network_id_uuid);
ALTER TABLE crypto_asset ADD CONSTRAINT crypto_asset_pkey PRIMARY KEY (crypto_asset_id_uuid);
ALTER TABLE nft_metadata ADD CONSTRAINT nft_metadata_pkey PRIMARY KEY (nft_metadata_id_uuid);

-- Step 4: Drop old BIGINT columns (this is destructive!)
-- Core tables
ALTER TABLE transaction DROP COLUMN IF EXISTS transaction_id;
ALTER TABLE transaction DROP COLUMN IF EXISTS related_transaction_id;
ALTER TABLE transaction DROP COLUMN IF EXISTS account_id;
ALTER TABLE transaction DROP COLUMN IF EXISTS account_space_id;
ALTER TABLE transaction DROP COLUMN IF EXISTS transaction_category_id;
ALTER TABLE transaction DROP COLUMN IF EXISTS blockchain_network_id;

ALTER TABLE transaction_leg DROP COLUMN IF EXISTS transaction_leg_id;
ALTER TABLE transaction_leg DROP COLUMN IF EXISTS transaction_id;
ALTER TABLE transaction_leg DROP COLUMN IF EXISTS account_id;
ALTER TABLE transaction_leg DROP COLUMN IF EXISTS account_space_id;

ALTER TABLE money DROP COLUMN IF EXISTS money_id;
ALTER TABLE money DROP COLUMN IF EXISTS crypto_asset_id;

ALTER TABLE statement DROP COLUMN IF EXISTS statement_id;
ALTER TABLE statement DROP COLUMN IF EXISTS account_id;
ALTER TABLE statement DROP COLUMN IF EXISTS account_space_id;

ALTER TABLE transaction_attachment DROP COLUMN IF EXISTS transaction_attachment_id;
ALTER TABLE transaction_attachment DROP COLUMN IF EXISTS transaction_id;

-- Transaction line tables
ALTER TABLE transaction_line_card DROP COLUMN IF EXISTS transaction_line_card_id;
ALTER TABLE transaction_line_card DROP COLUMN IF EXISTS transaction_id;

ALTER TABLE transaction_line_sepa_transfer DROP COLUMN IF EXISTS transaction_line_sepa_id;
ALTER TABLE transaction_line_sepa_transfer DROP COLUMN IF EXISTS transaction_id;

ALTER TABLE transaction_line_wire_transfer DROP COLUMN IF EXISTS transaction_line_wire_transfer_id;
ALTER TABLE transaction_line_wire_transfer DROP COLUMN IF EXISTS transaction_id;

ALTER TABLE transaction_line_standing_order DROP COLUMN IF EXISTS transaction_line_standing_order_id;
ALTER TABLE transaction_line_standing_order DROP COLUMN IF EXISTS transaction_id;

ALTER TABLE transaction_line_direct_debit DROP COLUMN IF EXISTS transaction_line_direct_debit_id;
ALTER TABLE transaction_line_direct_debit DROP COLUMN IF EXISTS transaction_id;

ALTER TABLE transaction_line_deposit DROP COLUMN IF EXISTS transaction_line_deposit_id;
ALTER TABLE transaction_line_deposit DROP COLUMN IF EXISTS transaction_id;

ALTER TABLE transaction_line_withdrawal DROP COLUMN IF EXISTS transaction_line_withdrawal_id;
ALTER TABLE transaction_line_withdrawal DROP COLUMN IF EXISTS transaction_id;

ALTER TABLE transaction_line_transfer DROP COLUMN IF EXISTS transaction_line_transfer_id;
ALTER TABLE transaction_line_transfer DROP COLUMN IF EXISTS transaction_id;
ALTER TABLE transaction_line_transfer DROP COLUMN IF EXISTS transfer_source_account_id;
ALTER TABLE transaction_line_transfer DROP COLUMN IF EXISTS transfer_destination_account_id;

ALTER TABLE transaction_line_fee DROP COLUMN IF EXISTS transaction_line_fee_id;
ALTER TABLE transaction_line_fee DROP COLUMN IF EXISTS transaction_id;

ALTER TABLE transaction_line_interest DROP COLUMN IF EXISTS transaction_line_interest_id;
ALTER TABLE transaction_line_interest DROP COLUMN IF EXISTS transaction_id;

ALTER TABLE transaction_line_ach DROP COLUMN IF EXISTS transaction_line_ach_id;
ALTER TABLE transaction_line_ach DROP COLUMN IF EXISTS transaction_id;
ALTER TABLE transaction_line_ach DROP COLUMN IF EXISTS ach_source_account_id;
ALTER TABLE transaction_line_ach DROP COLUMN IF EXISTS ach_destination_account_id;

-- Crypto/Blockchain tables
ALTER TABLE transaction_line_crypto DROP COLUMN IF EXISTS transaction_line_crypto_id;
ALTER TABLE transaction_line_crypto DROP COLUMN IF EXISTS transaction_id;
ALTER TABLE transaction_line_crypto DROP COLUMN IF EXISTS crypto_asset_id;

ALTER TABLE blockchain_network DROP COLUMN IF EXISTS blockchain_network_id;

ALTER TABLE crypto_asset DROP COLUMN IF EXISTS crypto_asset_id;
ALTER TABLE crypto_asset DROP COLUMN IF EXISTS blockchain_network_id;

ALTER TABLE nft_metadata DROP COLUMN IF EXISTS nft_metadata_id;
ALTER TABLE nft_metadata DROP COLUMN IF EXISTS crypto_asset_id;

-- Step 5: Rename UUID columns to their final names
-- Core tables
ALTER TABLE transaction RENAME COLUMN transaction_id_uuid TO transaction_id;
ALTER TABLE transaction RENAME COLUMN related_transaction_id_uuid TO related_transaction_id;
ALTER TABLE transaction RENAME COLUMN account_id_uuid TO account_id;
ALTER TABLE transaction RENAME COLUMN account_space_id_uuid TO account_space_id;
ALTER TABLE transaction RENAME COLUMN transaction_category_id_uuid TO transaction_category_id;
ALTER TABLE transaction RENAME COLUMN blockchain_network_id_uuid TO blockchain_network_id;

ALTER TABLE transaction_leg RENAME COLUMN transaction_leg_id_uuid TO transaction_leg_id;
ALTER TABLE transaction_leg RENAME COLUMN transaction_id_uuid TO transaction_id;
ALTER TABLE transaction_leg RENAME COLUMN account_id_uuid TO account_id;
ALTER TABLE transaction_leg RENAME COLUMN account_space_id_uuid TO account_space_id;

ALTER TABLE money RENAME COLUMN money_id_uuid TO money_id;
ALTER TABLE money RENAME COLUMN crypto_asset_id_uuid TO crypto_asset_id;

ALTER TABLE statement RENAME COLUMN statement_id_uuid TO statement_id;
ALTER TABLE statement RENAME COLUMN account_id_uuid TO account_id;
ALTER TABLE statement RENAME COLUMN account_space_id_uuid TO account_space_id;

ALTER TABLE transaction_attachment RENAME COLUMN transaction_attachment_id_uuid TO transaction_attachment_id;
ALTER TABLE transaction_attachment RENAME COLUMN transaction_id_uuid TO transaction_id;

-- Transaction line tables
ALTER TABLE transaction_line_card RENAME COLUMN transaction_line_card_id_uuid TO transaction_line_card_id;
ALTER TABLE transaction_line_card RENAME COLUMN transaction_id_uuid TO transaction_id;

ALTER TABLE transaction_line_sepa_transfer RENAME COLUMN transaction_line_sepa_id_uuid TO transaction_line_sepa_id;
ALTER TABLE transaction_line_sepa_transfer RENAME COLUMN transaction_id_uuid TO transaction_id;

ALTER TABLE transaction_line_wire_transfer RENAME COLUMN transaction_line_wire_transfer_id_uuid TO transaction_line_wire_transfer_id;
ALTER TABLE transaction_line_wire_transfer RENAME COLUMN transaction_id_uuid TO transaction_id;

ALTER TABLE transaction_line_standing_order RENAME COLUMN transaction_line_standing_order_id_uuid TO transaction_line_standing_order_id;
ALTER TABLE transaction_line_standing_order RENAME COLUMN transaction_id_uuid TO transaction_id;

ALTER TABLE transaction_line_direct_debit RENAME COLUMN transaction_line_direct_debit_id_uuid TO transaction_line_direct_debit_id;
ALTER TABLE transaction_line_direct_debit RENAME COLUMN transaction_id_uuid TO transaction_id;

ALTER TABLE transaction_line_deposit RENAME COLUMN transaction_line_deposit_id_uuid TO transaction_line_deposit_id;
ALTER TABLE transaction_line_deposit RENAME COLUMN transaction_id_uuid TO transaction_id;

ALTER TABLE transaction_line_withdrawal RENAME COLUMN transaction_line_withdrawal_id_uuid TO transaction_line_withdrawal_id;
ALTER TABLE transaction_line_withdrawal RENAME COLUMN transaction_id_uuid TO transaction_id;

ALTER TABLE transaction_line_transfer RENAME COLUMN transaction_line_transfer_id_uuid TO transaction_line_transfer_id;
ALTER TABLE transaction_line_transfer RENAME COLUMN transaction_id_uuid TO transaction_id;
ALTER TABLE transaction_line_transfer RENAME COLUMN transfer_source_account_id_uuid TO transfer_source_account_id;
ALTER TABLE transaction_line_transfer RENAME COLUMN transfer_destination_account_id_uuid TO transfer_destination_account_id;

ALTER TABLE transaction_line_fee RENAME COLUMN transaction_line_fee_id_uuid TO transaction_line_fee_id;
ALTER TABLE transaction_line_fee RENAME COLUMN transaction_id_uuid TO transaction_id;

ALTER TABLE transaction_line_interest RENAME COLUMN transaction_line_interest_id_uuid TO transaction_line_interest_id;
ALTER TABLE transaction_line_interest RENAME COLUMN transaction_id_uuid TO transaction_id;

ALTER TABLE transaction_line_ach RENAME COLUMN transaction_line_ach_id_uuid TO transaction_line_ach_id;
ALTER TABLE transaction_line_ach RENAME COLUMN transaction_id_uuid TO transaction_id;
ALTER TABLE transaction_line_ach RENAME COLUMN ach_source_account_id_uuid TO ach_source_account_id;
ALTER TABLE transaction_line_ach RENAME COLUMN ach_destination_account_id_uuid TO ach_destination_account_id;

-- Crypto/Blockchain tables
ALTER TABLE transaction_line_crypto RENAME COLUMN transaction_line_crypto_id_uuid TO transaction_line_crypto_id;
ALTER TABLE transaction_line_crypto RENAME COLUMN transaction_id_uuid TO transaction_id;
ALTER TABLE transaction_line_crypto RENAME COLUMN crypto_asset_id_uuid TO crypto_asset_id;

ALTER TABLE blockchain_network RENAME COLUMN blockchain_network_id_uuid TO blockchain_network_id;

ALTER TABLE crypto_asset RENAME COLUMN crypto_asset_id_uuid TO crypto_asset_id;
ALTER TABLE crypto_asset RENAME COLUMN blockchain_network_id_uuid TO blockchain_network_id;

ALTER TABLE nft_metadata RENAME COLUMN nft_metadata_id_uuid TO nft_metadata_id;
ALTER TABLE nft_metadata RENAME COLUMN crypto_asset_id_uuid TO crypto_asset_id;

-- Step 6: Drop mapping tables (no longer needed)
DROP TABLE IF EXISTS transaction_id_mapping;
DROP TABLE IF EXISTS transaction_leg_id_mapping;
DROP TABLE IF EXISTS money_id_mapping;
DROP TABLE IF EXISTS statement_id_mapping;
DROP TABLE IF EXISTS transaction_attachment_id_mapping;
DROP TABLE IF EXISTS transaction_line_card_id_mapping;
DROP TABLE IF EXISTS transaction_line_sepa_id_mapping;
DROP TABLE IF EXISTS transaction_line_wire_id_mapping;
DROP TABLE IF EXISTS transaction_line_standing_order_id_mapping;
DROP TABLE IF EXISTS transaction_line_direct_debit_id_mapping;
DROP TABLE IF EXISTS transaction_line_deposit_id_mapping;
DROP TABLE IF EXISTS transaction_line_withdrawal_id_mapping;
DROP TABLE IF EXISTS transaction_line_transfer_id_mapping;
DROP TABLE IF EXISTS transaction_line_fee_id_mapping;
DROP TABLE IF EXISTS transaction_line_interest_id_mapping;
DROP TABLE IF EXISTS transaction_line_ach_id_mapping;
DROP TABLE IF EXISTS transaction_line_crypto_id_mapping;
DROP TABLE IF EXISTS blockchain_network_id_mapping;
DROP TABLE IF EXISTS crypto_asset_id_mapping;
DROP TABLE IF EXISTS nft_metadata_id_mapping;

-- Step 7: Add final comment
COMMENT ON SCHEMA public IS 'UUID conversion completed - all primary keys and foreign keys are now UUID type';
