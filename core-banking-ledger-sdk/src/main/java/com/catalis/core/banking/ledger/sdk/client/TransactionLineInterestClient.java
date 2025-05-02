package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.interest.v1.TransactionLineInterestDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line Interest API endpoints.
 */
public class TransactionLineInterestClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/transactions";

    /**
     * Constructs a new TransactionLineInterestClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public TransactionLineInterestClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves the interest line associated with a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono emitting the interest line.
     */
    public Mono<TransactionLineInterestDTO> getInterestLine(Long transactionId) {
        return get("/" + transactionId + "/line-interest", TransactionLineInterestDTO.class);
    }

    /**
     * Creates a new interest line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param interestLine The interest line data to create.
     * @return A Mono emitting the created interest line.
     */
    public Mono<TransactionLineInterestDTO> createInterestLine(Long transactionId, TransactionLineInterestDTO interestLine) {
        return post("/" + transactionId + "/line-interest", interestLine, TransactionLineInterestDTO.class);
    }

    /**
     * Updates the interest line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param interestLine The updated interest line data.
     * @return A Mono emitting the updated interest line.
     */
    public Mono<TransactionLineInterestDTO> updateInterestLine(Long transactionId, TransactionLineInterestDTO interestLine) {
        return put("/" + transactionId + "/line-interest", interestLine, TransactionLineInterestDTO.class);
    }

    /**
     * Deletes the interest line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono completing when the interest line is deleted.
     */
    public Mono<Void> deleteInterestLine(Long transactionId) {
        return delete("/" + transactionId + "/line-interest", Void.class);
    }
}
