package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.fee.v1.TransactionLineFeeDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line Fee API endpoints.
 */
public class TransactionLineFeeClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/transactions";

    /**
     * Constructs a new TransactionLineFeeClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public TransactionLineFeeClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves the fee line associated with a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono emitting the fee line.
     */
    public Mono<TransactionLineFeeDTO> getFeeLine(Long transactionId) {
        return get("/" + transactionId + "/line-fee", TransactionLineFeeDTO.class);
    }

    /**
     * Creates a new fee line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param feeLine The fee line data to create.
     * @return A Mono emitting the created fee line.
     */
    public Mono<TransactionLineFeeDTO> createFeeLine(Long transactionId, TransactionLineFeeDTO feeLine) {
        return post("/" + transactionId + "/line-fee", feeLine, TransactionLineFeeDTO.class);
    }

    /**
     * Updates the fee line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param feeLine The updated fee line data.
     * @return A Mono emitting the updated fee line.
     */
    public Mono<TransactionLineFeeDTO> updateFeeLine(Long transactionId, TransactionLineFeeDTO feeLine) {
        return put("/" + transactionId + "/line-fee", feeLine, TransactionLineFeeDTO.class);
    }

    /**
     * Deletes the fee line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono completing when the fee line is deleted.
     */
    public Mono<Void> deleteFeeLine(Long transactionId) {
        return delete("/" + transactionId + "/line-fee", Void.class);
    }
}
