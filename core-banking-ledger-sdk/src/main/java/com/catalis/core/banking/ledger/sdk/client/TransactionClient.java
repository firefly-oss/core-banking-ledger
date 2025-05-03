package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Client for interacting with the Transaction API endpoints.
 * <p>
 * This client provides methods for creating, retrieving, updating, and deleting transactions,
 * as well as filtering transactions, updating transaction status, and creating reversal transactions.
 */
public class TransactionClient extends BaseClient {

    /**
     * The base path for transaction API endpoints.
     */
    private static final String BASE_PATH = "/api/v1/transactions";

    /**
     * Constructs a new TransactionClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for making HTTP requests.
     */
    public TransactionClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Creates a new transaction.
     *
     * @param transactionDTO The transaction data to create.
     * @return A Mono that emits the created transaction.
     */
    public Mono<TransactionDTO> createTransaction(TransactionDTO transactionDTO) {
        return post("", transactionDTO, TransactionDTO.class);
    }

    /**
     * Filters transactions based on the provided filter criteria.
     *
     * @param filterRequest The filter criteria and pagination parameters.
     * @return A Mono that emits a paginated response of transactions.
     */
    public Mono<PaginationResponse<TransactionDTO>> filterTransactions(FilterRequest<TransactionDTO> filterRequest) {
        return postFiltered("/filter", filterRequest, 
                new ParameterizedTypeReference<PaginationResponse<TransactionDTO>>() {});
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param transactionId The ID of the transaction to retrieve.
     * @return A Mono that emits the transaction.
     */
    public Mono<TransactionDTO> getTransaction(Long transactionId) {
        return get("/" + transactionId, TransactionDTO.class);
    }

    /**
     * Updates an existing transaction.
     *
     * @param transactionId  The ID of the transaction to update.
     * @param transactionDTO The updated transaction data.
     * @return A Mono that emits the updated transaction.
     */
    public Mono<TransactionDTO> updateTransaction(Long transactionId, TransactionDTO transactionDTO) {
        return put("/" + transactionId, transactionDTO, TransactionDTO.class);
    }

    /**
     * Deletes a transaction.
     *
     * @param transactionId The ID of the transaction to delete.
     * @return A Mono that completes when the transaction is deleted.
     */
    public Mono<Void> deleteTransaction(Long transactionId) {
        return delete("/" + transactionId);
    }

    /**
     * Updates the status of a transaction.
     *
     * @param transactionId The ID of the transaction to update.
     * @param newStatus     The new status to set.
     * @param reason        The reason for the status change.
     * @return A Mono that emits the updated transaction.
     */
    public Mono<TransactionDTO> updateTransactionStatus(Long transactionId, TransactionStatusEnum newStatus, String reason) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("newStatus", newStatus.name());
        queryParams.put("reason", reason);
        return patch("/" + transactionId + "/status", queryParams, TransactionDTO.class);
    }

    /**
     * Creates a reversal transaction for an existing transaction.
     *
     * @param transactionId The ID of the original transaction.
     * @param reason        The reason for the reversal.
     * @return A Mono that emits the created reversal transaction.
     */
    public Mono<TransactionDTO> createReversalTransaction(Long transactionId, String reason) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("reason", reason);
        return post("/" + transactionId + "/reversal", queryParams, null, TransactionDTO.class);
    }

    /**
     * Finds a transaction by its external reference.
     *
     * @param externalReference The external reference of the transaction.
     * @return A Mono that emits the transaction.
     */
    public Mono<TransactionDTO> findByExternalReference(String externalReference) {
        return get("/by-reference/" + externalReference, TransactionDTO.class);
    }
}