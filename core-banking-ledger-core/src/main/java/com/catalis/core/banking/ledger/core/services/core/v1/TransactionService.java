package com.catalis.core.banking.ledger.core.services.core.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
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
}