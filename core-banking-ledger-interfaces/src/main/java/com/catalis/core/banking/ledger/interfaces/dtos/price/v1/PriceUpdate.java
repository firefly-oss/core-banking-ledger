package com.catalis.core.banking.ledger.interfaces.dtos.price.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a price update for a crypto asset.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceUpdate {
    
    /**
     * The ID of the crypto asset
     */
    private Long cryptoAssetId;
    
    /**
     * The symbol of the crypto asset (e.g., BTC, ETH)
     */
    private String assetSymbol;
    
    /**
     * The name of the crypto asset (e.g., Bitcoin, Ethereum)
     */
    private String assetName;
    
    /**
     * The current price of the crypto asset
     */
    private BigDecimal price;
    
    /**
     * The currency in which the price is quoted (e.g., USD, EUR)
     */
    private String quoteCurrency;
    
    /**
     * The timestamp of the price update
     */
    private LocalDateTime timestamp;
    
    /**
     * The price change in the last 24 hours
     */
    private BigDecimal priceChange24h;
    
    /**
     * The price change percentage in the last 24 hours
     */
    private Double priceChangePercentage24h;
    
    /**
     * The source of the price data (e.g., "Coinbase", "Binance")
     */
    private String source;
}