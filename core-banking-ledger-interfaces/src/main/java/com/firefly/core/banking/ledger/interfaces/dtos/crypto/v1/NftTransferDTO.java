package com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object for NFT transfer transactions.
 * Extends BaseCryptoTransactionDTO with NFT transfer-specific fields.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NftTransferDTO extends BaseCryptoTransactionDTO {
    
    /**
     * The sender's blockchain address
     */
    private String senderAddress;
    
    /**
     * The recipient's blockchain address
     */
    private String recipientAddress;
    
    /**
     * The smart contract address of the NFT
     */
    private String contractAddress;
    
    /**
     * The token ID of the NFT
     */
    private String tokenId;
    
    /**
     * List of token IDs for batch transfers (ERC-1155)
     */
    private List<String> tokenIds;
    
    /**
     * List of quantities for each token ID in batch transfers (ERC-1155)
     */
    private List<Long> quantities;
    
    /**
     * The token standard (e.g., "ERC721", "ERC1155")
     */
    private String tokenStandard;
    
    /**
     * The URI for token metadata
     */
    private String tokenUri;
    
    /**
     * Flag indicating whether this is a sale transaction
     */
    private Boolean isSale;
    
    /**
     * The sale price if this is a sale transaction
     */
    private BigDecimal salePrice;
    
    /**
     * The currency of the sale price
     */
    private String salePriceCurrency;
    
    /**
     * The marketplace where the sale occurred (if applicable)
     */
    private String marketplace;
    
    /**
     * The marketplace fee percentage (if applicable)
     */
    private Double marketplaceFeePercentage;
    
    /**
     * The royalty fee percentage (if applicable)
     */
    private Double royaltyFeePercentage;
    
    /**
     * The address that received the royalty payment
     */
    private String royaltyRecipientAddress;
}