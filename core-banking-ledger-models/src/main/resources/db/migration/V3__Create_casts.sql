-- V3__Create_casts.sql
-- Postgres casts between varchar and the enumerations using built-in IN/OUT functions.

-------------------------
-- transaction_type_enum
-------------------------
CREATE CAST (varchar AS transaction_type_enum)
    WITH INOUT
    AS IMPLICIT;

-------------------------
-- transaction_status_enum
-------------------------
CREATE CAST (varchar AS transaction_status_enum)
    WITH INOUT
    AS IMPLICIT;

-------------------------
-- category_type_enum
-------------------------
CREATE CAST (varchar AS category_type_enum)
    WITH INOUT
    AS IMPLICIT;

-------------------------
-- status_code_enum
-------------------------
CREATE CAST (varchar AS status_code_enum)
    WITH INOUT
    AS IMPLICIT;

-------------------------
-- direct_debit_sequence_type_enum
-------------------------
CREATE CAST (varchar AS direct_debit_sequence_type_enum)
    WITH INOUT
    AS IMPLICIT;

-------------------------
-- direct_debit_processing_status_enum
-------------------------
CREATE CAST (varchar AS direct_debit_processing_status_enum)
    WITH INOUT
    AS IMPLICIT;

-------------------------
-- direct_debit_spanish_scheme_enum
-------------------------
CREATE CAST (varchar AS direct_debit_spanish_scheme_enum)
    WITH INOUT
    AS IMPLICIT;

-------------------------
-- sepa_transaction_status_enum
-------------------------
CREATE CAST (varchar AS sepa_transaction_status_enum)
    WITH INOUT
    AS IMPLICIT;

-------------------------
-- sepa_payment_scheme_enum
-------------------------
CREATE CAST (varchar AS sepa_payment_scheme_enum)
    WITH INOUT
    AS IMPLICIT;

-------------------------
-- wire_transfer_priority_enum
-------------------------
CREATE CAST (varchar AS wire_transfer_priority_enum)
    WITH INOUT
    AS IMPLICIT;

-------------------------
-- wire_reception_status_enum
-------------------------
CREATE CAST (varchar AS wire_reception_status_enum)
    WITH INOUT
    AS IMPLICIT;

-------------------------
-- ledger_account_type_enum
-------------------------
CREATE CAST (varchar AS ledger_account_type_enum)
    WITH INOUT
    AS IMPLICIT;

-------------------------
-- debit_credit_indicator_enum
-------------------------
CREATE CAST (varchar AS debit_credit_indicator_enum)
    WITH INOUT
    AS IMPLICIT;


-------------------------
-- standing_order_frequency_enum
-------------------------
CREATE CAST (varchar AS standing_order_frequency_enum)
    WITH INOUT
    AS IMPLICIT;

-------------------------
-- standing_order_status_enum
-------------------------
CREATE CAST (varchar AS standing_order_status_enum)
    WITH INOUT
    AS IMPLICIT;