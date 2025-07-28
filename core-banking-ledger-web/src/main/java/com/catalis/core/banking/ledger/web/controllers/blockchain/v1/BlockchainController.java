package com.catalis.core.banking.ledger.web.controllers.blockchain.v1;

import com.catalis.core.banking.ledger.core.services.blockchain.v1.BlockchainService;
import com.catalis.core.banking.ledger.interfaces.dtos.blockchain.v1.BlockConfirmation;
import com.catalis.core.banking.ledger.interfaces.enums.blockchain.v1.TransactionStatus;
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
 * REST controller for blockchain operations.
 * Provides endpoints for sending transactions, checking transaction status,
 * tracking confirmations, and estimating gas costs.
 */
@RestController
@RequestMapping("/api/v1/blockchain")
@Tag(name = "Blockchain", description = "Blockchain operations API")
public class BlockchainController {

    private final BlockchainService blockchainService;

    @Autowired
    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    /**
     * Sends a transaction to the blockchain network.
     *
     * @param fromAddress The sender's blockchain address
     * @param toAddress The recipient's blockchain address
     * @param amount The amount to send
     * @param cryptoAssetId The ID of the crypto asset to send
     * @return The blockchain transaction hash
     */
    @PostMapping("/transactions")
    @Operation(summary = "Send a blockchain transaction", description = "Sends a transaction to the blockchain network")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction sent successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<String>> sendTransaction(
            @Parameter(description = "Sender's blockchain address") @RequestParam String fromAddress,
            @Parameter(description = "Recipient's blockchain address") @RequestParam String toAddress,
            @Parameter(description = "Amount to send") @RequestParam BigDecimal amount,
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId) {
        
        return blockchainService.sendTransaction(fromAddress, toAddress, amount, cryptoAssetId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Gets the current status of a transaction on the blockchain.
     *
     * @param transactionHash The blockchain transaction hash
     * @param blockchainNetworkId The ID of the blockchain network
     * @return The transaction status
     */
    @GetMapping("/transactions/{transactionHash}/status")
    @Operation(summary = "Get transaction status", description = "Gets the current status of a transaction on the blockchain")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionStatus.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<TransactionStatus>> getTransactionStatus(
            @Parameter(description = "Blockchain transaction hash") @PathVariable String transactionHash,
            @Parameter(description = "ID of the blockchain network") @RequestParam Long blockchainNetworkId) {
        
        return blockchainService.getTransactionStatus(transactionHash, blockchainNetworkId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Tracks confirmations for a transaction on the blockchain.
     * Returns a stream that emits a new BlockConfirmation each time the confirmation count increases.
     *
     * @param transactionHash The blockchain transaction hash
     * @param blockchainNetworkId The ID of the blockchain network
     * @return A stream of BlockConfirmation objects
     */
    @GetMapping("/transactions/{transactionHash}/confirmations")
    @Operation(summary = "Track transaction confirmations", 
               description = "Tracks confirmations for a transaction on the blockchain")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stream of confirmations",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BlockConfirmation.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<BlockConfirmation> trackTransactionConfirmations(
            @Parameter(description = "Blockchain transaction hash") @PathVariable String transactionHash,
            @Parameter(description = "ID of the blockchain network") @RequestParam Long blockchainNetworkId) {
        
        return blockchainService.trackTransactionConfirmations(transactionHash, blockchainNetworkId);
    }

    /**
     * Gets the current gas price on the blockchain network.
     *
     * @param blockchainNetworkId The ID of the blockchain network
     * @return The current gas price in the network's native currency
     */
    @GetMapping("/gas-price")
    @Operation(summary = "Get gas price", description = "Gets the current gas price on the blockchain network")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gas price retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<BigDecimal>> getGasPrice(
            @Parameter(description = "ID of the blockchain network") @RequestParam Long blockchainNetworkId) {
        
        return blockchainService.getGasPrice(blockchainNetworkId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Estimates the gas required for a transaction.
     *
     * @param fromAddress The sender's blockchain address
     * @param toAddress The recipient's blockchain address
     * @param amount The amount to send
     * @param cryptoAssetId The ID of the crypto asset to send
     * @return The estimated gas amount
     */
    @GetMapping("/estimate-gas")
    @Operation(summary = "Estimate gas", description = "Estimates the gas required for a transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gas estimated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<BigDecimal>> estimateGas(
            @Parameter(description = "Sender's blockchain address") @RequestParam String fromAddress,
            @Parameter(description = "Recipient's blockchain address") @RequestParam String toAddress,
            @Parameter(description = "Amount to send") @RequestParam BigDecimal amount,
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId) {
        
        return blockchainService.estimateGas(fromAddress, toAddress, amount, cryptoAssetId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}