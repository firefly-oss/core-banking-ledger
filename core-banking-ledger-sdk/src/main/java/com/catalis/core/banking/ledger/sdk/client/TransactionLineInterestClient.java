package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.interest.v1.TransactionLineInterestDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line Interest API endpoints.
 * <p>
 * This client provides methods for creating, retrieving, updating, and deleting interest lines
 * associated with a specific transaction.
 */
public class TransactionLineInterestClient extends BaseClient {

    /**
     * The base path format for transaction line interest API endpoints.
     */
    private static final String BASE_PATH_FORMAT = "/api/v1/transactions/%s/line-interest";

    /**
     * Constructs a new TransactionLineInterestClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for making HTTP requests.
     */
    public TransactionLineInterestClient(WebClient webClient) {
        super(webClient, "");  // Base path will be set dynamically for each transaction
    }

    /**
     * Retrieves the interest line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono that emits the interest line.
     */
    public Mono<TransactionLineInterestDTO> getInterestLine(Long transactionId) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return get(path, TransactionLineInterestDTO.class);
    }

    /**
     * Creates a new interest line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param interestDTO   The interest line data to create.
     * @return A Mono that emits the created interest line.
     */
    public Mono<TransactionLineInterestDTO> createInterestLine(Long transactionId, TransactionLineInterestDTO interestDTO) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return post(path, interestDTO, TransactionLineInterestDTO.class);
    }

    /**
     * Updates an existing interest line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param interestDTO   The updated interest line data.
     * @return A Mono that emits the updated interest line.
     */
    public Mono<TransactionLineInterestDTO> updateInterestLine(Long transactionId, TransactionLineInterestDTO interestDTO) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return put(path, interestDTO, TransactionLineInterestDTO.class);
    }

    /**
     * Deletes an interest line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono that completes when the interest line is deleted.
     */
    public Mono<Void> deleteInterestLine(Long transactionId) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return delete(path);
    }
}