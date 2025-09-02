package com.firefly.core.banking.ledger.interfaces.dtos.price.v1;

import com.firefly.annotations.ValidAmount;
import com.firefly.annotations.ValidCurrencyCode;
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
    @NotNull(message = "Crypto asset ID is required")
    private UUID cryptoAssetId;

    /**
     * The symbol of the crypto asset (e.g., BTC, ETH)
     */
    @NotBlank(message = "Asset symbol is required")
    @Size(max = 10, message = "Asset symbol cannot exceed 10 characters")
    private String assetSymbol;

    /**
     * The name of the crypto asset (e.g., Bitcoin, Ethereum)
     */
    @NotBlank(message = "Asset name is required")
    @Size(max = 100, message = "Asset name cannot exceed 100 characters")
    private String assetName;

    /**
     * The current price of the crypto asset
     */
    @NotNull(message = "Price is required")
    @ValidAmount
    private BigDecimal price;

    /**
     * The currency in which the price is quoted (e.g., USD, EUR)
     */
    @NotBlank(message = "Quote currency is required")
    @ValidCurrencyCode
    private String quoteCurrency;

    /**
     * The timestamp of the price update
     */
    @NotNull(message = "Timestamp is required")
    @ValidDateTime
    private LocalDateTime timestamp;

    /**
     * The price change in the last 24 hours
     */
    @ValidAmount
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