package com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for crypto withdrawal transactions.
 * Extends BaseCryptoTransactionDTO with withdrawal-specific fields.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CryptoWithdrawalDTO extends BaseCryptoTransactionDTO {
    
    /**
     * The sender's blockchain address (withdrawal source address)
     */
    private String senderAddress;
    
    /**
     * The recipient's blockchain address (destination address)
     */
    private String recipientAddress;
    
    /**
     * Flag indicating whether this is an internal transfer to another account in the system
     */
    private Boolean isInternalTransfer;
    
    /**
     * The priority of the withdrawal (e.g., "HIGH", "MEDIUM", "LOW")
     * Higher priority typically means higher gas price/fee
     */
    private String priority;
    
    /**
     * Flag indicating whether the withdrawal requires additional approval
     */
    private Boolean requiresApproval;
    
    /**
     * The ID of the user who approved the withdrawal (if applicable)
     */
    private String approvedBy;
    
    /**
     * Flag indicating whether the withdrawal is part of a batched transaction
     */
    private Boolean isBatchedTransaction;
    
    /**
     * The ID of the batch if this is part of a batched transaction
     */
    private String batchId;
    
    /**
     * The destination tag or memo required by some blockchains/exchanges
     */
    private String destinationTag;
}