-- V3__Create_indexes.sql

-- Indexes for transaction table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction' AND indexname = 'idx_transaction_account_id'
    ) THEN
CREATE INDEX idx_transaction_account_id ON transaction(account_id);
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction' AND indexname = 'idx_transaction_transaction_category_id'
    ) THEN
CREATE INDEX idx_transaction_transaction_category_id ON transaction(transaction_category_id);
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction' AND indexname = 'idx_transaction_transaction_type'
    ) THEN
CREATE INDEX idx_transaction_transaction_type ON transaction(transaction_type);
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction' AND indexname = 'idx_transaction_transaction_status'
    ) THEN
CREATE INDEX idx_transaction_transaction_status ON transaction(transaction_status);
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction' AND indexname = 'idx_transaction_transaction_date'
    ) THEN
CREATE INDEX idx_transaction_transaction_date ON transaction(transaction_date);
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction' AND indexname = 'idx_transaction_value_date'
    ) THEN
CREATE INDEX idx_transaction_value_date ON transaction(value_date);
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction' AND indexname = 'idx_transaction_currency'
    ) THEN
CREATE INDEX idx_transaction_currency ON transaction(currency);
END IF;
END$$;

-- Indexes for transaction_status_history
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction_status_history' AND indexname = 'idx_tsh_transaction_id'
    ) THEN
CREATE INDEX idx_tsh_transaction_id ON transaction_status_history(transaction_id);
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction_status_history' AND indexname = 'idx_tsh_status_code'
    ) THEN
CREATE INDEX idx_tsh_status_code ON transaction_status_history(status_code);
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction_status_history' AND indexname = 'idx_tsh_status_start_datetime'
    ) THEN
CREATE INDEX idx_tsh_status_start_datetime ON transaction_status_history(status_start_datetime);
END IF;
END$$;

-- Indexes for transaction_category
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction_category' AND indexname = 'idx_txn_category_parent_id'
    ) THEN
CREATE INDEX idx_txn_category_parent_id ON transaction_category(parent_category_id);
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction_category' AND indexname = 'idx_txn_category_type'
    ) THEN
CREATE INDEX idx_txn_category_type ON transaction_category(category_type);
END IF;
END$$;

-- Indexes for subtype detail tables
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction_line_card' AND indexname = 'idx_tlc_transaction_id'
    ) THEN
CREATE INDEX idx_tlc_transaction_id ON transaction_line_card(transaction_id);
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction_line_direct_debit' AND indexname = 'idx_tldd_transaction_id'
    ) THEN
CREATE INDEX idx_tldd_transaction_id ON transaction_line_direct_debit(transaction_id);
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction_line_sepa_transfer' AND indexname = 'idx_tls_transfer_transaction_id'
    ) THEN
CREATE INDEX idx_tls_transfer_transaction_id ON transaction_line_sepa_transfer(transaction_id);
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction_line_wire_transfer' AND indexname = 'idx_tlw_transaction_id'
    ) THEN
CREATE INDEX idx_tlw_transaction_id ON transaction_line_wire_transfer(transaction_id);
END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'transaction_line_standing_order' AND indexname = 'idx_tls_order_transaction_id'
    ) THEN
CREATE INDEX idx_tls_order_transaction_id ON transaction_line_standing_order(transaction_id);
END IF;
END$$;