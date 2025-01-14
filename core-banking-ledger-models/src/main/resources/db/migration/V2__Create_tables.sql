-- V2__Create_tables.sql

-- =============================================
-- TRANSACTION_CATEGORY (Master Table)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_category (
                                                    transaction_category_id BIGINT NOT NULL PRIMARY KEY,
                                                    parent_category_id      BIGINT,  -- Self-referencing hierarchy
                                                    category_name           VARCHAR(100) NOT NULL,
    category_description    VARCHAR(255),
    category_type           category_type_enum NOT NULL,
    date_created            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Spanish-specific
    spanish_tax_code        VARCHAR(50),

    CONSTRAINT fk_category_parent
    FOREIGN KEY (parent_category_id) REFERENCES transaction_category (transaction_category_id)
    );

-- =============================================
-- TRANSACTION (Base Entity)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction (
                                           transaction_id          BIGINT NOT NULL PRIMARY KEY,
                                           external_reference      VARCHAR(100),
    transaction_date        TIMESTAMP NOT NULL,
    value_date              TIMESTAMP,
    transaction_type        transaction_type_enum NOT NULL,
    transaction_status      transaction_status_enum NOT NULL,
    total_amount            DECIMAL(18,2) NOT NULL,
    currency                CHAR(3) NOT NULL,   -- e.g., 'EUR', 'USD'
    description             VARCHAR(255),
    initiating_party        VARCHAR(100),
    account_id              BIGINT,
    transaction_category_id BIGINT,
    date_created            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Spanish-specific
    branch_office_code      VARCHAR(10),
    nif_initiating_party    VARCHAR(20),

    CONSTRAINT fk_transaction_category
    FOREIGN KEY (transaction_category_id) REFERENCES transaction_category (transaction_category_id)
    );

-- =============================================
-- TRANSACTION_STATUS_HISTORY
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_status_history (
                                                          transaction_status_history_id BIGINT NOT NULL PRIMARY KEY,
                                                          transaction_id                BIGINT NOT NULL,
                                                          status_code                   status_code_enum NOT NULL,
                                                          status_start_datetime         TIMESTAMP NOT NULL,
                                                          status_end_datetime           TIMESTAMP,
                                                          reason                        VARCHAR(255),
    date_created                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Spanish-specific
    regulated_reporting_flag      BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_status_history_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
    );

-- =============================================
-- TRANSACTION_LINE_CARD (Subtype Detail)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_line_card (
                                                     transaction_line_card_id   BIGINT NOT NULL PRIMARY KEY,
                                                     transaction_id             BIGINT NOT NULL,
                                                     card_auth_code             VARCHAR(50),
    card_merchant_category_code VARCHAR(10),
    card_merchant_name         VARCHAR(100),
    card_pos_entry_mode        VARCHAR(50),
    card_transaction_reference VARCHAR(100),
    card_terminal_id           VARCHAR(50),
    card_holder_country        CHAR(2),
    card_present_flag          BOOLEAN NOT NULL DEFAULT FALSE,
    card_transaction_timestamp TIMESTAMP,
    card_fraud_flag            BOOLEAN NOT NULL DEFAULT FALSE,
    card_currency_conversion_rate DECIMAL(18,4),
    card_fee_amount            DECIMAL(18,2),
    card_fee_currency          CHAR(3),
    card_installment_plan      VARCHAR(100),
    date_created               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Spanish-specific
    card_merchant_cif          VARCHAR(20),

    CONSTRAINT fk_line_card_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
    );

-- =============================================
-- TRANSACTION_LINE_DIRECT_DEBIT (Subtype Detail)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_line_direct_debit (
                                                             transaction_line_direct_debit_id BIGINT NOT NULL PRIMARY KEY,
                                                             transaction_id                   BIGINT NOT NULL,
                                                             direct_debit_mandate_id         VARCHAR(100),
    direct_debit_creditor_id        VARCHAR(100),
    direct_debit_reference          VARCHAR(100),
    direct_debit_sequence_type      direct_debit_sequence_type_enum,
    direct_debit_due_date           DATE,
    direct_debit_payment_method     VARCHAR(50),
    direct_debit_debtor_name        VARCHAR(100),
    direct_debit_debtor_address     VARCHAR(255),
    direct_debit_debtor_contact     VARCHAR(100),
    direct_debit_processing_status  direct_debit_processing_status_enum,
    direct_debit_authorization_date TIMESTAMP,
    direct_debit_revocation_date    TIMESTAMP,
    date_created                    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated                    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Spanish-specific
    direct_debit_spanish_scheme     direct_debit_spanish_scheme_enum,

    CONSTRAINT fk_line_dd_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
    );

