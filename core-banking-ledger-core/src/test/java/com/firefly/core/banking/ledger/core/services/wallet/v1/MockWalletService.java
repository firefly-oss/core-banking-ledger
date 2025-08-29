package com.firefly.core.banking.ledger.core.services.wallet.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.blockchain.v1.CryptoTransaction;
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

/**
 * Mock implementation of the WalletService interface for testing purposes.
 * Simulates wallet operations without actually connecting to any cryptocurrency network.
 */
public class MockWalletService implements WalletService {

    private final Map<String, Long> addressToAccountMap = new ConcurrentHashMap<>();
    private final Map<String, Long> addressToAssetMap = new ConcurrentHashMap<>();
    private final Map<String, BigDecimal> addressBalances = new ConcurrentHashMap<>();
    private final Map<String, Boolean> validAddresses = new ConcurrentHashMap<>();
    private final Map<String, CryptoTransaction> transactions = new ConcurrentHashMap<>();

    /**
     * Initializes the mock service with default values.
     */
    public MockWalletService() {
        // Pre-populate with some test data
        String testAddress1 = "0x1234567890abcdef1234567890abcdef12345678";
        String testAddress2 = "0xabcdef1234567890abcdef1234567890abcdef12";
        
        addressToAccountMap.put(testAddress1, 100L);
        addressToAssetMap.put(testAddress1, 1L);
        addressBalances.put(testAddress1, new BigDecimal("10.5"));
        validAddresses.put(testAddress1, true);
        
        addressToAccountMap.put(testAddress2, 200L);
        addressToAssetMap.put(testAddress2, 1L);
        addressBalances.put(testAddress2, new BigDecimal("5.25"));
        validAddresses.put(testAddress2, true);
        
        // Add some invalid addresses
        validAddresses.put("invalid-address", false);
        validAddresses.put("0xinvalid", false);
    }

    @Override
    public Mono<String> generateAddress(Long accountId, Long cryptoAssetId) {
        // Generate a random Ethereum-style address
        String address = "0x" + UUID.randomUUID().toString().replace("-", "").substring(0, 40);
        
        // Store the mapping
        addressToAccountMap.put(address, accountId);
        addressToAssetMap.put(address, cryptoAssetId);
        addressBalances.put(address, BigDecimal.ZERO);
        validAddresses.put(address, true);
        
        // Simulate network delay
        return Mono.just(address).delayElement(Duration.ofMillis(100));
    }

    @Override
    public Mono<Boolean> validateAddress(String address, Long cryptoAssetId) {
        // Check if the address is in our valid addresses map
        Boolean isValid = validAddresses.getOrDefault(address, false);
        
        // Simulate network delay
        return Mono.just(isValid).delayElement(Duration.ofMillis(50));
    }

    @Override
    public Mono<BigDecimal> getBalance(Long accountId, Long cryptoAssetId) {
        // Find addresses for this account and asset
        BigDecimal totalBalance = BigDecimal.ZERO;
        
        for (Map.Entry<String, Long> entry : addressToAccountMap.entrySet()) {
            String address = entry.getKey();
            Long addrAccountId = entry.getValue();
            Long addrAssetId = addressToAssetMap.get(address);
            
            if (addrAccountId.equals(accountId) && addrAssetId.equals(cryptoAssetId)) {
                BigDecimal balance = addressBalances.getOrDefault(address, BigDecimal.ZERO);
                totalBalance = totalBalance.add(balance);
            }
        }
        
        // Simulate network delay
        return Mono.just(totalBalance).delayElement(Duration.ofMillis(150));
    }

    @Override
    public Flux<CryptoTransaction> getTransactionHistory(Long accountId, Long cryptoAssetId) {
        // Find addresses for this account and asset
        return Flux.fromIterable(addressToAccountMap.entrySet())
                .filter(entry -> entry.getValue().equals(accountId))
                .map(Map.Entry::getKey)
                .filter(address -> addressToAssetMap.get(address).equals(cryptoAssetId))
                .flatMap(address -> getAddressTransactionHistory(address, cryptoAssetId));
    }

    @Override
    public Flux<CryptoTransaction> getAddressTransactionHistory(String address, Long cryptoAssetId) {
        // Check if the address is valid
        if (!validAddresses.getOrDefault(address, false)) {
            return Flux.empty();
        }
        
        // Generate some mock transactions
        return Flux.range(1, 5)
                .map(i -> {
                    String txHash = "0x" + UUID.randomUUID().toString().replace("-", "");
                    
                    CryptoTransaction tx = CryptoTransaction.builder()
                            .transactionHash(txHash)
                            .senderAddress(i % 2 == 0 ? address : "0x" + UUID.randomUUID().toString().replace("-", "").substring(0, 40))
                            .recipientAddress(i % 2 == 0 ? "0x" + UUID.randomUUID().toString().replace("-", "").substring(0, 40) : address)
                            .amount(new BigDecimal(i + ".0"))
                            .transactionFee(new BigDecimal("0.001"))
                            .feeCurrency("ETH")
                            .timestamp(LocalDateTime.now().minusDays(i))
                            .confirmationCount(12 - i)
                            .blockNumber(1000000L + i)
                            .cryptoAssetId(cryptoAssetId)
                            .assetSymbol(cryptoAssetId == 1L ? "BTC" : cryptoAssetId == 2L ? "ETH" : "UNKNOWN")
                            .status(TransactionStatus.CONFIRMED)
                            .build();
                    
                    // Store the transaction
                    transactions.put(txHash, tx);
                    
                    return tx;
                })
                .delayElements(Duration.ofMillis(50)); // Simulate network delay
    }

    @Override
    public Mono<CryptoTransaction> getTransaction(String transactionHash, Long cryptoAssetId) {
        // Return the transaction if it exists
        return Mono.justOrEmpty(transactions.get(transactionHash))
                .delayElement(Duration.ofMillis(75)); // Simulate network delay
    }

    /**
     * Manually set the balance for an address for testing purposes.
     *
     * @param address The blockchain address
     * @param balance The balance to set
     */
    public void setAddressBalance(String address, BigDecimal balance) {
        addressBalances.put(address, balance);
        validAddresses.put(address, true);
    }

    /**
     * Manually add a transaction for testing purposes.
     *
     * @param transaction The transaction to add
     */
    public void addTransaction(CryptoTransaction transaction) {
        transactions.put(transaction.getTransactionHash(), transaction);
    }

    /**
     * Manually set the validity of an address for testing purposes.
     *
     * @param address The blockchain address
     * @param isValid Whether the address is valid
     */
    public void setAddressValidity(String address, boolean isValid) {
        validAddresses.put(address, isValid);
    }
}