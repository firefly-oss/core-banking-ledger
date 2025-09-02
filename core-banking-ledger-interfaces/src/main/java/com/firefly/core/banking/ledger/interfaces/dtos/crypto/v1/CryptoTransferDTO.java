package com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;

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
    @NotNull(message = "Source account ID is required")
    private UUID sourceAccountId;

    /**
     * The ID of the source account space
     */
    private UUID sourceAccountSpaceId;

    /**
     * The ID of the destination account
     */
    @NotNull(message = "Destination account ID is required")
    private UUID destinationAccountId;

    /**
     * The ID of the destination account space
     */
    private UUID destinationAccountSpaceId;
    
    /**
     * The sender's blockchain address (if an on-chain transaction is needed)
     */
    @Size(max = 100, message = "Sender address cannot exceed 100 characters")
    private String senderAddress;

    /**
     * The recipient's blockchain address (if an on-chain transaction is needed)
     */
    @Size(max = 100, message = "Recipient address cannot exceed 100 characters")
    private String recipientAddress;

    /**
     * Flag indicating whether this transfer requires an on-chain transaction
     * If false, the transfer is done internally without blockchain interaction
     */
    private Boolean requiresOnChainTransaction;

    /**
     * The reference ID for the related transaction (e.g., the withdrawal transaction ID)
     */
    private UUID relatedTransactionId;

    /**
     * The purpose or reason for the transfer
     */
    @Size(max = 200, message = "Transfer purpose cannot exceed 200 characters")
    private String transferPurpose;
}