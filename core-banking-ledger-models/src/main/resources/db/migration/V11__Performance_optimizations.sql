-- V11__Performance_optimizations.sql

-- ===================================================
-- Performance Optimizations
-- ===================================================

-- 1. Create Statistics Extension
CREATE EXTENSION IF NOT EXISTS pg_stat_statements;

-- 2. Create Transaction Sequences
CREATE SEQUENCE IF NOT EXISTS transaction_partitioned_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;

-- 3. Create Partitioned Table
CREATE TABLE transaction_partitioned (
                                         transaction_id INTEGER DEFAULT nextval('transaction_partitioned_id_seq'),
                                         external_reference VARCHAR(255),
                                         transaction_date TIMESTAMP NOT NULL,
                                         value_date TIMESTAMP,
                                         transaction_type transaction_type_enum,
                                         transaction_status transaction_status_enum,
                                         total_amount DECIMAL,
                                         currency CHAR(3),
                                         description TEXT,
                                         initiating_party VARCHAR(255),
                                         account_id INTEGER,
                                         transaction_category_id INTEGER,
                                         date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         date_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         CONSTRAINT pk_transaction_partitioned PRIMARY KEY(transaction_id, transaction_date)
) PARTITION BY RANGE (transaction_date);

-- 4. Create Initial Partitions with Storage Parameters
DO $$
DECLARE
partition_name text;
BEGIN
FOR y IN 2024..2025 LOOP
        FOR m IN 1..12 LOOP
            partition_name := format('transaction_partitioned_y%sm%s',
                y,
                LPAD(m::text, 2, '0'));

EXECUTE format(
        'CREATE TABLE IF NOT EXISTS %I PARTITION OF transaction_partitioned
         FOR VALUES FROM (%L) TO (%L)',
        partition_name,
        TO_DATE(y || '-' || m || '-01', 'YYYY-MM-DD'),
        TO_DATE(y || '-' || m || '-01', 'YYYY-MM-DD') + INTERVAL '1 month'
        );

-- Set storage parameters for each partition
EXECUTE format(
        'ALTER TABLE %I SET (
            autovacuum_vacuum_scale_factor = 0.05,
            autovacuum_analyze_scale_factor = 0.02,
            autovacuum_vacuum_threshold = 50,
            autovacuum_analyze_threshold = 50
        )',
        partition_name
        );
END LOOP;
END LOOP;
END $$;

-- 5. Create Partition Management Functions
CREATE OR REPLACE FUNCTION create_transaction_partition(
    start_date DATE
)
RETURNS void AS $$
DECLARE
partition_name TEXT;
    start_timestamp TIMESTAMP;
    end_timestamp TIMESTAMP;
BEGIN
    partition_name := 'transaction_partitioned_y' ||
                     to_char(start_date, 'YYYY') ||
                     'm' || to_char(start_date, 'MM');
    start_timestamp := start_date::TIMESTAMP;
    end_timestamp := (start_date + INTERVAL '1 month')::TIMESTAMP;

EXECUTE format(
        'CREATE TABLE IF NOT EXISTS %I PARTITION OF transaction_partitioned
         FOR VALUES FROM (%L) TO (%L)',
        partition_name,
        start_timestamp,
        end_timestamp
        );

-- Set storage parameters for new partition
EXECUTE format(
        'ALTER TABLE %I SET (
            autovacuum_vacuum_scale_factor = 0.05,
            autovacuum_analyze_scale_factor = 0.02,
            autovacuum_vacuum_threshold = 50,
            autovacuum_analyze_threshold = 50
        )',
        partition_name
        );
END;
$$ LANGUAGE plpgsql;

-- 6. Create Indexes on Partitioned Table
CREATE INDEX idx_transaction_date ON transaction_partitioned (transaction_date);

CREATE INDEX idx_transaction_search_partitioned ON transaction_partitioned (
                                                                            transaction_date,
                                                                            account_id,
                                                                            transaction_status
    ) INCLUDE (total_amount, currency);

CREATE INDEX idx_transaction_type_partitioned ON transaction_partitioned (
                                                                          transaction_type,
                                                                          transaction_date
    ) INCLUDE (total_amount);

CREATE INDEX idx_transaction_category_partitioned ON transaction_partitioned (
                                                                              transaction_category_id,
                                                                              transaction_date
    ) INCLUDE (total_amount);

CREATE INDEX idx_transaction_status_partitioned ON transaction_partitioned (
                                                                            transaction_status,
                                                                            transaction_date
    ) WHERE transaction_status != 'POSTED';

-- 7. Create Performance Monitoring Views
CREATE OR REPLACE VIEW v_transaction_performance AS
SELECT
    schemaname,
    relname,
    seq_scan,
    seq_tup_read,
    idx_scan,
    idx_tup_fetch,
    n_tup_ins,
    n_tup_upd,
    n_tup_del,
    n_live_tup,
    n_dead_tup,
    last_vacuum,
    last_autovacuum,
    last_analyze,
    last_autoanalyze
FROM pg_stat_user_tables
WHERE schemaname = 'public'
  AND relname LIKE 'transaction%';

-- 8. Create Statistics for Query Optimization
CREATE STATISTICS transaction_extended_stats (dependencies) ON
    transaction_date, transaction_type, transaction_status
FROM transaction_partitioned;

-- 9. Create Partition Management Functions
CREATE OR REPLACE FUNCTION manage_transaction_partitions()
RETURNS void AS $$
DECLARE
next_month DATE;
BEGIN
FOR i IN 0..3 LOOP
        next_month := date_trunc('month', current_date + (i || ' months')::interval);
        PERFORM create_transaction_partition(next_month);
END LOOP;
END;
$$ LANGUAGE plpgsql;

-- 10. Create Function for Real-time Monitoring
CREATE OR REPLACE FUNCTION get_partition_stats()
RETURNS TABLE (
    partition_name TEXT,
    total_rows BIGINT,
    total_size TEXT,
    last_vacuum TIMESTAMP,
    last_analyze TIMESTAMP
) AS $$
BEGIN
RETURN QUERY
SELECT
    relname::TEXT as partition_name,
        n_live_tup::BIGINT as total_rows,
        pg_size_pretty(pg_total_relation_size(schemaname || '.' || relname))::TEXT as total_size,
        last_vacuum::TIMESTAMP,
        last_analyze::TIMESTAMP
FROM pg_stat_user_tables
WHERE relname LIKE 'transaction_partitioned%'
ORDER BY relname;
END;
$$ LANGUAGE plpgsql;