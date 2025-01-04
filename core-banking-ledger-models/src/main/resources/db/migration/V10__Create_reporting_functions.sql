-- V10__Create_reporting_functions.sql

-- ===================================================
-- Reporting Functions Implementation
-- ===================================================

-- 1. Create function for daily balance report
CREATE OR REPLACE FUNCTION generate_daily_balance_report(
    p_start_date date,
    p_end_date date,
    p_account_id integer
)
RETURNS TABLE (
    report_date date,
    opening_balance decimal,
    total_credits decimal,
    total_debits decimal,
    closing_balance decimal,
    transaction_count integer
) AS $$
BEGIN
RETURN QUERY
    WITH daily_transactions AS (
        SELECT
            DATE(transaction_date) as txn_date,
            SUM(CASE WHEN total_amount >= 0 THEN total_amount ELSE 0 END) as credits,
            SUM(CASE WHEN total_amount < 0 THEN ABS(total_amount) ELSE 0 END) as debits,
            COUNT(*) as num_transactions
        FROM transaction
        WHERE account_id = p_account_id
        AND DATE(transaction_date) BETWEEN p_start_date AND p_end_date
        GROUP BY DATE(transaction_date)
    ),
    running_balance AS (
        SELECT
            txn_date,
            credits,
            debits,
            num_transactions,
            SUM(credits - debits) OVER (ORDER BY txn_date) as cumulative_balance
        FROM daily_transactions
    )
SELECT
    rb.txn_date,
    LAG(rb.cumulative_balance, 1, 0) OVER (ORDER BY rb.txn_date) as opening_balance,
        rb.credits,
    rb.debits,
    rb.cumulative_balance as closing_balance,
    rb.num_transactions
FROM running_balance rb
ORDER BY rb.txn_date;
END;
$$ LANGUAGE plpgsql;

-- 2. Create function for category spending analysis
CREATE OR REPLACE FUNCTION generate_category_spending_report(
    p_start_date date,
    p_end_date date,
    p_category_type category_type_enum DEFAULT NULL
)
RETURNS TABLE (
    category_name text,
    category_path text,
    total_amount decimal,
    transaction_count integer,
    average_amount decimal,
    percentage_of_total decimal
) AS $$
BEGIN
RETURN QUERY
    WITH category_totals AS (
        SELECT
            ch.category_name,
            ch.full_path,
            SUM(t.total_amount) as total_amount,
            COUNT(*) as transaction_count,
            AVG(t.total_amount) as average_amount
        FROM transaction t
        JOIN mv_category_hierarchy ch ON t.transaction_category_id = ch.transaction_category_id
        WHERE DATE(t.transaction_date) BETWEEN p_start_date AND p_end_date
        AND (p_category_type IS NULL OR ch.category_type = p_category_type)
        GROUP BY ch.category_name, ch.full_path
    )
SELECT
    ct.category_name,
    ct.full_path,
    ct.total_amount,
    ct.transaction_count,
    ct.average_amount,
    ROUND(
            (ct.total_amount / NULLIF(SUM(ct.total_amount) OVER (), 0) * 100)::numeric,
            2
    ) as percentage_of_total
FROM category_totals ct
ORDER BY ct.total_amount DESC;
END;
$$ LANGUAGE plpgsql;

-- 3. Create function for transaction failure analysis
CREATE OR REPLACE FUNCTION generate_failure_analysis_report(
    p_start_date timestamp,
    p_end_date timestamp,
    p_transaction_type transaction_type_enum DEFAULT NULL
)
RETURNS TABLE (
    transaction_type transaction_type_enum,
    total_transactions integer,
    failed_transactions integer,
    failure_rate decimal,
    avg_resolution_time interval,
    common_failure_reasons text[]
) AS $$
BEGIN
RETURN QUERY
SELECT
    t.transaction_type,
    COUNT(*) as total_transactions,
    COUNT(*) FILTER (WHERE t.transaction_status = 'FAILED'::transaction_status_enum) as failed_transactions,
        ROUND(
                (COUNT(*) FILTER (WHERE t.transaction_status = 'FAILED'::transaction_status_enum)::decimal /
             NULLIF(COUNT(*)::decimal, 0) * 100)::numeric,
                2
        ) as failure_rate,
    AVG(
            CASE
                WHEN t.transaction_status = 'FAILED'::transaction_status_enum
                THEN t.date_updated - t.date_created
                END
    ) as avg_resolution_time,
    ARRAY_AGG(DISTINCT tsh.reason) FILTER (WHERE tsh.status_code = 'FAILED'::status_code_enum) as common_failure_reasons
FROM transaction t
         LEFT JOIN transaction_status_history tsh ON t.transaction_id = tsh.transaction_id
WHERE t.transaction_date BETWEEN p_start_date AND p_end_date
  AND (p_transaction_type IS NULL OR t.transaction_type = p_transaction_type)
GROUP BY t.transaction_type;
END;
$$ LANGUAGE plpgsql;

-- 4. Create function for standing order forecast (continued)
CREATE OR REPLACE FUNCTION generate_standing_order_forecast(
    p_forecast_months integer DEFAULT 12
)
RETURNS TABLE (
    forecast_date date,
    standing_order_id text,
    recipient_name text,
    forecast_amount decimal,
    currency char(3),
    frequency standing_order_frequency_enum,
    confidence_score decimal
) AS $$
BEGIN
RETURN QUERY
    WITH RECURSIVE forecast_dates AS (
        SELECT
            generate_series(
                current_date,
                current_date + (p_forecast_months || ' months')::interval,
                '1 day'::interval
            )::date as forecast_date
    )
