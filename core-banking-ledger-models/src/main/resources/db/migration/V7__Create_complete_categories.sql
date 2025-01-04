-- V7__Create_complete_categories.sql

-- ===================================================
-- Complete Category Structure Implementation
-- ===================================================

-- 1. B2C Income Categories
INSERT INTO transaction_category (
    transaction_category_id,
    parent_category_id,
    category_name,
    category_description,
    category_type,
    date_created,
    date_updated
) VALUES
-- Main B2C Income
(1, NULL, 'Personal Income', 'All personal income sources', 'INCOME'::category_type_enum, now(), now()),

-- Employment Income (10-29)
(10, 1, 'Employment Income', 'Income from employment', 'INCOME'::category_type_enum, now(), now()),
(11, 10, 'Salary', 'Regular salary payments', 'INCOME'::category_type_enum, now(), now()),
(12, 10, 'Bonus', 'Performance and annual bonuses', 'INCOME'::category_type_enum, now(), now()),
(13, 10, 'Commission', 'Sales and performance commissions', 'INCOME'::category_type_enum, now(), now()),
(14, 10, 'Overtime', 'Overtime compensation', 'INCOME'::category_type_enum, now(), now()),
(15, 10, 'Benefits', 'Employment benefits', 'INCOME'::category_type_enum, now(), now()),

-- Investment Income (30-49)
(30, 1, 'Investment Income', 'Income from investments', 'INCOME'::category_type_enum, now(), now()),
(31, 30, 'Dividends', 'Stock dividend payments', 'INCOME'::category_type_enum, now(), now()),
(32, 30, 'Interest Income', 'Interest from investments', 'INCOME'::category_type_enum, now(), now()),
(33, 30, 'Capital Gains', 'Investment sale gains', 'INCOME'::category_type_enum, now(), now()),
(34, 30, 'Rental Income', 'Property rental income', 'INCOME'::category_type_enum, now(), now()),
(35, 30, 'Cryptocurrency Gains', 'Cryptocurrency trading profits', 'INCOME'::category_type_enum, now(), now());

-- 2. B2B Income Categories
INSERT INTO transaction_category (
    transaction_category_id,
    parent_category_id,
    category_name,
    category_description,
    category_type,
    date_created,
    date_updated
) VALUES
-- Main B2B Income
(100, NULL, 'Business Income', 'All business income sources', 'INCOME'::category_type_enum, now(), now()),

-- Service Revenue (110-129)
(110, 100, 'Service Revenue', 'Income from services', 'INCOME'::category_type_enum, now(), now()),
(111, 110, 'Consulting Fees', 'Professional consulting', 'INCOME'::category_type_enum, now(), now()),
(112, 110, 'Maintenance Services', 'Maintenance contracts', 'INCOME'::category_type_enum, now(), now()),
(113, 110, 'Development Services', 'Software development', 'INCOME'::category_type_enum, now(), now()),
(114, 110, 'Training Services', 'Training and education', 'INCOME'::category_type_enum, now(), now()),
(115, 110, 'Subscription Revenue', 'Recurring subscriptions', 'INCOME'::category_type_enum, now(), now()),

-- Product Revenue (130-149)
(130, 100, 'Product Revenue', 'Income from products', 'INCOME'::category_type_enum, now(), now()),
(131, 130, 'Direct Sales', 'Direct product sales', 'INCOME'::category_type_enum, now(), now()),
(132, 130, 'Online Sales', 'E-commerce revenue', 'INCOME'::category_type_enum, now(), now()),
(133, 130, 'Wholesale', 'Wholesale distribution', 'INCOME'::category_type_enum, now(), now()),
(134, 130, 'Software Licenses', 'Software licensing', 'INCOME'::category_type_enum, now(), now()),
(135, 130, 'Hardware Sales', 'Hardware products', 'INCOME'::category_type_enum, now(), now());

-- 3. B2C Expense Categories
INSERT INTO transaction_category (
    transaction_category_id,
    parent_category_id,
    category_name,
    category_description,
    category_type,
    date_created,
    date_updated
) VALUES
-- Main B2C Expenses
(200, NULL, 'Personal Expenses', 'All personal expenses', 'EXPENSE'::category_type_enum, now(), now()),

-- Housing & Utilities (210-229)
(210, 200, 'Housing & Utilities', 'Housing related expenses', 'EXPENSE'::category_type_enum, now(), now()),
(211, 210, 'Rent', 'Rental payments', 'EXPENSE'::category_type_enum, now(), now()),
(212, 210, 'Mortgage', 'Mortgage payments', 'EXPENSE'::category_type_enum, now(), now()),
(213, 210, 'Electricity', 'Electricity bills', 'EXPENSE'::category_type_enum, now(), now()),
(214, 210, 'Water', 'Water bills', 'EXPENSE'::category_type_enum, now(), now()),
(215, 210, 'Gas', 'Gas utility bills', 'EXPENSE'::category_type_enum, now(), now()),
(216, 210, 'Internet', 'Internet service', 'EXPENSE'::category_type_enum, now(), now()),
(217, 210, 'Home Insurance', 'Property insurance', 'EXPENSE'::category_type_enum, now(), now()),

-- Transportation (230-249)
(230, 200, 'Transportation', 'Transport expenses', 'EXPENSE'::category_type_enum, now(), now()),
(231, 230, 'Public Transport', 'Public transit fares', 'EXPENSE'::category_type_enum, now(), now()),
(232, 230, 'Fuel', 'Vehicle fuel', 'EXPENSE'::category_type_enum, now(), now()),
(233, 230, 'Car Insurance', 'Vehicle insurance', 'EXPENSE'::category_type_enum, now(), now()),
(234, 230, 'Car Maintenance', 'Vehicle maintenance', 'EXPENSE'::category_type_enum, now(), now()),
(235, 230, 'Parking', 'Parking fees', 'EXPENSE'::category_type_enum, now(), now()),
(236, 230, 'Ride Share', 'Ride sharing services', 'EXPENSE'::category_type_enum, now(), now()),

