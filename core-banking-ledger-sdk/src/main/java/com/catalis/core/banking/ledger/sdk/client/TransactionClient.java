package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction API endpoints.
 */
public class TransactionClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/transactions";

    /**
     * Constructs a new TransactionClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public TransactionClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param transactionId The ID of the transaction to retrieve.
     * @return A Mono emitting the transaction.
     */
    public Mono<TransactionDTO> getTransaction(Long transactionId) {
        return get("/" + transactionId, TransactionDTO.class);
    }

    /**
     * Creates a new transaction.
     *
     * @param transaction The transaction to create.
     * @return A Mono emitting the created transaction.
     */
    public Mono<TransactionDTO> createTransaction(TransactionDTO transaction) {
        return post("", transaction, TransactionDTO.class);
    }

    /**
     * Updates an existing transaction.
     *
     * @param transactionId The ID of the transaction to update.
     * @param transaction   The updated transaction data.
     * @return A Mono emitting the updated transaction.
     */
    public Mono<TransactionDTO> updateTransaction(Long transactionId, TransactionDTO transaction) {
        return put("/" + transactionId, transaction, TransactionDTO.class);
    }

    /**
     * Deletes a transaction.
     *
     * @param transactionId The ID of the transaction to delete.
     * @return A Mono completing when the transaction is deleted.
     */
    public Mono<Void> deleteTransaction(Long transactionId) {
        return delete("/" + transactionId, Void.class);
    }

    /**
     * Retrieves a paginated list of transactions.
     *
     * @param paginationRequest The pagination parameters.
     * @return A Mono emitting a paginated response of transactions.
     */
    public Mono<PaginationResponse<TransactionDTO>> listTransactions(PaginationRequest paginationRequest) {
        return getPaginated("", paginationRequest, new ParameterizedTypeReference<PaginationResponse<TransactionDTO>>() {});
    }

    /**
     * Filters transactions based on the provided criteria.
     *
     * @param filterRequest The filter criteria.
     * @return A Mono emitting a paginated response of filtered transactions.
     */
    public Mono<PaginationResponse<TransactionDTO>> filterTransactions(FilterRequest<TransactionDTO> filterRequest) {
        return getFiltered("/filter", filterRequest, new ParameterizedTypeReference<PaginationResponse<TransactionDTO>>() {});
    }
}
