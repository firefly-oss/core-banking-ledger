package com.catalis.core.banking.ledger.interfaces.dtos.crypto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for token burning transactions.
 * Extends BaseCryptoTransactionDTO with token burning-specific fields.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TokenBurnDTO extends BaseCryptoTransactionDTO {
    
    /**
     * The owner's blockchain address (from which tokens will be burned)
     */
    private String ownerAddress;
    
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
     * Flag indicating whether this is a batch burn (multiple tokens in one transaction)
     */
    private Boolean isBatchBurn;
    
    /**
     * The number of tokens to burn (for fungible tokens or batch NFT burning)
     */
    private Long tokenQuantity;
    
    /**
     * The reason for burning the tokens
     */
    private String burnReason;
    
    /**
     * The ID of the user who authorized the burning
     */
    private String authorizedBy;
    
    /**
     * Flag indicating whether this burn is part of a redemption process
     */
    private Boolean isRedemption;
    
    /**
     * The redemption reference ID if this burn is part of a redemption process
     */
    private String redemptionReference;
}