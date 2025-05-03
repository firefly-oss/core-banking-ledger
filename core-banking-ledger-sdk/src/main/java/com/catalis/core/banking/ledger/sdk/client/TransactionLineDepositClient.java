package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.deposit.v1.TransactionLineDepositDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line Deposit API endpoints.
 * <p>
 * This client provides methods for creating, retrieving, updating, and deleting deposit lines
 * associated with a specific transaction.
 */
public class TransactionLineDepositClient extends BaseClient {

    /**
     * The base path format for transaction line deposit API endpoints.
     */
    private static final String BASE_PATH_FORMAT = "/api/v1/transactions/%s/line-deposit";

    /**
     * Constructs a new TransactionLineDepositClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for making HTTP requests.
     */
    public TransactionLineDepositClient(WebClient webClient) {
        super(webClient, "");  // Base path will be set dynamically for each transaction
    }

    /**
     * Retrieves the deposit line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono that emits the deposit line.
     */
    public Mono<TransactionLineDepositDTO> getDepositLine(Long transactionId) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return get(path, TransactionLineDepositDTO.class);
    }

    /**
     * Creates a new deposit line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param depositDTO    The deposit line data to create.
     * @return A Mono that emits the created deposit line.
     */
    public Mono<TransactionLineDepositDTO> createDepositLine(Long transactionId, TransactionLineDepositDTO depositDTO) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return post(path, depositDTO, TransactionLineDepositDTO.class);
    }

    /**
     * Updates an existing deposit line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param depositDTO    The updated deposit line data.
     * @return A Mono that emits the updated deposit line.
     */
    public Mono<TransactionLineDepositDTO> updateDepositLine(Long transactionId, TransactionLineDepositDTO depositDTO) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return put(path, depositDTO, TransactionLineDepositDTO.class);
    }

    /**
     * Deletes a deposit line for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono that completes when the deposit line is deleted.
     */
    public Mono<Void> deleteDepositLine(Long transactionId) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return delete(path);
    }
}