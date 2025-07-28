package com.catalis.core.banking.ledger.interfaces.dtos.crypto.v1;

import com.catalis.core.banking.ledger.interfaces.enums.asset.v1.AssetTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Base Data Transfer Object for crypto transactions.
 * Contains common fields for all types of crypto transactions.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseCryptoTransactionDTO {
    
    /**
     * The ID of the account
     */
    private Long accountId;
    
    /**
     * The ID of the account space
     */
    private Long accountSpaceId;
    
    /**
     * The ID of the crypto asset
     */
    private Long cryptoAssetId;
    
    /**
     * The symbol of the crypto asset (e.g., BTC, ETH)
     */
    private String assetSymbol;
    
    /**
     * The type of asset (CRYPTOCURRENCY, TOKEN_SECURITY, etc.)
     */
    private AssetTypeEnum assetType;
    
    /**
     * The amount of the transaction
     */
    private BigDecimal amount;
    
    /**
     * The ID of the blockchain network
     */
    private Long blockchainNetworkId;
    
    /**
     * The blockchain transaction hash (if already known)
     */
    private String blockchainTransactionHash;
    
    /**
     * The transaction date
     */
    private LocalDateTime transactionDate;
    
    /**
     * The value date
     */
    private LocalDateTime valueDate;
    
    /**
     * The description of the transaction
     */
    private String description;
    
    /**
     * The gas price in the network's native currency (for Ethereum-based networks)
     */
    private BigDecimal gasPrice;
    
    /**
     * The gas limit for the transaction (for Ethereum-based networks)
     */
    private Long gasLimit;
    
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
    
    /**
     * External reference for the transaction
     */
    private String externalReference;
}