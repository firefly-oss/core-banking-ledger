package com.firefly.core.banking.ledger.web.controllers.price.v1;

import com.firefly.core.banking.ledger.core.services.price.v1.PriceOracleService;
import com.firefly.core.banking.ledger.interfaces.dtos.price.v1.MarketData;
import com.firefly.core.banking.ledger.interfaces.dtos.price.v1.PriceUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * REST controller for cryptocurrency price data.
 * Provides endpoints for getting current and historical prices,
 * as well as subscribing to price updates.
 */
@RestController
@RequestMapping("/api/v1/prices")
@Tag(name = "Price Oracle", description = "Cryptocurrency price data API")
public class PriceOracleController {

    private final PriceOracleService priceOracleService;

    @Autowired
    public PriceOracleController(PriceOracleService priceOracleService) {
        this.priceOracleService = priceOracleService;
    }

    /**
     * Gets the current price of a crypto asset in the specified quote currency.
     *
     * @param cryptoAssetId The ID of the crypto asset
     * @param quoteCurrency The currency to quote the price in (e.g., "USD", "EUR")
     * @return The current price
     */
    @GetMapping("/current")
    @Operation(summary = "Get current price", 
               description = "Gets the current price of a crypto asset in the specified quote currency")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Price retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(responseCode = "404", description = "Crypto asset not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<BigDecimal>> getCurrentPrice(
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId,
            @Parameter(description = "Currency to quote the price in (e.g., USD, EUR)") @RequestParam String quoteCurrency) {
        
        return priceOracleService.getCurrentPrice(cryptoAssetId, quoteCurrency)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Gets the historical price of a crypto asset at a specific timestamp.
     *
     * @param cryptoAssetId The ID of the crypto asset
     * @param quoteCurrency The currency to quote the price in (e.g., "USD", "EUR")
     * @param timestamp The timestamp for which to get the price
     * @return The historical price
     */
    @GetMapping("/historical")
    @Operation(summary = "Get historical price", 
               description = "Gets the historical price of a crypto asset at a specific timestamp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Price retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(responseCode = "404", description = "Crypto asset not found or no price data available for the timestamp"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<BigDecimal>> getHistoricalPrice(
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId,
            @Parameter(description = "Currency to quote the price in (e.g., USD, EUR)") @RequestParam String quoteCurrency,
            @Parameter(description = "Timestamp for which to get the price") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        
        return priceOracleService.getHistoricalPrice(cryptoAssetId, quoteCurrency, timestamp)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Subscribes to price updates for a crypto asset.
     * Returns a stream that emits a new PriceUpdate each time the price changes.
     *
     * @param cryptoAssetId The ID of the crypto asset
     * @param quoteCurrency The currency to quote the price in (e.g., "USD", "EUR")
     * @return A stream of PriceUpdate objects
     */
    @GetMapping("/updates")
    @Operation(summary = "Subscribe to price updates", 
               description = "Subscribes to price updates for a crypto asset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stream of price updates",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PriceUpdate.class))),
            @ApiResponse(responseCode = "404", description = "Crypto asset not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<PriceUpdate> subscribeToPriceUpdates(
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId,
            @Parameter(description = "Currency to quote the price in (e.g., USD, EUR)") @RequestParam String quoteCurrency) {
        
        return priceOracleService.subscribeToPriceUpdates(cryptoAssetId, quoteCurrency);
    }

    /**
     * Gets the price change percentage for a crypto asset over a specified time period.
     *
     * @param cryptoAssetId The ID of the crypto asset
     * @param quoteCurrency The currency to quote the price in (e.g., "USD", "EUR")
     * @param period The time period (e.g., "1h", "24h", "7d", "30d")
     * @return The price change percentage
     */
    @GetMapping("/change")
    @Operation(summary = "Get price change percentage", 
               description = "Gets the price change percentage for a crypto asset over a specified time period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Price change percentage retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "404", description = "Crypto asset not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<Double>> getPriceChangePercentage(
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId,
            @Parameter(description = "Currency to quote the price in (e.g., USD, EUR)") @RequestParam String quoteCurrency,
            @Parameter(description = "Time period (e.g., 1h, 24h, 7d, 30d)") @RequestParam String period) {
        
        return priceOracleService.getPriceChangePercentage(cryptoAssetId, quoteCurrency, period)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Gets the market data for a crypto asset.
     *
     * @param cryptoAssetId The ID of the crypto asset
     * @param quoteCurrency The currency to quote the price in (e.g., "USD", "EUR")
     * @return The MarketData
     */
    @GetMapping("/market-data")
    @Operation(summary = "Get market data", 
               description = "Gets the market data for a crypto asset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Market data retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MarketData.class))),
            @ApiResponse(responseCode = "404", description = "Crypto asset not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<MarketData>> getMarketData(
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId,
            @Parameter(description = "Currency to quote the price in (e.g., USD, EUR)") @RequestParam String quoteCurrency) {
        
        return priceOracleService.getMarketData(cryptoAssetId, quoteCurrency)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}