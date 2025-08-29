package com.firefly.core.banking.ledger.core.services.wallet.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.blockchain.v1.CryptoTransaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Service interface for interacting with cryptocurrency wallets.
 * Provides methods for generating addresses, validating addresses,
 * checking balances, and retrieving transaction history.
 */
public interface WalletService {
    
    /**
     * Generates a new blockchain address for the specified account and crypto asset.
     *
     * @param accountId The ID of the account
     * @param cryptoAssetId The ID of the crypto asset
     * @return A Mono containing the generated blockchain address
     */
    Mono<String> generateAddress(Long accountId, Long cryptoAssetId);
    
    /**
     * Validates a blockchain address for the specified crypto asset.
     *
     * @param address The blockchain address to validate
     * @param cryptoAssetId The ID of the crypto asset
     * @return A Mono containing a boolean indicating whether the address is valid
     */
    Mono<Boolean> validateAddress(String address, Long cryptoAssetId);
    
    /**
     * Gets the balance of the specified crypto asset for the specified account.
     *
     * @param accountId The ID of the account
     * @param cryptoAssetId The ID of the crypto asset
     * @return A Mono containing the balance
     */
    Mono<BigDecimal> getBalance(Long accountId, Long cryptoAssetId);
    
    /**
     * Gets the transaction history for the specified account and crypto asset.
     *
     * @param accountId The ID of the account
     * @param cryptoAssetId The ID of the crypto asset
     * @return A Flux of CryptoTransaction objects
     */
    Flux<CryptoTransaction> getTransactionHistory(Long accountId, Long cryptoAssetId);
    
    /**
     * Gets the transaction history for the specified blockchain address.
     *
     * @param address The blockchain address
     * @param cryptoAssetId The ID of the crypto asset
     * @return A Flux of CryptoTransaction objects
     */
    Flux<CryptoTransaction> getAddressTransactionHistory(String address, Long cryptoAssetId);
    
    /**
     * Gets the details of a specific transaction by its hash.
     *
     * @param transactionHash The blockchain transaction hash
     * @param cryptoAssetId The ID of the crypto asset
     * @return A Mono containing the CryptoTransaction
     */
    Mono<CryptoTransaction> getTransaction(String transactionHash, Long cryptoAssetId);
}