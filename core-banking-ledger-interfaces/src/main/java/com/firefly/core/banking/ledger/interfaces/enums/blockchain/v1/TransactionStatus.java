package com.firefly.core.banking.ledger.interfaces.enums.blockchain.v1;

/**
 * Enumeration of possible blockchain transaction statuses.
 */
public enum TransactionStatus {
    /**
     * Transaction has been submitted to the blockchain but not yet included in a block
     */
    PENDING,
    
    /**
     * Transaction has been included in a block but doesn't have enough confirmations yet
     */
    CONFIRMING,
    
    /**
     * Transaction has been confirmed with the required number of confirmations
     */
    CONFIRMED,
    
    /**
     * Transaction has failed (e.g., out of gas, reverted, etc.)
     */
    FAILED,
    
    /**
     * Transaction was dropped from the mempool (e.g., replaced by a transaction with higher gas price)
     */
    DROPPED,
    
    /**
     * Transaction status is unknown (e.g., transaction hash not found)
     */
    UNKNOWN
}