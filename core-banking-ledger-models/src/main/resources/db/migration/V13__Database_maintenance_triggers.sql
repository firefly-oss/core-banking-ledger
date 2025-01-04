-- V13__Create_maintenance_triggers.sql

-- ===================================================
-- Maintenance Triggers
-- ===================================================

-- 1. Create function to track transaction changes
CREATE OR REPLACE FUNCTION track_transaction_changes()
RETURNS TRIGGER AS $$
BEGIN
    -- Update statistics for modified tables
    ANALYZE transaction_partitioned;

    -- Check if we need new partitions
    IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN
        -- Create partitions for next 3 months
        DECLARE
next_month DATE;
BEGIN
FOR i IN 0..2 LOOP
                next_month := date_trunc('month', current_date + (i || ' months')::interval);
                PERFORM create_transaction_partition(next_month);
END LOOP;
END;
END IF;

RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- 2. Create function for automatic cleanup
CREATE OR REPLACE FUNCTION auto_cleanup_trigger()
RETURNS TRIGGER AS $$
DECLARE
cleanup_date TIMESTAMP;
    affected_rows INTEGER;
BEGIN
    -- Run cleanup every 1000 transactions
    IF (TG_OP = 'INSERT' AND
        (SELECT count(*) FROM transaction_partitioned) % 1000 = 0) THEN

        -- Archive old transactions
        cleanup_date := current_timestamp - interval '1 year';
        PERFORM archive_old_transactions(cleanup_date, 1000);

        -- Cleanup audit logs
        PERFORM cleanup_old_audit_logs(365);

        -- Run integrity checks
        PERFORM validate_data_integrity();
END IF;

RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- 3. Create function for statistics updates
CREATE OR REPLACE FUNCTION update_statistics_trigger()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' OR TG_OP = 'DELETE' THEN
        -- Update statistics after every 100 changes
        IF (random() < 0.01) THEN  -- Approximately every 100 operations
            ANALYZE transaction_partitioned;
            ANALYZE transaction_category;
END IF;
END IF;

RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- 4. Create function to monitor partition size
CREATE OR REPLACE FUNCTION monitor_partition_size()
RETURNS TRIGGER AS $$
DECLARE
partition_size BIGINT;
    max_size BIGINT := 1073741824; -- 1GB in bytes
BEGIN
    -- Check partition size after inserts
    IF TG_OP = 'INSERT' THEN
SELECT pg_total_relation_size(TG_TABLE_NAME::regclass) INTO partition_size;

IF partition_size > max_size THEN
            RAISE NOTICE 'Partition % has exceeded size threshold. Current size: % bytes',
                TG_TABLE_NAME, partition_size;

            -- Log the warning
INSERT INTO system_alerts (
    alert_type,
    alert_message,
    alert_timestamp
) VALUES (
             'PARTITION_SIZE_WARNING',
             format('Partition %s has exceeded size threshold. Size: %s bytes',
                    TG_TABLE_NAME, partition_size),
             current_timestamp
         );
END IF;
END IF;

RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- 5. Create system alerts table if not exists
CREATE TABLE IF NOT EXISTS system_alerts (
                                             alert_id SERIAL PRIMARY KEY,
                                             alert_type VARCHAR(50),
    alert_message TEXT,
    alert_timestamp TIMESTAMP WITH TIME ZONE,
                                  processed BOOLEAN DEFAULT FALSE
                                  );

-- 6. Create the triggers
CREATE TRIGGER trg_transaction_changes
    AFTER INSERT OR UPDATE OR DELETE ON transaction_partitioned
    FOR EACH STATEMENT
    EXECUTE FUNCTION track_transaction_changes();

CREATE TRIGGER trg_auto_cleanup
    AFTER INSERT ON transaction_partitioned
    FOR EACH STATEMENT
    EXECUTE FUNCTION auto_cleanup_trigger();

CREATE TRIGGER trg_update_statistics
    AFTER INSERT OR UPDATE OR DELETE ON transaction_partitioned
    FOR EACH STATEMENT
    EXECUTE FUNCTION update_statistics_trigger();

CREATE TRIGGER trg_monitor_partition_size
    AFTER INSERT ON transaction_partitioned
    FOR EACH STATEMENT
    EXECUTE FUNCTION monitor_partition_size();

-- 7. Create view to monitor trigger status
CREATE OR REPLACE VIEW v_trigger_status AS
SELECT
    tgname as trigger_name,
    tgrelid::regclass as table_name,
        tgenabled as trigger_enabled,
    tgtype as trigger_type,
    proname as trigger_function
FROM pg_trigger t
         JOIN pg_proc p ON t.tgfoid = p.oid
WHERE tgrelid::regclass::text LIKE 'transaction_partitioned%';

-- 8. Create function to enable/disable triggers
CREATE OR REPLACE FUNCTION toggle_maintenance_trigger(
    trigger_name text,
    should_enable boolean
)
RETURNS void AS $$
BEGIN
EXECUTE format(
        'ALTER TABLE transaction_partitioned %s TRIGGER %I',
        CASE WHEN should_enable THEN 'ENABLE' ELSE 'DISABLE' END,
        trigger_name
        );
END;
$$ LANGUAGE plpgsql;