package com.firefly.core.banking.ledger.web.controllers.blockchain.v1;

import com.firefly.core.banking.ledger.core.services.blockchain.v1.BlockchainEventListener;
import com.firefly.core.banking.ledger.interfaces.dtos.blockchain.v1.BlockchainEvent;
import com.firefly.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
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
 * REST controller for blockchain event listening operations.
 * Provides endpoints for monitoring blockchain events, registering addresses for monitoring,
 * and controlling the event listener.
 */
@RestController
@RequestMapping("/api/v1/blockchain/events")
@Tag(name = "Blockchain Events", description = "Blockchain event monitoring API")
public class BlockchainEventListenerController {

    private final BlockchainEventListener blockchainEventListener;

    @Autowired
    public BlockchainEventListenerController(BlockchainEventListener blockchainEventListener) {
        this.blockchainEventListener = blockchainEventListener;
    }

    /**
     * Starts listening for deposit events on the specified blockchain network.
     *
     * @param blockchainNetworkId The ID of the blockchain network to monitor
     * @return A stream of TransactionDTO objects representing the created deposit transactions
     */
    @GetMapping("/deposits")
    @Operation(summary = "Listen for deposits", 
               description = "Starts listening for deposit events on the specified blockchain network")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stream of deposit transactions",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<TransactionDTO> listenForDeposits(
            @Parameter(description = "ID of the blockchain network") @RequestParam Long blockchainNetworkId) {
        
        return blockchainEventListener.listenForDeposits(blockchainNetworkId);
    }

    /**
     * Starts listening for token transfer events on the specified blockchain network.
     *
     * @param blockchainNetworkId The ID of the blockchain network to monitor
     * @param contractAddress The address of the token contract to monitor (optional)
     * @return A stream of TransactionDTO objects representing the created token transfer transactions
     */
    @GetMapping("/token-transfers")
    @Operation(summary = "Listen for token transfers", 
               description = "Starts listening for token transfer events on the specified blockchain network")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stream of token transfer transactions",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<TransactionDTO> listenForTokenTransfers(
            @Parameter(description = "ID of the blockchain network") @RequestParam Long blockchainNetworkId,
            @Parameter(description = "Address of the token contract (optional)") @RequestParam(required = false) String contractAddress) {
        
        return blockchainEventListener.listenForTokenTransfers(blockchainNetworkId, contractAddress);
    }

    /**
     * Starts listening for smart contract events on the specified blockchain network.
     *
     * @param blockchainNetworkId The ID of the blockchain network to monitor
     * @param contractAddress The address of the smart contract to monitor
     * @param eventName The name of the event to monitor
     * @return A stream of blockchain events
     */
    @GetMapping("/smart-contract-events")
    @Operation(summary = "Listen for smart contract events", 
               description = "Starts listening for smart contract events on the specified blockchain network")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stream of blockchain events",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BlockchainEvent.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<BlockchainEvent> listenForSmartContractEvents(
            @Parameter(description = "ID of the blockchain network") @RequestParam Long blockchainNetworkId,
            @Parameter(description = "Address of the smart contract") @RequestParam String contractAddress,
            @Parameter(description = "Name of the event") @RequestParam String eventName) {
        
        return blockchainEventListener.listenForSmartContractEvents(blockchainNetworkId, contractAddress, eventName);
    }

    /**
     * Registers an address to be monitored for incoming transactions.
     *
     * @param address The blockchain address to monitor
     * @param cryptoAssetId The ID of the crypto asset
     * @param accountId The ID of the account that owns the address
     * @return A response entity indicating success or failure
     */
    @PostMapping("/addresses")
    @Operation(summary = "Register address for monitoring", 
               description = "Registers an address to be monitored for incoming transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<Void>> registerAddressForMonitoring(
            @Parameter(description = "Blockchain address") @RequestParam String address,
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId,
            @Parameter(description = "ID of the account") @RequestParam Long accountId) {
        
        return blockchainEventListener.registerAddressForMonitoring(address, cryptoAssetId, accountId)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    /**
     * Unregisters an address from monitoring.
     *
     * @param address The blockchain address to stop monitoring
     * @param cryptoAssetId The ID of the crypto asset
     * @return A response entity indicating success or failure
     */
    @DeleteMapping("/addresses")
    @Operation(summary = "Unregister address from monitoring", 
               description = "Unregisters an address from monitoring")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address unregistered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<Void>> unregisterAddressFromMonitoring(
            @Parameter(description = "Blockchain address") @RequestParam String address,
            @Parameter(description = "ID of the crypto asset") @RequestParam Long cryptoAssetId) {
        
        return blockchainEventListener.unregisterAddressFromMonitoring(address, cryptoAssetId)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    /**
     * Starts the blockchain event listener for all registered addresses and contracts.
     *
     * @return A response entity indicating success or failure
     */
    @PostMapping("/start")
    @Operation(summary = "Start listener", 
               description = "Starts the blockchain event listener for all registered addresses and contracts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listener started successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<Void>> startListener() {
        return blockchainEventListener.startListener()
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    /**
     * Stops the blockchain event listener.
     *
     * @return A response entity indicating success or failure
     */
    @PostMapping("/stop")
    @Operation(summary = "Stop listener", 
               description = "Stops the blockchain event listener")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listener stopped successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<Void>> stopListener() {
        return blockchainEventListener.stopListener()
                .then(Mono.just(ResponseEntity.ok().build()));
    }
}