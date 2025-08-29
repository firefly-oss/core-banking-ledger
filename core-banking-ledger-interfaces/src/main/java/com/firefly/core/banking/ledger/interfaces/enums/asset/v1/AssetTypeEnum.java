package com.firefly.core.banking.ledger.interfaces.enums.asset.v1;

/**
 * Enumeration of asset types supported by the ledger system.
 * This enum corresponds to the asset_type_enum in the database.
 */
public enum AssetTypeEnum {
    /**
     * Traditional fiat currencies (e.g., USD, EUR, GBP)
     */
    FIAT,
    
    /**
     * Native cryptocurrencies (e.g., BTC, ETH)
     */
    CRYPTOCURRENCY,
    
    /**
     * Security tokens representing ownership in an asset
     */
    TOKEN_SECURITY,
    
    /**
     * Utility tokens providing access to a product or service
     */
    TOKEN_UTILITY,
    
    /**
     * Non-fungible tokens representing unique digital assets
     */
    TOKEN_NFT,
    
    /**
     * Stablecoins pegged to the value of another asset
     */
    TOKEN_STABLECOIN
}