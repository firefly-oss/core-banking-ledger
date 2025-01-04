-- V2__Create_tables.sql

-- Create table: transaction_category
CREATE TABLE IF NOT EXISTS transaction_category (
                                                    transaction_category_id SERIAL PRIMARY KEY,
                                                    parent_category_id INT REFERENCES transaction_category(transaction_category_id) ON DELETE SET NULL,
    category_name VARCHAR(255) NOT NULL,
    category_description TEXT,
    category_type category_type_enum NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Create table: transaction
CREATE TABLE IF NOT EXISTS transaction (
                                           transaction_id SERIAL PRIMARY KEY,
                                           external_reference VARCHAR(255),
    transaction_date TIMESTAMP NOT NULL,
    value_date TIMESTAMP NOT NULL,
    transaction_type transaction_type_enum NOT NULL,
    transaction_status transaction_status_enum NOT NULL,
    total_amount DECIMAL(19,4) NOT NULL,
    currency CHAR(3) NOT NULL,
    description TEXT,
    initiating_party VARCHAR(255),
    account_id INT NOT NULL, -- Ensure Account table exists prior to this migration
    transaction_category_id INT REFERENCES transaction_category(transaction_category_id) ON DELETE SET NULL,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Create table: transaction_status_history
CREATE TABLE IF NOT EXISTS transaction_status_history (
                                                          transaction_status_history_id SERIAL PRIMARY KEY,
                                                          transaction_id INT NOT NULL REFERENCES transaction(transaction_id) ON DELETE CASCADE,
    status_code status_code_enum NOT NULL,
    status_start_datetime TIMESTAMP NOT NULL,
    status_end_datetime TIMESTAMP,
    reason TEXT,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Create table: transaction_line_card
CREATE TABLE IF NOT EXISTS transaction_line_card (
                                                     transaction_line_card_id SERIAL PRIMARY KEY,
                                                     transaction_id INT NOT NULL UNIQUE REFERENCES transaction(transaction_id) ON DELETE CASCADE,
    card_auth_code VARCHAR(100),
    card_merchant_category_code VARCHAR(50),
    card_merchant_name VARCHAR(255),
    card_pos_entry_mode VARCHAR(50),
    card_transaction_reference VARCHAR(255),
    card_terminal_id VARCHAR(100),
    card_holder_country CHAR(2),
    card_present_flag BOOLEAN DEFAULT FALSE,
    card_transaction_timestamp TIMESTAMP,
    card_fraud_flag BOOLEAN DEFAULT FALSE,
    card_currency_conversion_rate DECIMAL(19,6),
    card_fee_amount DECIMAL(19,4),
    card_fee_currency CHAR(3),
    card_installment_plan TEXT,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Create table: transaction_line_direct_debit
CREATE TABLE IF NOT EXISTS transaction_line_direct_debit (
                                                             transaction_line_direct_debit_id SERIAL PRIMARY KEY,
                                                             transaction_id INT NOT NULL UNIQUE REFERENCES transaction(transaction_id) ON DELETE CASCADE,
    direct_debit_mandate_id VARCHAR(255),
    direct_debit_creditor_id VARCHAR(255),
    direct_debit_reference VARCHAR(255),
    direct_debit_sequence_type direct_debit_sequence_type_enum,
    direct_debit_due_date DATE,
    direct_debit_payment_method VARCHAR(100),
    direct_debit_debtor_name VARCHAR(255),
    direct_debit_debtor_address TEXT,
    direct_debit_debtor_contact VARCHAR(255),
    direct_debit_processing_status direct_debit_processing_status_enum,
    direct_debit_authorization_date TIMESTAMP,
    direct_debit_revocation_date TIMESTAMP,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Create table: transaction_line_sepa_transfer
CREATE TABLE IF NOT EXISTS transaction_line_sepa_transfer (
                                                              transaction_line_sepa_id SERIAL PRIMARY KEY,
                                                              transaction_id INT NOT NULL UNIQUE REFERENCES transaction(transaction_id) ON DELETE CASCADE,
    sepa_end_to_end_id VARCHAR(255),
    sepa_remittance_info TEXT,
    sepa_origin_iban VARCHAR(34),
    sepa_origin_bic VARCHAR(11),
    sepa_destination_iban VARCHAR(34),
    sepa_destination_bic VARCHAR(11),
    sepa_transaction_status sepa_transaction_status_enum,
    sepa_creditor_id VARCHAR(255),
    sepa_debtor_id VARCHAR(255),
    sepa_initiating_agent_bic VARCHAR(11),
    sepa_intermediary_bic VARCHAR(11),
    sepa_transaction_purpose TEXT,
    sepa_requested_execution_date DATE,
    sepa_exchange_rate DECIMAL(19,6),
    sepa_fee_amount DECIMAL(19,4),
    sepa_fee_currency CHAR(3),
    sepa_recipient_name VARCHAR(255),
    sepa_recipient_address TEXT,
    sepa_processing_date TIMESTAMP,
    sepa_notes TEXT,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Create table: transaction_line_wire_transfer
CREATE TABLE IF NOT EXISTS transaction_line_wire_transfer (
                                                              transaction_line_wire_transfer_id SERIAL PRIMARY KEY,
                                                              transaction_id INT NOT NULL UNIQUE REFERENCES transaction(transaction_id) ON DELETE CASCADE,
    wire_transfer_reference VARCHAR(255),
    wire_origin_swift_bic VARCHAR(11),
    wire_destination_swift_bic VARCHAR(11),
    wire_origin_account_number VARCHAR(34),
    wire_destination_account_number VARCHAR(34),
    wire_transfer_purpose TEXT,
    wire_transfer_priority wire_transfer_priority_enum,
    wire_exchange_rate DECIMAL(19,6),
    wire_fee_amount DECIMAL(19,4),
    wire_fee_currency CHAR(3),
    wire_instructing_party VARCHAR(255),
    wire_beneficiary_name VARCHAR(255),
    wire_beneficiary_address TEXT,
    wire_processing_date TIMESTAMP,
    wire_transaction_notes TEXT,
    wire_reception_status wire_reception_status_enum,
    wire_decline_reason TEXT,
    wire_cancelled_flag BOOLEAN DEFAULT FALSE,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Create table: transaction_line_standing_order
CREATE TABLE IF NOT EXISTS transaction_line_standing_order (
                                                               transaction_line_standing_order_id SERIAL PRIMARY KEY,
                                                               transaction_id INT NOT NULL UNIQUE REFERENCES transaction(transaction_id) ON DELETE CASCADE,
    standing_order_id VARCHAR(255),
    standing_order_frequency standing_order_frequency_enum,
    standing_order_start_date DATE,
    standing_order_end_date DATE,
    standing_order_next_execution_date DATE,
    standing_order_reference VARCHAR(255),
    standing_order_recipient_name VARCHAR(255),
    standing_order_recipient_iban VARCHAR(34),
    standing_order_recipient_bic VARCHAR(11),
    standing_order_purpose TEXT,
    standing_order_status standing_order_status_enum,
    standing_order_notes TEXT,
    standing_order_last_execution_date DATE,
    standing_order_total_executions INT DEFAULT 0,
    standing_order_cancelled_date TIMESTAMP,
    standing_order_suspended_until_date DATE,
    standing_order_created_by VARCHAR(255),
    standing_order_updated_by VARCHAR(255),
    standing_order_creation_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    standing_order_update_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );