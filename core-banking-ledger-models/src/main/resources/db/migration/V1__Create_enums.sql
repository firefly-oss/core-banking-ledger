-- V1__Create_enums.sql

-- Create ENUM for transaction_type
CREATE TYPE transaction_type_enum AS ENUM (
    'DEPOSIT',
    'WITHDRAWAL',
    'TRANSFER',
    'FEE',
    'INTEREST',
    'WIRE_TRANSFER',
    'STANDING_ORDER'
);

-- Create ENUM for transaction_status
CREATE TYPE transaction_status_enum AS ENUM (
    'PENDING',
    'POSTED',
    'REVERSED',
    'FAILED'
);

-- Create ENUM for category_type
CREATE TYPE category_type_enum AS ENUM (
    'INCOME',
    'EXPENSE'
);

-- Create ENUM for transaction_status_history.status_code
CREATE TYPE status_code_enum AS ENUM (
    'PENDING',
    'AUTHORIZED',
    'POSTED',
    'REVERSED',
    'FAILED'
);

-- Create ENUM for direct_debit_sequence_type
CREATE TYPE direct_debit_sequence_type_enum AS ENUM (
    'FRST',
    'RCUR',
    'FNAL',
    'OOFF'
);

-- Create ENUM for direct_debit_processing_status
CREATE TYPE direct_debit_processing_status_enum AS ENUM (
    'INITIATED',
    'PENDING',
    'COMPLETED',
    'FAILED'
);

-- Create ENUM for sepa_transaction_status
CREATE TYPE sepa_transaction_status_enum AS ENUM (
    'ACCP',
    'RJCT',
    'PDNG'
);

-- Create ENUM for wire_transfer_priority
CREATE TYPE wire_transfer_priority_enum AS ENUM (
    'HIGH',
    'NORMAL',
    'LOW'
);

-- Create ENUM for wire_reception_status
CREATE TYPE wire_reception_status_enum AS ENUM (
    'RECEIVED',
    'NOT_RECEIVED'
);

-- Create ENUM for standing_order_frequency
CREATE TYPE standing_order_frequency_enum AS ENUM (
    'DAILY',
    'WEEKLY',
    'MONTHLY',
    'YEARLY'
);

-- Create ENUM for standing_order_status
CREATE TYPE standing_order_status_enum AS ENUM (
    'ACTIVE',
    'CANCELLED',
    'SUSPENDED'
);