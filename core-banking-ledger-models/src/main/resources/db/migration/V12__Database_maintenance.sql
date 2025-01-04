-- V12__Database_maintenance.sql

-- ===================================================
-- Database Maintenance Functions
-- ===================================================

-- 1. Create function to cleanup old audit logs
CREATE OR REPLACE FUNCTION cleanup_old_audit_logs(
    retention_days integer DEFAULT 365
)
RETURNS integer AS $$
DECLARE
deleted_count integer;
BEGIN
DELETE FROM audit_log
WHERE action_timestamp < current_timestamp - (retention_days || ' days')::interval;
GET DIAGNOSTICS deleted_count = ROW_COUNT;
RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- 2. Create function to archive old transactions
CREATE OR REPLACE FUNCTION archive_old_transactions(
    archive_before_date timestamp,
    batch_size integer DEFAULT 1000
)
RETURNS TABLE (
    archived_count integer,
    archive_status text
) AS $$
DECLARE
v_archived_count integer := 0;
    v_status text := 'Success';
BEGIN
    -- Create archive table if it doesn't exist
CREATE TABLE IF NOT EXISTS transaction_archive (LIKE transaction_partitioned INCLUDING ALL);

-- Archive in batches
WITH to_archive AS (
DELETE FROM transaction_partitioned
WHERE transaction_date < archive_before_date
  AND transaction_status IN ('POSTED', 'FAILED')
    RETURNING *
    )
INSERT INTO transaction_archive
SELECT * FROM to_archive;

GET DIAGNOSTICS v_archived_count = ROW_COUNT;
RETURN QUERY SELECT v_archived_count, v_status;
END;
$$ LANGUAGE plpgsql;

-- 3. Create function to validate data integrity
CREATE OR REPLACE FUNCTION validate_data_integrity()
RETURNS TABLE (
    check_name text,
    check_status text,
    failed_records integer
) AS $$
DECLARE
v_failed_records integer;
BEGIN
    -- Check for orphaned transaction lines
    check_name := 'Orphaned Transaction Lines';
SELECT COUNT(*) INTO v_failed_records
FROM transaction_line_wire_transfer wt
         LEFT JOIN transaction_partitioned t ON wt.transaction_id = t.transaction_id
WHERE t.transaction_id IS NULL;

check_status := CASE WHEN v_failed_records > 0 THEN 'Failed' ELSE 'Passed' END;
    RETURN NEXT;

    -- Check for invalid category hierarchies
    check_name := 'Invalid Category Hierarchy';
WITH RECURSIVE category_check AS (
    SELECT transaction_category_id, parent_category_id, 1 as level
    FROM transaction_category
    WHERE parent_category_id IS NOT NULL
    UNION ALL
    SELECT tc.transaction_category_id, tc.parent_category_id, cc.level + 1
    FROM transaction_category tc
             JOIN category_check cc ON tc.parent_category_id = cc.transaction_category_id
    WHERE cc.level < 10
)
SELECT COUNT(*) INTO v_failed_records
FROM category_check
WHERE level >= 10;

check_status := CASE WHEN v_failed_records > 0 THEN 'Failed' ELSE 'Passed' END;
    RETURN NEXT;

    -- Check for inconsistent transaction amounts
    check_name := 'Inconsistent Transaction Amounts';
SELECT COUNT(*) INTO v_failed_records
FROM transaction_partitioned
WHERE total_amount = 0
   OR total_amount IS NULL;

check_status := CASE WHEN v_failed_records > 0 THEN 'Failed' ELSE 'Passed' END;
    RETURN NEXT;
END;
$$ LANGUAGE plpgsql;

-- 4. Create function to analyze partition usage
CREATE OR REPLACE FUNCTION analyze_partition_usage()
RETURNS TABLE (
    partition_name text,
    total_rows bigint,
    total_size text,
    last_vacuum timestamp,
    last_analyze timestamp,
    bloat_ratio numeric
) AS $$
BEGIN
RETURN QUERY
SELECT
    relname::text,
        n_live_tup::bigint,
        pg_size_pretty(pg_total_relation_size(schemaname || '.' || relname))::text,
        last_vacuum::timestamp,
        last_analyze::timestamp,
        CASE
            WHEN n_live_tup > 0
                THEN round((n_dead_tup::numeric / n_live_tup::numeric) * 100, 2)
            ELSE 0
            END as bloat_ratio
FROM pg_stat_user_tables
WHERE relname LIKE 'transaction_partitioned%'
ORDER BY relname;
END;
$$ LANGUAGE plpgsql;

-- 5. Create monitoring views
CREATE OR REPLACE VIEW v_transaction_metrics AS
SELECT
    date_trunc('hour', transaction_date) as time_bucket,
    transaction_type,
    transaction_status,
    count(*) as transaction_count,
    sum(total_amount) as total_amount,
    avg(total_amount) as avg_amount
FROM transaction_partitioned
GROUP BY
    date_trunc('hour', transaction_date),
    transaction_type,
    transaction_status;