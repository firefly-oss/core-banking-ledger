package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionLineDirectDebitDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line Direct Debit API endpoints.
 * <p>
 * This client provides methods for creating, retrieving, updating, and deleting direct debit lines
 * associated with a specific transaction.
 */
public class TransactionLineDirectDebitClient extends BaseClient {

    /**
     * The base path format for transaction line direct debit API endpoints.
     */
    private static final String BASE_PATH_FORMAT = "/api/v1/transactions/%s/line-direct-debit";

    /**
     * Constructs a new TransactionLineDirectDebitClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for making HTTP requests.
     */
    public TransactionLineDirectDebitClient(WebClient webClient) {
        super(webClient, "");  // Base path will be set dynamically for each transaction
    }

    /**
     * Retrieves the direct debit line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono that emits the direct debit line.
     */
    public Mono<TransactionLineDirectDebitDTO> getDirectDebitLine(Long transactionId) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return get(path, TransactionLineDirectDebitDTO.class);
    }

    /**
     * Creates a new direct debit line for a specific transaction.
     *
     * @param transactionId   The ID of the transaction.
     * @param directDebitDTO  The direct debit line data to create.
     * @return A Mono that emits the created direct debit line.
     */
    public Mono<TransactionLineDirectDebitDTO> createDirectDebitLine(Long transactionId, TransactionLineDirectDebitDTO directDebitDTO) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return post(path, directDebitDTO, TransactionLineDirectDebitDTO.class);
    }

    /**
     * Updates an existing direct debit line for a specific transaction.
     *
     * @param transactionId   The ID of the transaction.
     * @param directDebitDTO  The updated direct debit line data.
     * @return A Mono that emits the updated direct debit line.
     */
    public Mono<TransactionLineDirectDebitDTO> updateDirectDebitLine(Long transactionId, TransactionLineDirectDebitDTO directDebitDTO) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return put(path, directDebitDTO, TransactionLineDirectDebitDTO.class);
    }

    /**
     * Deletes a direct debit line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono that completes when the direct debit line is deleted.
     */
    public Mono<Void> deleteDirectDebitLine(Long transactionId) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return delete(path);
    }
}