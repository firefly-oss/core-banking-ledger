package com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for crypto transfer transactions.
 * Extends BaseCryptoTransactionDTO with transfer-specific fields.
 * Used for transfers between accounts within the system.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CryptoTransferDTO extends BaseCryptoTransactionDTO {
    
    /**
     * The ID of the source account
     */
    private Long sourceAccountId;
    
    /**
     * The ID of the source account space
     */
    private Long sourceAccountSpaceId;
    
    /**
     * The ID of the destination account
     */
    private Long destinationAccountId;
    
    /**
     * The ID of the destination account space
     */
    private Long destinationAccountSpaceId;
    
    /**
     * The sender's blockchain address (if an on-chain transaction is needed)
     */
    private String senderAddress;
    
    /**
     * The recipient's blockchain address (if an on-chain transaction is needed)
     */
    private String recipientAddress;
    
    /**
     * Flag indicating whether this transfer requires an on-chain transaction
     * If false, the transfer is done internally without blockchain interaction
     */
    private Boolean requiresOnChainTransaction;
    
    /**
     * The reference ID for the related transaction (e.g., the withdrawal transaction ID)
     */
    private Long relatedTransactionId;
    
    /**
     * The purpose or reason for the transfer
     */
    private String transferPurpose;
}