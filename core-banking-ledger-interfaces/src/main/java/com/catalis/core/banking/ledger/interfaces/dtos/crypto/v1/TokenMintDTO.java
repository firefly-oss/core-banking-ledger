package com.catalis.core.banking.ledger.interfaces.dtos.crypto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * Data Transfer Object for token minting transactions.
 * Extends BaseCryptoTransactionDTO with token minting-specific fields.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TokenMintDTO extends BaseCryptoTransactionDTO {
    
    /**
     * The recipient's blockchain address (where tokens will be minted to)
     */
    private String recipientAddress;
    
    /**
     * The smart contract address of the token
     */
    private String contractAddress;
    
    /**
     * The token ID for NFTs (null for fungible tokens)
     */
    private String tokenId;
    
    /**
     * The token standard (e.g., "ERC20", "ERC721", "ERC1155")
     */
    private String tokenStandard;
    
    /**
     * The URI for token metadata (primarily for NFTs)
     */
    private String tokenUri;
    
    /**
     * The royalty percentage for NFTs (e.g., 2.5 for 2.5%)
     */
    private Double royaltyPercentage;
    
    /**
     * The address that will receive royalties
     */
    private String royaltyRecipientAddress;
    
    /**
     * Additional properties for the token (primarily for NFTs)
     */
    private Map<String, Object> tokenProperties;
    
    /**
     * Flag indicating whether this is a batch mint (multiple tokens in one transaction)
     */
    private Boolean isBatchMint;
    
    /**
     * The number of tokens to mint (for fungible tokens or batch NFT minting)
     */
    private Long tokenQuantity;
    
    /**
     * The ID of the user who authorized the minting
     */
    private String authorizedBy;
}