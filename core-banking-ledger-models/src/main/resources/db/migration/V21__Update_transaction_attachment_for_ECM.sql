-- V21__Update_transaction_attachment_for_ECM.sql

-- Rename object_storage_url column to document_id
ALTER TABLE transaction_attachment RENAME COLUMN object_storage_url TO document_id;

-- Update column comment
COMMENT ON COLUMN transaction_attachment.document_id IS 'Reference to document ID in external ECM (Enterprise Content Management) system';

-- Update column type comment
COMMENT ON COLUMN transaction_attachment.attachment_type IS 'Type of attachment (INVOICE, RECEIPT, CONTRACT, etc.)';

-- Add constraint to ensure attachment_type is a valid enum value
ALTER TABLE transaction_attachment 
ADD CONSTRAINT check_attachment_type 
CHECK (attachment_type IN (
    'INVOICE', 
    'RECEIPT', 
    'CONTRACT', 
    'IDENTIFICATION', 
    'AUTHORIZATION', 
    'STATEMENT', 
    'PROOF_OF_PAYMENT', 
    'SUPPORTING_DOCUMENT', 
    'CORRESPONDENCE', 
    'OTHER'
));
