-- Create casts for transaction_type_enum
CREATE CAST (varchar AS transaction_type_enum) WITH INOUT AS IMPLICIT;

-- Create casts for transaction_status_enum
CREATE CAST (varchar AS transaction_status_enum) WITH INOUT AS IMPLICIT;

-- Create casts for category_type_enum
CREATE CAST (varchar AS category_type_enum) WITH INOUT AS IMPLICIT;

-- Create casts for status_code_enum
CREATE CAST (varchar AS status_code_enum) WITH INOUT AS IMPLICIT;

-- Create casts for direct_debit_sequence_type_enum
CREATE CAST (varchar AS direct_debit_sequence_type_enum) WITH INOUT AS IMPLICIT;

-- Create casts for direct_debit_processing_status_enum
CREATE CAST (varchar AS direct_debit_processing_status_enum) WITH INOUT AS IMPLICIT;

-- Create casts for sepa_transaction_status_enum
CREATE CAST (varchar AS sepa_transaction_status_enum) WITH INOUT AS IMPLICIT;

-- Create casts for wire_transfer_priority_enum
CREATE CAST (varchar AS wire_transfer_priority_enum) WITH INOUT AS IMPLICIT;

-- Create casts for wire_reception_status_enum
CREATE CAST (varchar AS wire_reception_status_enum) WITH INOUT AS IMPLICIT;

-- Create casts for standing_order_frequency_enum
CREATE CAST (varchar AS standing_order_frequency_enum) WITH INOUT AS IMPLICIT;

-- Create casts for standing_order_status_enum
CREATE CAST (varchar AS standing_order_status_enum) WITH INOUT AS IMPLICIT;