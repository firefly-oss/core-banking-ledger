package com.firefly.core.banking.ledger.core.services.core.v1;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.CryptoDepositDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.CryptoTransferDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.CryptoWithdrawalDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.NftTransferDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.TokenBurnDTO;
import com.firefly.core.banking.ledger.interfaces.dtos.crypto.v1.TokenMintDTO;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing financial transactions.
 * Provides methods for creating, retrieving, updating, and deleting transactions,
 * as well as various search and filtering capabilities.
 */
public interface TransactionService {

    /**
     * Creates a new transaction record.
     *
     * @param transactionDTO The transaction data to create
     * @return A Mono emitting the created transaction with its generated ID
     */
    Mono<TransactionDTO> createTransaction(TransactionDTO transactionDTO);

    /**
     * Retrieves a specific transaction by its unique ID.
     *
     * @param transactionId The unique identifier of the transaction
     * @return A Mono emitting the transaction if found, or an empty Mono if not found
     */
    Mono<TransactionDTO> getTransaction(Long transactionId);

    /**
     * Updates an existing transaction by its unique ID.
     *
     * @param transactionId The unique identifier of the transaction to update
     * @param transactionDTO The updated transaction data
     * @return A Mono emitting the updated transaction, or an empty Mono if not found
     */
    Mono<TransactionDTO> updateTransaction(Long transactionId, TransactionDTO transactionDTO);

    /**
     * Deletes a transaction by its unique ID.
     *
     * @param transactionId The unique identifier of the transaction to delete
     * @return A Mono completing when the deletion is done, or completing empty if not found
     */
    Mono<Void> deleteTransaction(Long transactionId);

    /**
     * Filters transactions based on various criteria specified in the filter request.
     *
     * @param filterRequest The filter criteria and pagination parameters
     * @return A Mono emitting a paginated response of transactions matching the filter criteria
     */
    Mono<PaginationResponse<TransactionDTO>> filterTransactions(FilterRequest<TransactionDTO> filterRequest);

    /**
     * Gets all transactions for an account.
     *
     * @param accountId The account ID
     * @return A Flux emitting all transactions for the account
     */
    Flux<TransactionDTO> getTransactionsByAccountId(Long accountId);

    /**
     * Gets all transactions for an account space.
     *
     * @param accountSpaceId The account space ID
     * @return A Flux emitting all transactions for the account space
     */
    Flux<TransactionDTO> getTransactionsByAccountSpaceId(Long accountSpaceId);

    /**
     * Updates the status of a transaction and records the status change in the history.
     *
     * @param transactionId The unique identifier of the transaction
     * @param newStatus The new status to set
     * @param reason The reason for the status change
     * @return A Mono emitting the updated transaction, or an empty Mono if not found
     */
    Mono<TransactionDTO> updateTransactionStatus(Long transactionId, TransactionStatusEnum newStatus, String reason);

    /**
     * Creates a reversal transaction for an existing transaction.
     *
     * @param originalTransactionId The unique identifier of the original transaction
     * @param reason The reason for the reversal
     * @return A Mono emitting the created reversal transaction
     */
    Mono<TransactionDTO> createReversalTransaction(Long originalTransactionId, String reason);

    /**
     * Finds a transaction by its external reference.
     *
     * @param externalReference The external reference of the transaction
     * @return A Mono emitting the transaction if found, or an empty Mono if not found
     */
    Mono<TransactionDTO> findByExternalReference(String externalReference);
    
    // Crypto-specific methods
    
    /**
     * Creates a new crypto deposit transaction.
     *
     * @param depositDTO The crypto deposit data
     * @return A Mono emitting the created transaction with its generated ID
     */
    Mono<TransactionDTO> createCryptoDeposit(CryptoDepositDTO depositDTO);
    
    /**
     * Creates a new crypto withdrawal transaction.
     *
     * @param withdrawalDTO The crypto withdrawal data
     * @return A Mono emitting the created transaction with its generated ID
     */
    Mono<TransactionDTO> createCryptoWithdrawal(CryptoWithdrawalDTO withdrawalDTO);
    
    /**
     * Creates a new crypto transfer transaction between accounts.
     *
     * @param transferDTO The crypto transfer data
     * @return A Mono emitting the created transaction with its generated ID
     */
    Mono<TransactionDTO> createCryptoTransfer(CryptoTransferDTO transferDTO);
    
    /**
     * Creates a new token mint transaction.
     *
     * @param mintDTO The token mint data
     * @return A Mono emitting the created transaction with its generated ID
     */
    Mono<TransactionDTO> createTokenMint(TokenMintDTO mintDTO);
    
    /**
     * Creates a new token burn transaction.
     *
     * @param burnDTO The token burn data
     * @return A Mono emitting the created transaction with its generated ID
     */
    Mono<TransactionDTO> createTokenBurn(TokenBurnDTO burnDTO);
    
    /**
     * Creates a new NFT transfer transaction.
     *
     * @param transferDTO The NFT transfer data
     * @return A Mono emitting the created transaction with its generated ID
     */
    Mono<TransactionDTO> createNftTransfer(NftTransferDTO transferDTO);
    
    /**
     * Gets transactions by blockchain transaction hash.
     *
     * @param blockchainTransactionHash The blockchain transaction hash
     * @return A Flux emitting all transactions with the specified blockchain transaction hash
     */
    Flux<TransactionDTO> getTransactionsByBlockchainHash(String blockchainTransactionHash);
    
    /**
     * Gets crypto transactions for a specific crypto asset.
     *
     * @param cryptoAssetId The ID of the crypto asset
     * @return A Flux emitting all transactions for the specified crypto asset
     */
    Flux<TransactionDTO> getTransactionsByCryptoAssetId(Long cryptoAssetId);
    
    /**
     * Updates the blockchain transaction hash for a transaction.
     *
     * @param transactionId The unique identifier of the transaction
     * @param blockchainTransactionHash The blockchain transaction hash
     * @return A Mono emitting the updated transaction, or an empty Mono if not found
     */
    Mono<TransactionDTO> updateBlockchainTransactionHash(Long transactionId, String blockchainTransactionHash);
    
    /**
     * Updates the confirmation count for a crypto transaction.
     *
     * @param transactionId The unique identifier of the transaction
     * @param confirmationCount The new confirmation count
     * @return A Mono emitting the updated transaction, or an empty Mono if not found
     */
    Mono<TransactionDTO> updateConfirmationCount(Long transactionId, Integer confirmationCount);
}