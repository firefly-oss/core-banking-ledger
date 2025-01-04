-- V8__Create_analytics_views.sql

-- ===================================================
-- Transaction Analytics Views
-- ===================================================

-- 1. Monthly Transaction Summary
CREATE MATERIALIZED VIEW mv_monthly_transaction_summary AS
SELECT
    DATE_TRUNC('month', t.transaction_date) as month,
    tc.full_path as category_path,
    t.currency,
    COUNT(*) as transaction_count,
    SUM(t.total_amount) as total_amount,
    AVG(t.total_amount) as average_amount,
    tc.category_type
FROM transaction t
    JOIN mv_category_hierarchy tc ON t.transaction_category_id = tc.transaction_category_id
GROUP BY
    DATE_TRUNC('month', t.transaction_date),
    tc.full_path,
    t.currency,
    tc.category_type;

-- 2. Transaction Status Analysis
CREATE MATERIALIZED VIEW mv_transaction_status_analysis AS
SELECT
    DATE_TRUNC('day', t.transaction_date) as date,
    t.transaction_status,
    t.transaction_type,
    COUNT(*) as status_count,
    AVG(EXTRACT(EPOCH FROM (tsh.status_end_datetime - tsh.status_start_datetime))/3600) as avg_status_duration_hours
FROM transaction t
    LEFT JOIN transaction_status_history tsh ON t.transaction_id = tsh.transaction_id
GROUP BY
    DATE_TRUNC('day', t.transaction_date),
    t.transaction_status,
    t.transaction_type;

-- 3. Wire Transfer Performance
CREATE MATERIALIZED VIEW mv_wire_transfer_analysis AS
SELECT
    DATE_TRUNC('day', t.transaction_date) as date,
    wt.wire_transfer_priority,
    wt.wire_reception_status,
    COUNT(*) as transfer_count,
    AVG(wt.wire_fee_amount) as avg_fee,
    SUM(t.total_amount) as total_amount,
    AVG(EXTRACT(EPOCH FROM (t.date_updated - t.date_created))/3600) as avg_processing_time_hours
FROM transaction t
    JOIN transaction_line_wire_transfer wt ON t.transaction_id = wt.transaction_id
GROUP BY
    DATE_TRUNC('day', t.transaction_date),
    wt.wire_transfer_priority,
    wt.wire_reception_status;

-- 4. Standing Order Analysis
CREATE MATERIALIZED VIEW mv_standing_order_analysis AS
SELECT
    so.standing_order_frequency,
    so.standing_order_status,
    COUNT(*) as order_count,
    SUM(t.total_amount) as total_amount,
    AVG(t.total_amount) as avg_amount,
    MIN(t.total_amount) as min_amount,
    MAX(t.total_amount) as max_amount
FROM transaction t
         JOIN transaction_line_standing_order so ON t.transaction_id = so.transaction_id
GROUP BY
    so.standing_order_frequency,
    so.standing_order_status;

-- 5. Category Distribution
CREATE MATERIALIZED VIEW mv_category_distribution AS
WITH RECURSIVE CategoryDistribution AS (
    SELECT
        tc.transaction_category_id,
        tc.parent_category_id,
        tc.category_name,
        tc.category_type,
        COUNT(t.transaction_id) as transaction_count,
        COALESCE(SUM(t.total_amount), 0) as total_amount
    FROM transaction_category tc
    LEFT JOIN transaction t ON tc.transaction_category_id = t.transaction_category_id
    GROUP BY
        tc.transaction_category_id,
        tc.parent_category_id,
        tc.category_name,
        tc.category_type
)
SELECT
    cd.*,
    h.full_path,
    ROUND(
            (cd.total_amount / NULLIF(SUM(cd.total_amount) OVER (PARTITION BY cd.category_type), 0) * 100)::numeric,
            2
    ) as percentage_of_type
FROM CategoryDistribution cd
         JOIN mv_category_hierarchy h ON cd.transaction_category_id = h.transaction_category_id;

-- Create refresh functions for materialized views
CREATE OR REPLACE FUNCTION refresh_analytics_views()
RETURNS TRIGGER AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_monthly_transaction_summary;
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_transaction_status_analysis;
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_wire_transfer_analysis;
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_standing_order_analysis;
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_category_distribution;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Create triggers to refresh views
CREATE TRIGGER trg_refresh_analytics_views
    AFTER INSERT OR UPDATE OR DELETE ON transaction
    FOR EACH STATEMENT
    EXECUTE FUNCTION refresh_analytics_views();

-- Create indexes for better query performance
CREATE INDEX idx_monthly_summary_month ON mv_monthly_transaction_summary(month);
CREATE INDEX idx_status_analysis_date ON mv_transaction_status_analysis(date);
CREATE INDEX idx_wire_analysis_date ON mv_wire_transfer_analysis(date);
CREATE INDEX idx_category_dist_type ON mv_category_distribution(category_type);