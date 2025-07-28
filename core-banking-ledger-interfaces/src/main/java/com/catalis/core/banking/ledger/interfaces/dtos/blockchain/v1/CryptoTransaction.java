package com.catalis.core.banking.ledger.interfaces.dtos.blockchain.v1;

import com.catalis.core.banking.ledger.interfaces.enums.blockchain.v1.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private String transactionHash;
    
    /**
     * The ID of the crypto asset
     */
    private Long cryptoAssetId;
    
    /**
     * The symbol of the crypto asset (e.g., BTC, ETH)
     */
    private String assetSymbol;
    
    /**
     * The amount of the transaction
     */
    private BigDecimal amount;
    
    /**
     * The sender's blockchain address
     */
    private String senderAddress;
    
    /**
     * The recipient's blockchain address
     */
    private String recipientAddress;
    
    /**
     * The timestamp when the transaction was created
     */
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