SELECT DISTINCT
    fd.forecast_date,
    so.standing_order_id,
    so.standing_order_recipient_name,
    t.total_amount as forecast_amount,
    t.currency,
    so.standing_order_frequency,
    CASE
        WHEN so.standing_order_status = 'ACTIVE'::standing_order_status_enum THEN 0.95
        WHEN so.standing_order_status = 'SUSPENDED'::standing_order_status_enum THEN 0.5
        ELSE 0.0
        END as confidence_score
FROM forecast_dates fd
         CROSS JOIN transaction t
         JOIN transaction_line_standing_order so ON t.transaction_id = so.transaction_id
WHERE so.standing_order_status != 'CANCELLED'::standing_order_status_enum
    AND (
        CASE
            WHEN so.standing_order_frequency = 'DAILY'::standing_order_frequency_enum THEN true
            WHEN so.standing_order_frequency = 'WEEKLY'::standing_order_frequency_enum
                THEN EXTRACT(DOW FROM fd.forecast_date) = EXTRACT(DOW FROM so.standing_order_start_date)
            WHEN so.standing_order_frequency = 'MONTHLY'::standing_order_frequency_enum
                THEN EXTRACT(DAY FROM fd.forecast_date) = EXTRACT(DAY FROM so.standing_order_start_date)
            WHEN so.standing_order_frequency = 'YEARLY'::standing_order_frequency_enum
                THEN EXTRACT(MONTH FROM fd.forecast_date) = EXTRACT(MONTH FROM so.standing_order_start_date)
                AND EXTRACT(DAY FROM fd.forecast_date) = EXTRACT(DAY FROM so.standing_order_start_date)
        END
    )
    AND (so.standing_order_end_date IS NULL OR fd.forecast_date <= so.standing_order_end_date)
ORDER BY fd.forecast_date, so.standing_order_id;
END;
$$ LANGUAGE plpgsql;

-- 5. Create function for transaction reconciliation report
CREATE OR REPLACE FUNCTION generate_reconciliation_report(
    p_start_date timestamp,
    p_end_date timestamp,
    p_account_id integer
)
RETURNS TABLE (
    transaction_date timestamp,
    transaction_id integer,
    transaction_type transaction_type_enum,
    description text,
    amount decimal,
    currency char(3),
    status transaction_status_enum,
    reconciliation_status text,
    discrepancy_amount decimal
) AS $$
BEGIN
RETURN QUERY
SELECT
    t.transaction_date,
    t.transaction_id,
    t.transaction_type,
    t.description,
    t.total_amount as amount,
    t.currency,
    t.transaction_status as status,
    CASE
        WHEN t.transaction_status = 'POSTED'::transaction_status_enum THEN 'Reconciled'
        WHEN t.transaction_status = 'PENDING'::transaction_status_enum THEN 'Pending'
        WHEN t.transaction_status = 'FAILED'::transaction_status_enum THEN 'Failed'
        ELSE 'Unreconciled'
        END as reconciliation_status,
    COALESCE(
            CASE
                WHEN t.transaction_type = 'WIRE_TRANSFER'::transaction_type_enum THEN
                    ABS(t.total_amount) - (SELECT wire_fee_amount FROM transaction_line_wire_transfer WHERE transaction_id = t.transaction_id)
                ELSE 0
                END,
            0
    ) as discrepancy_amount
FROM transaction t
WHERE t.account_id = p_account_id
  AND t.transaction_date BETWEEN p_start_date AND p_end_date
ORDER BY t.transaction_date;
END;
$$ LANGUAGE plpgsql;

-- 6. Create function for regulatory reporting
CREATE OR REPLACE FUNCTION generate_regulatory_report(
    p_start_date timestamp,
    p_end_date timestamp,
    p_min_amount decimal DEFAULT 10000 -- Default threshold for large transactions
)
RETURNS TABLE (
    transaction_id integer,
    transaction_date timestamp,
    transaction_type transaction_type_enum,
    amount decimal,
    currency char(3),
    initiating_party text,
    beneficiary_name text,
    country_code char(2),
    high_risk_indicators text[]
) AS $$
BEGIN
RETURN QUERY
SELECT
    t.transaction_id,
    t.transaction_date,
    t.transaction_type,
    t.total_amount,
    t.currency,
    t.initiating_party,
    COALESCE(
            wt.wire_beneficiary_name,
            so.standing_order_recipient_name,
            NULL
    ) as beneficiary_name,
    COALESCE(
            tlc.card_holder_country,
            NULL
    ) as country_code,
    ARRAY_REMOVE(ARRAY[
                     CASE WHEN ABS(t.total_amount) >= p_min_amount THEN 'Large Transaction' END,
                 CASE WHEN t.transaction_type = 'WIRE_TRANSFER'::transaction_type_enum
                     AND wt.wire_transfer_priority = 'HIGH'::wire_transfer_priority_enum
                 THEN 'High Priority Wire' END,
                 CASE WHEN tlc.card_fraud_flag THEN 'Fraud Flag' END
                     ], NULL) as high_risk_indicators
FROM transaction t
         LEFT JOIN transaction_line_wire_transfer wt ON t.transaction_id = wt.transaction_id
         LEFT JOIN transaction_line_standing_order so ON t.transaction_id = so.transaction_id
         LEFT JOIN transaction_line_card tlc ON t.transaction_id = tlc.transaction_id
WHERE t.transaction_date BETWEEN p_start_date AND p_end_date
  AND (
    ABS(t.total_amount) >= p_min_amount
        OR tlc.card_fraud_flag = true
        OR wt.wire_transfer_priority = 'HIGH'::wire_transfer_priority_enum
    )
ORDER BY t.transaction_date;
END;
$$ LANGUAGE plpgsql;