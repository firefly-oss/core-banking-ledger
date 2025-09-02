package com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;

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
    @NotBlank(message = "Sender address is required")
    @Size(max = 100, message = "Sender address cannot exceed 100 characters")
    private String senderAddress;

    /**
     * The recipient's blockchain address (destination address)
     */
    @NotBlank(message = "Recipient address is required")
    @Size(max = 100, message = "Recipient address cannot exceed 100 characters")
    private String recipientAddress;

    /**
     * Flag indicating whether this is an internal transfer to another account in the system
     */
    private Boolean isInternalTransfer;

    /**
     * The priority of the withdrawal (e.g., "HIGH", "MEDIUM", "LOW")
     * Higher priority typically means higher gas price/fee
     */
    @Pattern(regexp = "HIGH|MEDIUM|LOW", message = "Priority must be one of: HIGH, MEDIUM, LOW")
    private String priority;
    
    /**
     * Flag indicating whether the withdrawal requires additional approval
     */
    private Boolean requiresApproval;

    /**
     * The ID of the user who approved the withdrawal (if applicable)
     */
    @Size(max = 100, message = "Approved by cannot exceed 100 characters")
    private String approvedBy;

    /**
     * Flag indicating whether the withdrawal is part of a batched transaction
     */
    private Boolean isBatchedTransaction;

    /**
     * The ID of the batch if this is part of a batched transaction
     */
    @Size(max = 100, message = "Batch ID cannot exceed 100 characters")
    private String batchId;

    /**
     * The destination tag or memo required by some blockchains/exchanges
     */
    @Size(max = 100, message = "Destination tag cannot exceed 100 characters")
    private String destinationTag;
}