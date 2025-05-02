package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.deposit.v1.TransactionLineDepositDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line Deposit API endpoints.
 */
public class TransactionLineDepositClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/transactions";

    /**
     * Constructs a new TransactionLineDepositClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public TransactionLineDepositClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves the deposit line associated with a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono emitting the deposit line.
     */
    public Mono<TransactionLineDepositDTO> getDepositLine(Long transactionId) {
        return get("/" + transactionId + "/line-deposit", TransactionLineDepositDTO.class);
    }

    /**
     * Creates a new deposit line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param depositLine The deposit line data to create.
     * @return A Mono emitting the created deposit line.
     */
    public Mono<TransactionLineDepositDTO> createDepositLine(Long transactionId, TransactionLineDepositDTO depositLine) {
        return post("/" + transactionId + "/line-deposit", depositLine, TransactionLineDepositDTO.class);
    }

    /**
     * Updates the deposit line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param depositLine The updated deposit line data.
     * @return A Mono emitting the updated deposit line.
     */
    public Mono<TransactionLineDepositDTO> updateDepositLine(Long transactionId, TransactionLineDepositDTO depositLine) {
        return put("/" + transactionId + "/line-deposit", depositLine, TransactionLineDepositDTO.class);
    }

    /**
     * Deletes the deposit line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono completing when the deposit line is deleted.
     */
    public Mono<Void> deleteDepositLine(Long transactionId) {
        return delete("/" + transactionId + "/line-deposit", Void.class);
    }
}
