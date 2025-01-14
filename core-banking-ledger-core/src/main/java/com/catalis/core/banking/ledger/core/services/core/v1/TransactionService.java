package com.catalis.core.banking.ledger.core.services.core.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import reactor.core.publisher.Mono;

public interface TransactionService {

    /**
     * Create a new transaction record.
     */
    Mono<TransactionDTO> createTransaction(TransactionDTO transactionDTO);

    /**
     * Filter transactions based on various criteria in FilterRequest.
     */
    Mono<PaginationResponse<TransactionDTO>> filterTransactions(FilterRequest<TransactionDTO> filterRequest);

    /**
     * Retrieve a specific transaction by its unique ID.
     */
    Mono<TransactionDTO> getTransaction(Long transactionId);

    /**
     * Update an existing transaction by its unique ID.
     */
    Mono<TransactionDTO> updateTransaction(Long transactionId, TransactionDTO transactionDTO);

    /**
     * Delete a transaction by its unique ID.
     */
    Mono<Void> deleteTransaction(Long transactionId);
}