-- =============================================
-- TRANSACTION_LINE_SEPA_TRANSFER (Subtype Detail)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_line_sepa_transfer (
                                                              transaction_line_sepa_id       BIGINT NOT NULL PRIMARY KEY,
                                                              transaction_id                 BIGINT NOT NULL,
                                                              sepa_end_to_end_id             VARCHAR(100),
    sepa_remittance_info           VARCHAR(255),
    sepa_origin_iban               VARCHAR(34),
    sepa_origin_bic                VARCHAR(11),
    sepa_destination_iban          VARCHAR(34),
    sepa_destination_bic           VARCHAR(11),
    sepa_transaction_status        sepa_transaction_status_enum,
    sepa_creditor_id               VARCHAR(100),
    sepa_debtor_id                 VARCHAR(100),
    sepa_initiating_agent_bic      VARCHAR(11),
    sepa_intermediary_bic          VARCHAR(11),
    sepa_transaction_purpose       VARCHAR(255),
    sepa_requested_execution_date  DATE,
    sepa_exchange_rate             DECIMAL(18,4),
    sepa_fee_amount                DECIMAL(18,2),
    sepa_fee_currency              CHAR(3),
    sepa_recipient_name            VARCHAR(100),
    sepa_recipient_address         VARCHAR(255),
    sepa_processing_date           TIMESTAMP,
    sepa_notes                     VARCHAR(255),
    date_created                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Spanish-specific
    sepa_payment_scheme            sepa_payment_scheme_enum,

    CONSTRAINT fk_line_sepa_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
    );

-- =============================================
-- TRANSACTION_LINE_WIRE_TRANSFER (Subtype Detail)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_line_wire_transfer (
                                                              transaction_line_wire_transfer_id BIGINT NOT NULL PRIMARY KEY,
                                                              transaction_id                    BIGINT NOT NULL,
                                                              wire_transfer_reference           VARCHAR(100),
    wire_origin_swift_bic             VARCHAR(11),
    wire_destination_swift_bic        VARCHAR(11),
    wire_origin_account_number        VARCHAR(34),
    wire_destination_account_number   VARCHAR(34),
    wire_transfer_purpose             VARCHAR(255),
    wire_transfer_priority            wire_transfer_priority_enum,
    wire_exchange_rate                DECIMAL(18,4),
    wire_fee_amount                   DECIMAL(18,2),
    wire_fee_currency                 CHAR(3),
    wire_instructing_party            VARCHAR(100),
    wire_beneficiary_name             VARCHAR(100),
    wire_beneficiary_address          VARCHAR(255),
    wire_processing_date              TIMESTAMP,
    wire_transaction_notes            VARCHAR(255),
    wire_reception_status             wire_reception_status_enum,
    wire_decline_reason               VARCHAR(255),
    wire_cancelled_flag               BOOLEAN NOT NULL DEFAULT FALSE,
    date_created                      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated                      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Spanish-specific
    bank_of_spain_reg_code            VARCHAR(50),

    CONSTRAINT fk_line_wire_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
    );

-- =============================================
-- TRANSACTION_LINE_STANDING_ORDER (Subtype Detail)
-- =============================================
CREATE TABLE IF NOT EXISTS transaction_line_standing_order (
                                                               transaction_line_standing_order_id BIGINT NOT NULL PRIMARY KEY,
                                                               transaction_id                     BIGINT NOT NULL,
                                                               standing_order_id                  VARCHAR(100),
    standing_order_frequency           standing_order_frequency_enum,
    standing_order_start_date          DATE,
    standing_order_end_date            DATE,
    standing_order_next_execution_date DATE,
    standing_order_reference           VARCHAR(100),
    standing_order_recipient_name      VARCHAR(100),
    standing_order_recipient_iban      VARCHAR(34),
    standing_order_recipient_bic       VARCHAR(11),
    standing_order_purpose             VARCHAR(255),
    standing_order_status              standing_order_status_enum,
    standing_order_notes               VARCHAR(255),
    standing_order_last_execution_date DATE,
    standing_order_total_executions    BIGINT NOT NULL DEFAULT 0,
    standing_order_cancelled_date      TIMESTAMP,
    standing_order_suspended_until_date DATE,
    standing_order_created_by          VARCHAR(50),
    standing_order_updated_by          VARCHAR(50),
    standing_order_creation_timestamp  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    standing_order_update_timestamp    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Spanish-specific
    standing_order_spanish_tax_flag    BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_line_so_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
    );

-- =============================================
-- LEDGER_ACCOUNT (Chart of Accounts)
-- =============================================
CREATE TABLE IF NOT EXISTS ledger_account (
                                              ledger_account_id   BIGINT NOT NULL PRIMARY KEY,
                                              account_code        VARCHAR(50) NOT NULL,
    account_name        VARCHAR(100) NOT NULL,
    account_type        ledger_account_type_enum NOT NULL,
    parent_account_id   BIGINT,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    date_created        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ledger_parent
    FOREIGN KEY (parent_account_id) REFERENCES ledger_account (ledger_account_id)
    );

-- =============================================
-- LEDGER_ENTRY (Double-Entry Postings)
-- =============================================
CREATE TABLE IF NOT EXISTS ledger_entry (
                                            ledger_entry_id        BIGINT NOT NULL PRIMARY KEY,
                                            transaction_id         BIGINT NOT NULL,
                                            ledger_account_id      BIGINT NOT NULL,
                                            debit_credit_indicator debit_credit_indicator_enum NOT NULL,
                                            amount                 DECIMAL(18,2) NOT NULL,
    currency               CHAR(3) NOT NULL,
    posting_date           TIMESTAMP NOT NULL,
    exchange_rate          DECIMAL(18,4),
    cost_center_id         BIGINT,
    notes                  VARCHAR(255),
    date_created           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_entry_transaction
    FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id),
    CONSTRAINT fk_entry_ledger_account
    FOREIGN KEY (ledger_account_id) REFERENCES ledger_account (ledger_account_id)
    );
