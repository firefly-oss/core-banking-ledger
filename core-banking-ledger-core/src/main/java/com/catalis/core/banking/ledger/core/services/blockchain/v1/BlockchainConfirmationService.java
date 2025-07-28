package com.catalis.core.banking.ledger.core.services.blockchain.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.blockchain.v1.BlockConfirmation;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.interfaces.enums.blockchain.v1.TransactionStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for tracking blockchain transaction confirmations and updating transaction status.
 * This service monitors blockchain transactions and updates the ledger transaction status
 * based on the confirmation count.
 */
public interface BlockchainConfirmationService {
    
    /**
     * Tracks confirmations for a blockchain transaction and updates the transaction status.
     *
     * @param transactionHash The blockchain transaction hash
     * @param blockchainNetworkId The ID of the blockchain network
     * @return A Flux of updated TransactionDTO objects as confirmation count changes
     */
    Flux<TransactionDTO> trackConfirmations(String transactionHash, Long blockchainNetworkId);
    
    /**
     * Checks the status of a blockchain transaction and updates the ledger transaction accordingly.
     *
     * @param transactionHash The blockchain transaction hash
     * @param blockchainNetworkId The ID of the blockchain network
     * @return A Mono containing the updated transaction
     */
    Mono<TransactionDTO> checkTransactionStatus(String transactionHash, Long blockchainNetworkId);
}