package com.firefly.core.banking.ledger.core.services.blockchain.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.blockchain.v1.BlockConfirmation;
import com.firefly.core.banking.ledger.interfaces.enums.blockchain.v1.TransactionStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mock implementation of the BlockchainService interface for testing purposes.
 * Simulates blockchain interactions without actually connecting to a blockchain network.
 */
public class MockBlockchainService implements BlockchainService {

    private final Map<String, TransactionStatus> transactionStatuses = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> confirmationCounts = new ConcurrentHashMap<>();
    private final Map<Long, BigDecimal> gasPrices = new HashMap<>();
    private final Map<Long, BigDecimal> estimatedGasAmounts = new HashMap<>();

    /**
     * Initializes the mock service with default values.
     */
    public MockBlockchainService() {
        // Default gas prices for common networks
        gasPrices.put(1L, new BigDecimal("0.000000025")); // Ethereum (25 Gwei)
        gasPrices.put(2L, new BigDecimal("0.00000001")); // Binance Smart Chain (10 Gwei)
        gasPrices.put(3L, new BigDecimal("0.000000001")); // Polygon (1 Gwei)

        // Default estimated gas amounts for common transaction types
        estimatedGasAmounts.put(1L, new BigDecimal("21000")); // Simple ETH transfer
        estimatedGasAmounts.put(2L, new BigDecimal("65000")); // ERC-20 transfer
        estimatedGasAmounts.put(3L, new BigDecimal("200000")); // Complex smart contract interaction
    }

    @Override
    public Mono<String> sendTransaction(String fromAddress, String toAddress, BigDecimal amount, Long cryptoAssetId) {
        // Generate a random transaction hash
        String txHash = "0x" + UUID.randomUUID().toString().replace("-", "");
        
        // Set initial status to PENDING
        transactionStatuses.put(txHash, TransactionStatus.PENDING);
        
        // Initialize confirmation count to 0
        confirmationCounts.put(txHash, new AtomicInteger(0));
        
        // Simulate network delay
        return Mono.just(txHash).delayElement(Duration.ofMillis(100));
    }

    @Override
    public Mono<TransactionStatus> getTransactionStatus(String transactionHash, Long blockchainNetworkId) {
        // Return the status if it exists, otherwise return UNKNOWN
        return Mono.justOrEmpty(transactionStatuses.get(transactionHash))
                .defaultIfEmpty(TransactionStatus.UNKNOWN)
                .delayElement(Duration.ofMillis(50)); // Simulate network delay
    }

    @Override
    public Flux<BlockConfirmation> trackTransactionConfirmations(String transactionHash, Long blockchainNetworkId) {
        // If the transaction doesn't exist, return an empty flux
        if (!transactionStatuses.containsKey(transactionHash)) {
            return Flux.empty();
        }
        
        // Get the current confirmation count
        AtomicInteger count = confirmationCounts.getOrDefault(transactionHash, new AtomicInteger(0));
        
        // Simulate confirmations coming in over time
        return Flux.range(count.get() + 1, 12) // Simulate up to 12 confirmations
                .delayElements(Duration.ofMillis(500)) // Each confirmation takes 500ms
                .map(confirmationNumber -> {
                    // Update the confirmation count
                    count.set(confirmationNumber);
                    
                    // Update the transaction status based on confirmation count
                    if (confirmationNumber >= 1) {
                        transactionStatuses.put(transactionHash, TransactionStatus.CONFIRMING);
                    }
                    if (confirmationNumber >= 6) {
                        transactionStatuses.put(transactionHash, TransactionStatus.CONFIRMED);
                    }
                    
                    // Create and return a BlockConfirmation object
                    return BlockConfirmation.builder()
                            .transactionHash(transactionHash)
                            .blockNumber(1000000L + confirmationNumber) // Simulate block number
                            .blockTimestamp(LocalDateTime.now())
                            .confirmationCount(confirmationNumber)
                            .currentBlockHeight(1000000L + confirmationNumber + 1) // Current block is always ahead
                            .isFinal(confirmationNumber >= 6) // Consider 6 confirmations as final
                            .requiredConfirmations(6) // Standard is 6 confirmations
                            .build();
                });
    }

    @Override
    public Mono<BigDecimal> getGasPrice(Long blockchainNetworkId) {
        // Return the gas price for the specified network, or a default value
        return Mono.justOrEmpty(gasPrices.get(blockchainNetworkId))
                .defaultIfEmpty(new BigDecimal("0.00000001")) // Default to 10 Gwei
                .delayElement(Duration.ofMillis(50)); // Simulate network delay
    }

    @Override
    public Mono<BigDecimal> estimateGas(String fromAddress, String toAddress, BigDecimal amount, Long cryptoAssetId) {
        // Return the estimated gas amount for the specified asset, or a default value
        return Mono.justOrEmpty(estimatedGasAmounts.get(cryptoAssetId))
                .defaultIfEmpty(new BigDecimal("21000")) // Default to simple transfer
                .delayElement(Duration.ofMillis(100)); // Simulate network delay
    }

    /**
     * Manually set the status of a transaction for testing purposes.
     *
     * @param transactionHash The transaction hash
     * @param status The status to set
     */
    public void setTransactionStatus(String transactionHash, TransactionStatus status) {
        transactionStatuses.put(transactionHash, status);
    }

    /**
     * Manually set the confirmation count for a transaction for testing purposes.
     *
     * @param transactionHash The transaction hash
     * @param count The confirmation count to set
     */
    public void setConfirmationCount(String transactionHash, int count) {
        confirmationCounts.put(transactionHash, new AtomicInteger(count));
    }

    /**
     * Manually set the gas price for a blockchain network for testing purposes.
     *
     * @param blockchainNetworkId The blockchain network ID
     * @param gasPrice The gas price to set
     */
    public void setGasPrice(Long blockchainNetworkId, BigDecimal gasPrice) {
        gasPrices.put(blockchainNetworkId, gasPrice);
    }

    /**
     * Manually set the estimated gas amount for a crypto asset for testing purposes.
     *
     * @param cryptoAssetId The crypto asset ID
     * @param gasAmount The gas amount to set
     */
    public void setEstimatedGas(Long cryptoAssetId, BigDecimal gasAmount) {
        estimatedGasAmounts.put(cryptoAssetId, gasAmount);
    }
}