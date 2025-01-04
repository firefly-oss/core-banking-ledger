-- V4__Create_triggers.sql

-- Function to update date_updated timestamp
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.date_updated = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply the trigger to transaction table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger
        WHERE tgname = 'trg_update_transaction_timestamp'
    ) THEN
CREATE TRIGGER trg_update_transaction_timestamp
    BEFORE UPDATE ON transaction
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();
END IF;
END$$;

-- Apply the trigger to transaction_category table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger
        WHERE tgname = 'trg_update_transaction_category_timestamp'
    ) THEN
CREATE TRIGGER trg_update_transaction_category_timestamp
    BEFORE UPDATE ON transaction_category
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();
END IF;
END$$;

-- Apply the trigger to transaction_status_history table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger
        WHERE tgname = 'trg_update_transaction_status_history_timestamp'
    ) THEN
CREATE TRIGGER trg_update_transaction_status_history_timestamp
    BEFORE UPDATE ON transaction_status_history
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();
END IF;
END$$;

-- Apply the trigger to transaction_line_card table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger
        WHERE tgname = 'trg_update_transaction_line_card_timestamp'
    ) THEN
CREATE TRIGGER trg_update_transaction_line_card_timestamp
    BEFORE UPDATE ON transaction_line_card
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();
END IF;
END$$;

-- Apply the trigger to transaction_line_direct_debit table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger
        WHERE tgname = 'trg_update_transaction_line_direct_debit_timestamp'
    ) THEN
CREATE TRIGGER trg_update_transaction_line_direct_debit_timestamp
    BEFORE UPDATE ON transaction_line_direct_debit
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();
END IF;
END$$;

-- Apply the trigger to transaction_line_sepa_transfer table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger
        WHERE tgname = 'trg_update_transaction_line_sepa_transfer_timestamp'
    ) THEN
CREATE TRIGGER trg_update_transaction_line_sepa_transfer_timestamp
    BEFORE UPDATE ON transaction_line_sepa_transfer
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();
END IF;
END$$;

-- Apply the trigger to transaction_line_wire_transfer table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger
        WHERE tgname = 'trg_update_transaction_line_wire_transfer_timestamp'
    ) THEN
CREATE TRIGGER trg_update_transaction_line_wire_transfer_timestamp
    BEFORE UPDATE ON transaction_line_wire_transfer
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();
END IF;
END$$;

-- Apply the trigger to transaction_line_standing_order table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger
        WHERE tgname = 'trg_update_transaction_line_standing_order_timestamp'
    ) THEN
CREATE TRIGGER trg_update_transaction_line_standing_order_timestamp
    BEFORE UPDATE ON transaction_line_standing_order
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();
END IF;
END$$;