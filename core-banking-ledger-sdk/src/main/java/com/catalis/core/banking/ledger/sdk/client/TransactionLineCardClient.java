package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.card.v1.TransactionLineCardDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line Card API endpoints.
 */
public class TransactionLineCardClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/transactions";

    /**
     * Constructs a new TransactionLineCardClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public TransactionLineCardClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves the card line associated with a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono emitting the card line.
     */
    public Mono<TransactionLineCardDTO> getCardLine(Long transactionId) {
        return get("/" + transactionId + "/line-card", TransactionLineCardDTO.class);
    }

    /**
     * Creates a card line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param cardLine      The card line to create.
     * @return A Mono emitting the created card line.
     */
    public Mono<TransactionLineCardDTO> createCardLine(Long transactionId, TransactionLineCardDTO cardLine) {
        return post("/" + transactionId + "/line-card", cardLine, TransactionLineCardDTO.class);
    }

    /**
     * Updates the card line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param cardLine      The updated card line data.
     * @return A Mono emitting the updated card line.
     */
    public Mono<TransactionLineCardDTO> updateCardLine(Long transactionId, TransactionLineCardDTO cardLine) {
        return put("/" + transactionId + "/line-card", cardLine, TransactionLineCardDTO.class);
    }

    /**
     * Deletes the card line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono completing when the card line is deleted.
     */
    public Mono<Void> deleteCardLine(Long transactionId) {
        return delete("/" + transactionId + "/line-card", Void.class);
    }
}
