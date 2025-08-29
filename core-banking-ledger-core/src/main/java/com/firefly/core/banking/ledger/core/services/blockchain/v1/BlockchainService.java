package com.firefly.core.banking.ledger.core.services.blockchain.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.blockchain.v1.BlockConfirmation;
import com.firefly.core.banking.ledger.interfaces.enums.blockchain.v1.TransactionStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Service interface for interacting with blockchain networks.
 * Provides methods for sending transactions, checking transaction status,
 * tracking confirmations, and estimating gas costs.
 */
public interface BlockchainService {
    
    /**
     * Sends a transaction to the blockchain network.
     *
     * @param fromAddress The sender's blockchain address
     * @param toAddress The recipient's blockchain address
     * @param amount The amount to send
     * @param cryptoAssetId The ID of the crypto asset to send
     * @return A Mono containing the blockchain transaction hash
     */
    Mono<String> sendTransaction(String fromAddress, String toAddress, 
                                BigDecimal amount, Long cryptoAssetId);
    
    /**
     * Gets the current status of a transaction on the blockchain.
     *
     * @param transactionHash The blockchain transaction hash
     * @param blockchainNetworkId The ID of the blockchain network
     * @return A Mono containing the transaction status
     */
    Mono<TransactionStatus> getTransactionStatus(String transactionHash, Long blockchainNetworkId);
    
    /**
     * Tracks confirmations for a transaction on the blockchain.
     * Returns a Flux that emits a new BlockConfirmation each time the confirmation count increases.
     *
     * @param transactionHash The blockchain transaction hash
     * @param blockchainNetworkId The ID of the blockchain network
     * @return A Flux of BlockConfirmation objects
     */
    Flux<BlockConfirmation> trackTransactionConfirmations(String transactionHash, 
                                                        Long blockchainNetworkId);
    
    /**
     * Gets the current gas price on the blockchain network.
     *
     * @param blockchainNetworkId The ID of the blockchain network
     * @return A Mono containing the current gas price in the network's native currency
     */
    Mono<BigDecimal> getGasPrice(Long blockchainNetworkId);
    
    /**
     * Estimates the gas required for a transaction.
     *
     * @param fromAddress The sender's blockchain address
     * @param toAddress The recipient's blockchain address
     * @param amount The amount to send
     * @param cryptoAssetId The ID of the crypto asset to send
     * @return A Mono containing the estimated gas amount
     */
    Mono<BigDecimal> estimateGas(String fromAddress, String toAddress, 
                               BigDecimal amount, Long cryptoAssetId);
}