-- Daily Living (250-269)
(250, 200, 'Daily Living', 'Daily expenses', 'EXPENSE'::category_type_enum, now(), now()),
(251, 250, 'Groceries', 'Food and household', 'EXPENSE'::category_type_enum, now(), now()),
(252, 250, 'Restaurants', 'Dining out', 'EXPENSE'::category_type_enum, now(), now()),
(253, 250, 'Healthcare', 'Medical expenses', 'EXPENSE'::category_type_enum, now(), now()),
(254, 250, 'Personal Care', 'Grooming and care', 'EXPENSE'::category_type_enum, now(), now()),
(255, 250, 'Clothing', 'Clothing and accessories', 'EXPENSE'::category_type_enum, now(), now()),
(256, 250, 'Entertainment', 'Entertainment activities', 'EXPENSE'::category_type_enum, now(), now());

-- 4. B2B Expense Categories
INSERT INTO transaction_category (
    transaction_category_id,
    parent_category_id,
    category_name,
    category_description,
    category_type,
    date_created,
    date_updated
) VALUES
-- Main B2B Expenses
(300, NULL, 'Business Expenses', 'All business expenses', 'EXPENSE'::category_type_enum, now(), now()),

-- Operating Expenses (310-329)
(310, 300, 'Operating Expenses', 'Day-to-day operations', 'EXPENSE'::category_type_enum, now(), now()),
(311, 310, 'Office Rent', 'Office space rental', 'EXPENSE'::category_type_enum, now(), now()),
(312, 310, 'Office Utilities', 'Utility services', 'EXPENSE'::category_type_enum, now(), now()),
(313, 310, 'Office Supplies', 'General supplies', 'EXPENSE'::category_type_enum, now(), now()),
(314, 310, 'Equipment', 'Office equipment', 'EXPENSE'::category_type_enum, now(), now()),
(315, 310, 'Software Subscriptions', 'Software services', 'EXPENSE'::category_type_enum, now(), now()),
(316, 310, 'Insurance', 'Business insurance', 'EXPENSE'::category_type_enum, now(), now()),

-- Employee Expenses (330-349)
(330, 300, 'Employee Expenses', 'Staff-related costs', 'EXPENSE'::category_type_enum, now(), now()),
(331, 330, 'Salaries', 'Employee salaries', 'EXPENSE'::category_type_enum, now(), now()),
(332, 330, 'Benefits', 'Employee benefits', 'EXPENSE'::category_type_enum, now(), now()),
(333, 330, 'Payroll Taxes', 'Employment taxes', 'EXPENSE'::category_type_enum, now(), now()),
(334, 330, 'Training', 'Employee training', 'EXPENSE'::category_type_enum, now(), now()),
(335, 330, 'Travel', 'Business travel', 'EXPENSE'::category_type_enum, now(), now()),
(336, 330, 'Recruitment', 'Hiring expenses', 'EXPENSE'::category_type_enum, now(), now()),

-- Professional Services (350-369)
(350, 300, 'Professional Services', 'External services', 'EXPENSE'::category_type_enum, now(), now()),
(351, 350, 'Legal Services', 'Legal fees', 'EXPENSE'::category_type_enum, now(), now()),
(352, 350, 'Accounting Services', 'Accounting fees', 'EXPENSE'::category_type_enum, now(), now()),
(353, 350, 'Consulting', 'Consulting fees', 'EXPENSE'::category_type_enum, now(), now()),
(354, 350, 'Marketing Services', 'Marketing and advertising', 'EXPENSE'::category_type_enum, now(), now()),
(355, 350, 'IT Services', 'Technology services', 'EXPENSE'::category_type_enum, now(), now());

-- 5. Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_category_parent_id ON transaction_category(parent_category_id);
CREATE INDEX IF NOT EXISTS idx_category_type ON transaction_category(category_type);

-- 6. Create materialized view for category hierarchy
CREATE MATERIALIZED VIEW mv_category_hierarchy AS
WITH RECURSIVE category_tree AS (
    -- Base case: categories without parents
    SELECT
        transaction_category_id,
        parent_category_id,
        category_name,
        category_type,
        1 as level,
        ARRAY[transaction_category_id] as path,
        category_name::text as full_path
    FROM transaction_category
    WHERE parent_category_id IS NULL

    UNION ALL

    -- Recursive case: categories with parents
    SELECT
        tc.transaction_category_id,
        tc.parent_category_id,
        tc.category_name,
        tc.category_type,
        ct.level + 1,
        ct.path || tc.transaction_category_id,
        (ct.full_path || ' > ' || tc.category_name::text)
    FROM transaction_category tc
    INNER JOIN category_tree ct ON tc.parent_category_id = ct.transaction_category_id
)
SELECT
    transaction_category_id,
    parent_category_id,
    category_name,
    category_type,
    level,
    path,
    full_path
FROM category_tree
ORDER BY path;

-- Create unique index for the materialized view
CREATE UNIQUE INDEX IF NOT EXISTS idx_mv_category_hierarchy_id
    ON mv_category_hierarchy(transaction_category_id);

-- 7. Create function to refresh the materialized view
CREATE OR REPLACE FUNCTION refresh_category_hierarchy()
RETURNS TRIGGER AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY mv_category_hierarchy;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Create trigger to refresh the view when categories change
CREATE TRIGGER trg_refresh_category_hierarchy
    AFTER INSERT OR UPDATE OR DELETE ON transaction_category
    FOR EACH STATEMENT
    EXECUTE FUNCTION refresh_category_hierarchy();