-- V6__Business_rules_and_validations.sql

-- ===================================================
-- Business Rules and Validations
-- ===================================================

-- 1. Transaction Amount Validations
CREATE OR REPLACE FUNCTION validate_transaction_amount()
RETURNS TRIGGER AS $$
BEGIN
    -- Validate minimum transaction amount based on transaction type
CASE NEW.transaction_type
        WHEN 'WIRE_TRANSFER'::transaction_type_enum THEN
            IF ABS(NEW.total_amount) < 1 THEN
                RAISE EXCEPTION 'Wire transfers must have a minimum amount of 1';
END IF;
WHEN 'STANDING_ORDER'::transaction_type_enum THEN
            IF ABS(NEW.total_amount) < 0.01 THEN
                RAISE EXCEPTION 'Standing orders must have a minimum amount of 0.01';
END IF;
END CASE;

    -- Validate currency matches for related transaction lines
    IF NEW.transaction_type = 'WIRE_TRANSFER'::transaction_type_enum THEN
        IF EXISTS (
            SELECT 1 FROM transaction_line_wire_transfer wt
            WHERE wt.transaction_id = NEW.transaction_id
            AND wt.wire_fee_currency != NEW.currency
        ) THEN
            RAISE EXCEPTION 'Wire transfer fee currency must match transaction currency';
END IF;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_transaction_amount
    BEFORE INSERT OR UPDATE ON transaction
                         FOR EACH ROW
                         EXECUTE FUNCTION validate_transaction_amount();

-- 2. Transaction Status Flow Validation
CREATE OR REPLACE FUNCTION validate_transaction_status_flow()
RETURNS TRIGGER AS $$
BEGIN
    -- Define valid status transitions
    IF TG_OP = 'UPDATE' THEN
        -- Prevent status changes for completed transactions after 24 hours
        IF OLD.transaction_status = 'POSTED'::transaction_status_enum
           AND OLD.date_updated < (now() - interval '24 hours') THEN
            RAISE EXCEPTION 'Cannot modify transactions older than 24 hours';
END IF;

        -- Validate status transitions
        IF NOT (
            (OLD.transaction_status = 'PENDING'::transaction_status_enum AND
             NEW.transaction_status IN ('POSTED'::transaction_status_enum, 'FAILED'::transaction_status_enum))
            OR
            (OLD.transaction_status = 'POSTED'::transaction_status_enum AND
             NEW.transaction_status = 'REVERSED'::transaction_status_enum)
        ) THEN
            RAISE EXCEPTION 'Invalid status transition from % to %', OLD.transaction_status, NEW.transaction_status;
END IF;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_transaction_status_flow
    BEFORE UPDATE ON transaction
    FOR EACH ROW
    EXECUTE FUNCTION validate_transaction_status_flow();

-- 3. Standing Order Validation Rules
CREATE OR REPLACE FUNCTION validate_standing_order()
RETURNS TRIGGER AS $$
BEGIN
    -- Validate frequency and execution dates
    IF NEW.standing_order_frequency = 'DAILY'::standing_order_frequency_enum
       AND NEW.standing_order_next_execution_date > (NEW.standing_order_start_date + interval '1 day') THEN
        RAISE EXCEPTION 'Next execution date for daily standing order cannot be more than 1 day after start date';
END IF;

    -- Validate end date for final payments
    IF NEW.standing_order_status = 'CANCELLED'::standing_order_status_enum
       AND NEW.standing_order_end_date IS NULL THEN
        NEW.standing_order_end_date := now();
END IF;

    -- Ensure suspended orders have a valid suspended until date
    IF NEW.standing_order_status = 'SUSPENDED'::standing_order_status_enum
       AND NEW.standing_order_suspended_until_date IS NULL THEN
        RAISE EXCEPTION 'Suspended standing orders must have a suspended until date';
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_standing_order
    BEFORE INSERT OR UPDATE ON transaction_line_standing_order
                         FOR EACH ROW
                         EXECUTE FUNCTION validate_standing_order();

-- 4. Direct Debit Sequence Validation
CREATE OR REPLACE FUNCTION validate_direct_debit_sequence()
RETURNS TRIGGER AS $$
BEGIN
    -- Validate sequence type transitions
    IF TG_OP = 'UPDATE' THEN
        -- FRST can only be followed by RCUR or FNAL
        IF OLD.direct_debit_sequence_type = 'FRST'::direct_debit_sequence_type_enum
           AND NEW.direct_debit_sequence_type NOT IN ('RCUR'::direct_debit_sequence_type_enum, 'FNAL'::direct_debit_sequence_type_enum) THEN
            RAISE EXCEPTION 'Invalid direct debit sequence transition';
END IF;

        -- FNAL cannot be changed
        IF OLD.direct_debit_sequence_type = 'FNAL'::direct_debit_sequence_type_enum THEN
            RAISE EXCEPTION 'Cannot modify final direct debit sequence';
