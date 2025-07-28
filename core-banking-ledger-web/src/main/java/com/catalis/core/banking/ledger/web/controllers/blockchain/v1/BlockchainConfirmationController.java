package com.catalis.core.banking.ledger.web.controllers.blockchain.v1;

import com.catalis.core.banking.ledger.core.services.blockchain.v1.BlockchainConfirmationService;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
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

/**
 * REST controller for blockchain transaction confirmation operations.
 * Provides endpoints for tracking blockchain transaction confirmations and checking transaction status.
 */
@RestController
@RequestMapping("/api/v1/blockchain/confirmations")
@Tag(name = "Blockchain Confirmations", description = "Blockchain transaction confirmation API")
public class BlockchainConfirmationController {

    private final BlockchainConfirmationService blockchainConfirmationService;

    @Autowired
    public BlockchainConfirmationController(BlockchainConfirmationService blockchainConfirmationService) {
        this.blockchainConfirmationService = blockchainConfirmationService;
    }

    /**
     * Tracks confirmations for a blockchain transaction and updates the transaction status.
     * Returns a stream that emits an updated TransactionDTO each time the confirmation count changes.
     *
     * @param transactionHash The blockchain transaction hash
     * @param blockchainNetworkId The ID of the blockchain network
     * @return A stream of updated TransactionDTO objects as confirmation count changes
     */
    @GetMapping("/{transactionHash}/track")
    @Operation(summary = "Track transaction confirmations", 
               description = "Tracks confirmations for a blockchain transaction and updates the transaction status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stream of updated transactions",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<TransactionDTO> trackConfirmations(
            @Parameter(description = "Blockchain transaction hash") @PathVariable String transactionHash,
            @Parameter(description = "ID of the blockchain network") @RequestParam Long blockchainNetworkId) {
        
        return blockchainConfirmationService.trackConfirmations(transactionHash, blockchainNetworkId);
    }

    /**
     * Checks the status of a blockchain transaction and updates the ledger transaction accordingly.
     *
     * @param transactionHash The blockchain transaction hash
     * @param blockchainNetworkId The ID of the blockchain network
     * @return The updated transaction
     */
    @GetMapping("/{transactionHash}/status")
    @Operation(summary = "Check transaction status", 
               description = "Checks the status of a blockchain transaction and updates the ledger transaction accordingly")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction status checked and updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<TransactionDTO>> checkTransactionStatus(
            @Parameter(description = "Blockchain transaction hash") @PathVariable String transactionHash,
            @Parameter(description = "ID of the blockchain network") @RequestParam Long blockchainNetworkId) {
        
        return blockchainConfirmationService.checkTransactionStatus(transactionHash, blockchainNetworkId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}