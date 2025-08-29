package com.firefly.core.banking.ledger.core.services.blockchain.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.blockchain.v1.BlockchainEvent;
import com.firefly.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for listening to blockchain events and processing them.
 * This service monitors blockchain networks for events such as incoming deposits,
 * token transfers, and other relevant activities.
 */
public interface BlockchainEventListener {
    
    /**
     * Starts listening for deposit events on the specified blockchain network.
     * When a deposit is detected to one of our monitored addresses, a transaction is created.
     *
     * @param blockchainNetworkId The ID of the blockchain network to monitor
     * @return A Flux of TransactionDTO objects representing the created deposit transactions
     */
    Flux<TransactionDTO> listenForDeposits(Long blockchainNetworkId);
    
    /**
     * Starts listening for token transfer events on the specified blockchain network.
     * This is used for ERC-20, ERC-721, and other token standards.
     *
     * @param blockchainNetworkId The ID of the blockchain network to monitor
     * @param contractAddress The address of the token contract to monitor (optional)
     * @return A Flux of TransactionDTO objects representing the created token transfer transactions
     */
    Flux<TransactionDTO> listenForTokenTransfers(Long blockchainNetworkId, String contractAddress);
    
    /**
     * Starts listening for smart contract events on the specified blockchain network.
     * This is a more general method that can be used to monitor any type of event emitted by a smart contract.
     *
     * @param blockchainNetworkId The ID of the blockchain network to monitor
     * @param contractAddress The address of the smart contract to monitor
     * @param eventName The name of the event to monitor
     * @return A Flux of blockchain events
     */
    Flux<BlockchainEvent> listenForSmartContractEvents(Long blockchainNetworkId, String contractAddress, String eventName);
    
    /**
     * Registers an address to be monitored for incoming transactions.
     * This is typically called when a new deposit address is generated for a user.
     *
     * @param address The blockchain address to monitor
     * @param cryptoAssetId The ID of the crypto asset
     * @param accountId The ID of the account that owns the address
     * @return A Mono completing when the address is registered
     */
    Mono<Void> registerAddressForMonitoring(String address, Long cryptoAssetId, Long accountId);
    
    /**
     * Unregisters an address from monitoring.
     * This is typically called when an address is no longer in use.
     *
     * @param address The blockchain address to stop monitoring
     * @param cryptoAssetId The ID of the crypto asset
     * @return A Mono completing when the address is unregistered
     */
    Mono<Void> unregisterAddressFromMonitoring(String address, Long cryptoAssetId);
    
    /**
     * Starts the blockchain event listener for all registered addresses and contracts.
     * This is typically called during application startup.
     *
     * @return A Mono completing when the listener is started
     */
    Mono<Void> startListener();
    
    /**
     * Stops the blockchain event listener.
     * This is typically called during application shutdown.
     *
     * @return A Mono completing when the listener is stopped
     */
    Mono<Void> stopListener();
}