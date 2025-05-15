package com.catalis.core.banking.ledger.interfaces.enums.attachment.v1;

/**
 * Enum representing the types of attachments that can be associated with transactions.
 */
public enum AttachmentTypeEnum {
    /**
     * Invoice document.
     */
    INVOICE,
    
    /**
     * Receipt document.
     */
    RECEIPT,
    
    /**
     * Contract document.
     */
    CONTRACT,
    
    /**
     * Identification document.
     */
    IDENTIFICATION,
    
    /**
     * Authorization document.
     */
    AUTHORIZATION,
    
    /**
     * Statement document.
     */
    STATEMENT,
    
    /**
     * Proof of payment document.
     */
    PROOF_OF_PAYMENT,
    
    /**
     * Supporting document for a transaction.
     */
    SUPPORTING_DOCUMENT,
    
    /**
     * Correspondence related to a transaction.
     */
    CORRESPONDENCE,
    
    /**
     * Other type of document.
     */
    OTHER
}
