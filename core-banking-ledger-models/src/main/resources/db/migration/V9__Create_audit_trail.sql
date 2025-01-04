-- V9__Create_audit_trail.sql

-- ===================================================
-- Audit Trail Implementation
-- ===================================================

-- 1. Create audit_log table
CREATE TABLE audit_log (
                           audit_id BIGSERIAL PRIMARY KEY,
                           table_name varchar(50) NOT NULL,
                           record_id integer NOT NULL,
                           action_type varchar(20) NOT NULL CHECK (action_type IN ('INSERT', 'UPDATE', 'DELETE')),
                           old_values jsonb,
                           new_values jsonb,
                           changed_fields text[],
                           user_id varchar(50),
                           application_user varchar(100),
                           client_ip inet,
                           user_agent text,
                           action_timestamp timestamp with time zone DEFAULT now(),
                           transaction_id uuid
);

-- 2. Create indexes for audit_log
CREATE INDEX idx_audit_log_table_record ON audit_log(table_name, record_id);
CREATE INDEX idx_audit_log_timestamp ON audit_log(action_timestamp);
CREATE INDEX idx_audit_log_user ON audit_log(user_id);
CREATE INDEX idx_audit_log_transaction ON audit_log(transaction_id);

-- 3. Create audit trigger function
CREATE OR REPLACE FUNCTION process_audit_trail()
RETURNS TRIGGER AS $$
DECLARE
old_row_json jsonb;
    new_row_json jsonb;
    changed_fields_array text[];
    audit_user_id varchar(50);
    audit_app_user varchar(100);
    audit_client_ip inet;
    audit_user_agent text;
BEGIN
    -- Get current user context (implement these functions based on your application context)
    audit_user_id := current_setting('app.current_user_id', true);
    audit_app_user := current_setting('app.current_app_user', true);
    audit_client_ip := current_setting('app.current_client_ip', true);
    audit_user_agent := current_setting('app.current_user_agent', true);

    IF (TG_OP = 'UPDATE') THEN
        old_row_json := to_jsonb(OLD);
        new_row_json := to_jsonb(NEW);
        changed_fields_array := ARRAY(
            SELECT key
            FROM jsonb_each(new_row_json)
            WHERE new_row_json->key IS DISTINCT FROM old_row_json->key
        );

        IF changed_fields_array IS NOT NULL AND array_length(changed_fields_array, 1) > 0 THEN
            INSERT INTO audit_log(
                table_name,
                record_id,
                action_type,
                old_values,
                new_values,
                changed_fields,
                user_id,
                application_user,
                client_ip,
                user_agent,
                transaction_id
            )
            VALUES (
                TG_TABLE_NAME::varchar,
                CASE WHEN TG_TABLE_NAME = 'transaction' THEN OLD.transaction_id
                     ELSE OLD.id END,
                TG_OP,
                old_row_json,
                new_row_json,
                changed_fields_array,
                audit_user_id,
                audit_app_user,
                audit_client_ip,
                audit_user_agent,
                txid_current()::text::uuid
            );
END IF;

    ELSIF (TG_OP = 'DELETE') THEN
        old_row_json := to_jsonb(OLD);

INSERT INTO audit_log(
    table_name,
    record_id,
    action_type,
    old_values,
    user_id,
    application_user,
    client_ip,
    user_agent,
    transaction_id
)
VALUES (
           TG_TABLE_NAME::varchar,
           CASE WHEN TG_TABLE_NAME = 'transaction' THEN OLD.transaction_id
                ELSE OLD.id END,
           TG_OP,
           old_row_json,
           audit_user_id,
           audit_app_user,
           audit_client_ip,
           audit_user_agent,
           txid_current()::text::uuid
       );

ELSIF (TG_OP = 'INSERT') THEN
        new_row_json := to_jsonb(NEW);

