package com.firefly.core.banking.ledger.interfaces.dtos.blockchain.v1;

import com.firefly.core.banking.ledger.interfaces.enums.blockchain.v1.TransactionStatus;
import com.firefly.annotations.ValidAmount;
import com.firefly.annotations.ValidDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.UUID;
/**
 * Data Transfer Object representing a cryptocurrency transaction.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoTransaction {
    
    /**
     * The blockchain transaction hash
     */
    @NotBlank(message = "Transaction hash is required")
    @Size(max = 100, message = "Transaction hash cannot exceed 100 characters")
    private String transactionHash;

    /**
     * The ID of the crypto asset
     */
    @NotNull(message = "Crypto asset ID is required")
    private UUID cryptoAssetId;

    /**
     * The symbol of the crypto asset (e.g., BTC, ETH)
     */
    @NotBlank(message = "Asset symbol is required")
    @Size(max = 10, message = "Asset symbol cannot exceed 10 characters")
    private String assetSymbol;

    /**
     * The amount of the transaction
     */
    @NotNull(message = "Amount is required")
    @ValidAmount
    private BigDecimal amount;

    /**
     * The sender's blockchain address
     */
    @NotBlank(message = "Sender address is required")
    @Size(max = 100, message = "Sender address cannot exceed 100 characters")
    private String senderAddress;

    /**
     * The recipient's blockchain address
     */
    @NotBlank(message = "Recipient address is required")
    @Size(max = 100, message = "Recipient address cannot exceed 100 characters")
    private String recipientAddress;

    /**
     * The timestamp when the transaction was created
     */
    @NotNull(message = "Timestamp is required")
    @ValidDateTime
    private LocalDateTime timestamp;
    
    /**
     * The block number in which the transaction was included
     */
    private Long blockNumber;
    
    /**
     * The number of confirmations (blocks mined after the transaction's block)
     */
    private Integer confirmationCount;
    
    /**
     * The current status of the transaction
     */
    private TransactionStatus status;
    
    /**
     * The transaction fee in the network's native currency
     */
    private BigDecimal transactionFee;
    
    /**
     * The currency of the transaction fee (e.g., ETH for Ethereum)
     */
    private String feeCurrency;
    
    /**
     * Additional data or notes about the transaction
     */
    private String memo;
}