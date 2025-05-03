package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.card.v1.TransactionLineCardDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line Card API endpoints.
 * <p>
 * This client provides methods for creating, retrieving, updating, and deleting card lines
 * associated with a specific transaction.
 */
public class TransactionLineCardClient extends BaseClient {

    /**
     * The base path format for transaction line card API endpoints.
     */
    private static final String BASE_PATH_FORMAT = "/api/v1/transactions/%s/line-card";

    /**
     * Constructs a new TransactionLineCardClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for making HTTP requests.
     */
    public TransactionLineCardClient(WebClient webClient) {
        super(webClient, "");  // Base path will be set dynamically for each transaction
    }

    /**
     * Retrieves the card line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono that emits the card line.
     */
    public Mono<TransactionLineCardDTO> getCardLine(Long transactionId) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return get(path, TransactionLineCardDTO.class);
    }

    /**
     * Creates a new card line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param cardDTO       The card line data to create.
     * @return A Mono that emits the created card line.
     */
    public Mono<TransactionLineCardDTO> createCardLine(Long transactionId, TransactionLineCardDTO cardDTO) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return post(path, cardDTO, TransactionLineCardDTO.class);
    }

    /**
     * Updates an existing card line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param cardDTO       The updated card line data.
     * @return A Mono that emits the updated card line.
     */
    public Mono<TransactionLineCardDTO> updateCardLine(Long transactionId, TransactionLineCardDTO cardDTO) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return put(path, cardDTO, TransactionLineCardDTO.class);
    }

    /**
     * Deletes a card line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono that completes when the card line is deleted.
     */
    public Mono<Void> deleteCardLine(Long transactionId) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return delete(path);
    }
}