INSERT INTO audit_log(
    table_name,
    record_id,
    action_type,
    new_values,
    user_id,
    application_user,
    client_ip,
    user_agent,
    transaction_id
)
VALUES (
           TG_TABLE_NAME::varchar,
           CASE WHEN TG_TABLE_NAME = 'transaction' THEN NEW.transaction_id
                ELSE NEW.id END,
           TG_OP,
           new_row_json,
           audit_user_id,
           audit_app_user,
           audit_client_ip,
           audit_user_agent,
           txid_current()::text::uuid
       );
END IF;

RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- 4. Create audit triggers for all relevant tables
CREATE TRIGGER audit_transaction_trigger
    AFTER INSERT OR UPDATE OR DELETE ON transaction
    FOR EACH ROW EXECUTE FUNCTION process_audit_trail();

CREATE TRIGGER audit_transaction_category_trigger
    AFTER INSERT OR UPDATE OR DELETE ON transaction_category
    FOR EACH ROW EXECUTE FUNCTION process_audit_trail();

CREATE TRIGGER audit_transaction_status_history_trigger
    AFTER INSERT OR UPDATE OR DELETE ON transaction_status_history
    FOR EACH ROW EXECUTE FUNCTION process_audit_trail();

CREATE TRIGGER audit_transaction_line_card_trigger
    AFTER INSERT OR UPDATE OR DELETE ON transaction_line_card
    FOR EACH ROW EXECUTE FUNCTION process_audit_trail();

CREATE TRIGGER audit_transaction_line_direct_debit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON transaction_line_direct_debit
    FOR EACH ROW EXECUTE FUNCTION process_audit_trail();

CREATE TRIGGER audit_transaction_line_sepa_transfer_trigger
    AFTER INSERT OR UPDATE OR DELETE ON transaction_line_sepa_transfer
    FOR EACH ROW EXECUTE FUNCTION process_audit_trail();

CREATE TRIGGER audit_transaction_line_wire_transfer_trigger
    AFTER INSERT OR UPDATE OR DELETE ON transaction_line_wire_transfer
    FOR EACH ROW EXECUTE FUNCTION process_audit_trail();

CREATE TRIGGER audit_transaction_line_standing_order_trigger
    AFTER INSERT OR UPDATE OR DELETE ON transaction_line_standing_order
    FOR EACH ROW EXECUTE FUNCTION process_audit_trail();

-- 5. Create audit trail search function
CREATE OR REPLACE FUNCTION search_audit_trail(
    p_table_name varchar DEFAULT NULL,
    p_record_id integer DEFAULT NULL,
    p_action_type varchar DEFAULT NULL,
    p_user_id varchar DEFAULT NULL,
    p_start_date timestamp DEFAULT NULL,
    p_end_date timestamp DEFAULT NULL,
    p_changed_field text DEFAULT NULL
)
RETURNS TABLE (
    audit_id bigint,
    table_name varchar,
    record_id integer,
    action_type varchar,
    old_values jsonb,
    new_values jsonb,
    changed_fields text[],
    user_id varchar,
    action_timestamp timestamp with time zone
) AS $$
BEGIN
RETURN QUERY
SELECT
    al.audit_id,
    al.table_name,
    al.record_id,
    al.action_type,
    al.old_values,
    al.new_values,
    al.changed_fields,
    al.user_id,
    al.action_timestamp
FROM audit_log al
WHERE
    (p_table_name IS NULL OR al.table_name = p_table_name) AND
    (p_record_id IS NULL OR al.record_id = p_record_id) AND
    (p_action_type IS NULL OR al.action_type = p_action_type) AND
    (p_user_id IS NULL OR al.user_id = p_user_id) AND
    (p_start_date IS NULL OR al.action_timestamp >= p_start_date) AND
    (p_end_date IS NULL OR al.action_timestamp <= p_end_date) AND
    (p_changed_field IS NULL OR p_changed_field = ANY(al.changed_fields))
ORDER BY al.action_timestamp DESC;
END;
$$ LANGUAGE plpgsql;