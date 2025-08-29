package com.firefly.core.banking.ledger.web.controllers.wallet.v1;

import com.firefly.core.banking.ledger.core.services.wallet.v1.WalletService;
import com.firefly.core.banking.ledger.interfaces.dtos.blockchain.v1.CryptoTransaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * REST controller for cryptocurrency wallet operations.
 * Provides endpoints for generating addresses, validating addresses,
 * checking balances, and retrieving transaction history.
 */
@RestController
@RequestMapping("/api/v1/wallets")
@Tag(name = "Wallet", description = "Cryptocurrency wallet API")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    /**
     * Generates a new blockchain address for the specified account and crypto asset.
     *
     * @param accountId The ID of the account
     * @param cryptoAssetId The ID of the crypto asset
     * @return The generated blockchain address
     */
    @PostMapping("/addresses")
    @Operation(summary = "Generate address", 
               description = "Generates a new blockchain address for the specified account and crypto asset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address generated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<String>> generateAddress(
            @Parameter(description = "ID of the account") @RequestParam Long accountId,
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId) {
        
        return walletService.generateAddress(accountId, cryptoAssetId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Validates a blockchain address for the specified crypto asset.
     *
     * @param address The blockchain address to validate
     * @param cryptoAssetId The ID of the crypto asset
     * @return A boolean indicating whether the address is valid
     */
    @GetMapping("/addresses/validate")
    @Operation(summary = "Validate address", 
               description = "Validates a blockchain address for the specified crypto asset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address validated",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<Boolean>> validateAddress(
            @Parameter(description = "Blockchain address") @RequestParam String address,
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId) {
        
        return walletService.validateAddress(address, cryptoAssetId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Gets the balance of the specified crypto asset for the specified account.
     *
     * @param accountId The ID of the account
     * @param cryptoAssetId The ID of the crypto asset
     * @return The balance
     */
    @GetMapping("/balances")
    @Operation(summary = "Get balance", 
               description = "Gets the balance of the specified crypto asset for the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(responseCode = "404", description = "Account or crypto asset not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<BigDecimal>> getBalance(
            @Parameter(description = "ID of the account") @RequestParam Long accountId,
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId) {
        
        return walletService.getBalance(accountId, cryptoAssetId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Gets the transaction history for the specified account and crypto asset.
     *
     * @param accountId The ID of the account
     * @param cryptoAssetId The ID of the crypto asset
     * @return A stream of CryptoTransaction objects
     */
    @GetMapping("/transactions")
    @Operation(summary = "Get transaction history", 
               description = "Gets the transaction history for the specified account and crypto asset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction history retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CryptoTransaction.class))),
            @ApiResponse(responseCode = "404", description = "Account or crypto asset not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<CryptoTransaction> getTransactionHistory(
            @Parameter(description = "ID of the account") @RequestParam Long accountId,
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId) {
        
        return walletService.getTransactionHistory(accountId, cryptoAssetId);
    }

    /**
     * Gets the transaction history for the specified blockchain address.
     *
     * @param address The blockchain address
     * @param cryptoAssetId The ID of the crypto asset
     * @return A stream of CryptoTransaction objects
     */
    @GetMapping("/addresses/{address}/transactions")
    @Operation(summary = "Get address transaction history", 
               description = "Gets the transaction history for the specified blockchain address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction history retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CryptoTransaction.class))),
            @ApiResponse(responseCode = "404", description = "Address or crypto asset not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<CryptoTransaction> getAddressTransactionHistory(
            @Parameter(description = "Blockchain address") @PathVariable String address,
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId) {
        
        return walletService.getAddressTransactionHistory(address, cryptoAssetId);
    }

    /**
     * Gets the details of a specific transaction by its hash.
     *
     * @param transactionHash The blockchain transaction hash
     * @param cryptoAssetId The ID of the crypto asset
     * @return The CryptoTransaction
     */
    @GetMapping("/transactions/{transactionHash}")
    @Operation(summary = "Get transaction details", 
               description = "Gets the details of a specific transaction by its hash")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction details retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CryptoTransaction.class))),
            @ApiResponse(responseCode = "404", description = "Transaction or crypto asset not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<CryptoTransaction>> getTransaction(
            @Parameter(description = "Blockchain transaction hash") @PathVariable String transactionHash,
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId) {
        
        return walletService.getTransaction(transactionHash, cryptoAssetId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}