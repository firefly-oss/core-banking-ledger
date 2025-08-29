package com.firefly.core.banking.ledger.core.services.price.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.price.v1.MarketData;
import com.firefly.core.banking.ledger.interfaces.dtos.price.v1.PriceUpdate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service interface for retrieving cryptocurrency price data.
 * Provides methods for getting current and historical prices,
 * as well as subscribing to price updates.
 */
public interface PriceOracleService {
    
    /**
     * Gets the current price of a crypto asset in the specified quote currency.
     *
     * @param cryptoAssetId The ID of the crypto asset
     * @param quoteCurrency The currency to quote the price in (e.g., "USD", "EUR")
     * @return A Mono containing the current price
     */
    Mono<BigDecimal> getCurrentPrice(Long cryptoAssetId, String quoteCurrency);
    
    /**
     * Gets the historical price of a crypto asset at a specific timestamp.
     *
     * @param cryptoAssetId The ID of the crypto asset
     * @param quoteCurrency The currency to quote the price in (e.g., "USD", "EUR")
     * @param timestamp The timestamp for which to get the price
     * @return A Mono containing the historical price
     */
    Mono<BigDecimal> getHistoricalPrice(Long cryptoAssetId, String quoteCurrency, LocalDateTime timestamp);
    
    /**
     * Subscribes to price updates for a crypto asset.
     * Returns a Flux that emits a new PriceUpdate each time the price changes.
     *
     * @param cryptoAssetId The ID of the crypto asset
     * @param quoteCurrency The currency to quote the price in (e.g., "USD", "EUR")
     * @return A Flux of PriceUpdate objects
     */
    Flux<PriceUpdate> subscribeToPriceUpdates(Long cryptoAssetId, String quoteCurrency);
    
    /**
     * Gets the price change percentage for a crypto asset over a specified time period.
     *
     * @param cryptoAssetId The ID of the crypto asset
     * @param quoteCurrency The currency to quote the price in (e.g., "USD", "EUR")
     * @param period The time period (e.g., "1h", "24h", "7d", "30d")
     * @return A Mono containing the price change percentage
     */
    Mono<Double> getPriceChangePercentage(Long cryptoAssetId, String quoteCurrency, String period);
    
    /**
     * Gets the market data for a crypto asset.
     *
     * @param cryptoAssetId The ID of the crypto asset
     * @param quoteCurrency The currency to quote the price in (e.g., "USD", "EUR")
     * @return A Mono containing the MarketData
     */
    Mono<MarketData> getMarketData(Long cryptoAssetId, String quoteCurrency);
}