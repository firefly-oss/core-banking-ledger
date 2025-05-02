package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.withdrawal.v1.TransactionLineWithdrawalDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line Withdrawal API endpoints.
 */
public class TransactionLineWithdrawalClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/transactions";

    /**
     * Constructs a new TransactionLineWithdrawalClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public TransactionLineWithdrawalClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves the withdrawal line associated with a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono emitting the withdrawal line.
     */
    public Mono<TransactionLineWithdrawalDTO> getWithdrawalLine(Long transactionId) {
        return get("/" + transactionId + "/line-withdrawal", TransactionLineWithdrawalDTO.class);
    }

    /**
     * Creates a new withdrawal line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param withdrawalLine The withdrawal line data to create.
     * @return A Mono emitting the created withdrawal line.
     */
    public Mono<TransactionLineWithdrawalDTO> createWithdrawalLine(Long transactionId, TransactionLineWithdrawalDTO withdrawalLine) {
        return post("/" + transactionId + "/line-withdrawal", withdrawalLine, TransactionLineWithdrawalDTO.class);
    }

    /**
     * Updates the withdrawal line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param withdrawalLine The updated withdrawal line data.
     * @return A Mono emitting the updated withdrawal line.
     */
    public Mono<TransactionLineWithdrawalDTO> updateWithdrawalLine(Long transactionId, TransactionLineWithdrawalDTO withdrawalLine) {
        return put("/" + transactionId + "/line-withdrawal", withdrawalLine, TransactionLineWithdrawalDTO.class);
    }

    /**
     * Deletes the withdrawal line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono completing when the withdrawal line is deleted.
     */
    public Mono<Void> deleteWithdrawalLine(Long transactionId) {
        return delete("/" + transactionId + "/line-withdrawal", Void.class);
    }
}
