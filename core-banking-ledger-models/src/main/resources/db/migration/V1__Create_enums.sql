-- V1__Create_enums.sql

-- 1) transaction.transaction_type
CREATE TYPE transaction_type_enum AS ENUM (
    'deposit',
    'withdrawal',
    'transfer',
    'fee',
    'interest',
    'wire_transfer',
    'standing_order'
);

-- 2) transaction.transaction_status
CREATE TYPE transaction_status_enum AS ENUM (
    'pending',
    'posted',
    'reversed',
    'failed'
);

-- 3) transaction_category.category_type
CREATE TYPE category_type_enum AS ENUM (
    'income',
    'expense'
);

-- 4) transaction_status_history.status_code
CREATE TYPE status_code_enum AS ENUM (
    'pending',
    'authorized',
    'posted',
    'reversed',
    'failed'
);

-- 5) transaction_line_direct_debit.direct_debit_sequence_type
CREATE TYPE direct_debit_sequence_type_enum AS ENUM (
    'frst',
    'rcur',
    'fnal',
    'ooff'
);

-- 6) transaction_line_direct_debit.direct_debit_processing_status
CREATE TYPE direct_debit_processing_status_enum AS ENUM (
    'initiated',
    'pending',
    'completed',
    'failed'
);

-- 7) transaction_line_direct_debit.direct_debit_spanish_scheme
CREATE TYPE direct_debit_spanish_scheme_enum AS ENUM (
    'CORE',
    'B2B',
    'COR1'
);

-- 8) transaction_line_sepa_transfer.sepa_transaction_status
CREATE TYPE sepa_transaction_status_enum AS ENUM (
    'accp',
    'rjct',
    'pdng'
);

-- 9) transaction_line_sepa_transfer.sepa_payment_scheme
CREATE TYPE sepa_payment_scheme_enum AS ENUM (
    'CORE',
    'B2B',
    'COR1'
);

-- 10) transaction_line_wire_transfer.wire_transfer_priority
CREATE TYPE wire_transfer_priority_enum AS ENUM (
    'high',
    'normal',
    'low'
);

-- 11) transaction_line_wire_transfer.wire_reception_status
CREATE TYPE wire_reception_status_enum AS ENUM (
    'received',
    'not_received'
);

-- 12) ledger_account.account_type
CREATE TYPE ledger_account_type_enum AS ENUM (
    'ASSET',
    'LIABILITY',
    'EQUITY',
    'INCOME',
    'EXPENSE'
);

-- 13) ledger_entry.debit_credit_indicator
CREATE TYPE debit_credit_indicator_enum AS ENUM (
    'DEBIT',
    'CREDIT'
);

-- 14) transaction_line_standing_order.standing_order_frequency
CREATE TYPE standing_order_frequency_enum AS ENUM (
    'daily',
    'weekly',
    'monthly',
    'yearly'
);

-- 15) transaction_line_standing_order.standing_order_status
CREATE TYPE standing_order_status_enum AS ENUM (
    'active',
    'cancelled',
    'suspended'
);