END IF;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_direct_debit_sequence
    BEFORE UPDATE ON transaction_line_direct_debit
    FOR EACH ROW
    EXECUTE FUNCTION validate_direct_debit_sequence();

-- 5. SEPA Transfer Validation
CREATE OR REPLACE FUNCTION validate_sepa_transfer()
RETURNS TRIGGER AS $$
DECLARE
v_iban_regex text := '^[A-Z]{2}[0-9]{2}[A-Z0-9]{4}[0-9]{7}([A-Z0-9]?){0,16}$';
    v_bic_regex text := '^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$';
BEGIN
    -- Validate IBAN format
    IF NEW.sepa_destination_iban IS NOT NULL AND NEW.sepa_destination_iban !~ v_iban_regex THEN
        RAISE EXCEPTION 'Invalid SEPA destination IBAN format';
END IF;

    -- Validate BIC format
    IF NEW.sepa_destination_bic IS NOT NULL AND NEW.sepa_destination_bic !~ v_bic_regex THEN
        RAISE EXCEPTION 'Invalid SEPA destination BIC format';
END IF;

    -- Validate status transitions
    IF TG_OP = 'UPDATE' THEN
        IF OLD.sepa_transaction_status = 'RJCT'::sepa_transaction_status_enum THEN
            RAISE EXCEPTION 'Cannot modify rejected SEPA transfer';
END IF;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_sepa_transfer
    BEFORE INSERT OR UPDATE ON transaction_line_sepa_transfer
                         FOR EACH ROW
                         EXECUTE FUNCTION validate_sepa_transfer();

-- 6. Transaction Category Validation
CREATE OR REPLACE FUNCTION validate_transaction_category()
RETURNS TRIGGER AS $$
DECLARE
parent_type category_type_enum;
    has_circular_ref boolean;
BEGIN
    -- Check category type changes
    IF (TG_OP = 'UPDATE') THEN
        IF (NEW.category_type != OLD.category_type) AND
           EXISTS (SELECT 1 FROM transaction WHERE transaction_category_id = NEW.transaction_category_id) THEN
            RAISE EXCEPTION 'Cannot change category type while transactions exist';
END IF;
END IF;

    -- Parent category validations
    IF NEW.parent_category_id IS NOT NULL THEN
        -- Check if parent exists
SELECT category_type INTO parent_type
FROM transaction_category
WHERE transaction_category_id = NEW.parent_category_id;

IF parent_type IS NULL THEN
            RAISE EXCEPTION 'Parent category does not exist';
END IF;

        -- Check category type match
        IF NEW.category_type != parent_type THEN
            RAISE EXCEPTION 'Parent category must have same category type';
END IF;

        -- Check circular reference
WITH RECURSIVE category_tree AS (
    -- Base case
    SELECT transaction_category_id, parent_category_id, 1 as level
    FROM transaction_category
    WHERE transaction_category_id = NEW.parent_category_id

    UNION ALL

    -- Recursive case
    SELECT tc.transaction_category_id, tc.parent_category_id, ct.level + 1
    FROM transaction_category tc
             INNER JOIN category_tree ct ON tc.transaction_category_id = ct.parent_category_id
    WHERE ct.level < 10
)
SELECT EXISTS (
    SELECT 1 FROM category_tree
    WHERE parent_category_id = NEW.transaction_category_id
) INTO has_circular_ref;

IF has_circular_ref THEN
            RAISE EXCEPTION 'Circular reference detected in category hierarchy';
END IF;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 7. Wire Transfer Additional Validations
CREATE OR REPLACE FUNCTION validate_wire_transfer()
RETURNS TRIGGER AS $$
DECLARE
v_swift_regex text := '^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$';
BEGIN
    -- Validate SWIFT/BIC format
    IF NEW.wire_origin_swift_bic !~ v_swift_regex OR NEW.wire_destination_swift_bic !~ v_swift_regex THEN
        RAISE EXCEPTION 'Invalid SWIFT/BIC format';
END IF;

    -- High-value transfer validations
    IF (SELECT ABS(total_amount) FROM transaction WHERE transaction_id = NEW.transaction_id) >= 10000 THEN
        IF NEW.wire_beneficiary_name IS NULL OR NEW.wire_beneficiary_address IS NULL THEN
            RAISE EXCEPTION 'Beneficiary details required for transfers >= 10000';
END IF;

        IF NEW.wire_transfer_purpose IS NULL THEN
            RAISE EXCEPTION 'Transfer purpose required for transfers >= 10000';
END IF;
END IF;

    -- Validate priority based on amount
    IF NEW.wire_transfer_priority = 'HIGH'::wire_transfer_priority_enum AND
       (SELECT ABS(total_amount) FROM transaction WHERE transaction_id = NEW.transaction_id) < 5000 THEN
        RAISE EXCEPTION 'High priority only available for transfers >= 5000';
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_wire_transfer
    BEFORE INSERT OR UPDATE ON transaction_line_wire_transfer
                         FOR EACH ROW
                         EXECUTE FUNCTION validate_wire_transfer();