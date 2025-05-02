package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.transfer.v1.TransactionLineTransferDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line Transfer API endpoints.
 */
public class TransactionLineTransferClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/transactions";

    /**
     * Constructs a new TransactionLineTransferClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public TransactionLineTransferClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves the transfer line associated with a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono emitting the transfer line.
     */
    public Mono<TransactionLineTransferDTO> getTransferLine(Long transactionId) {
        return get("/" + transactionId + "/line-transfer", TransactionLineTransferDTO.class);
    }

    /**
     * Creates a new transfer line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param transferLine The transfer line data to create.
     * @return A Mono emitting the created transfer line.
     */
    public Mono<TransactionLineTransferDTO> createTransferLine(Long transactionId, TransactionLineTransferDTO transferLine) {
        return post("/" + transactionId + "/line-transfer", transferLine, TransactionLineTransferDTO.class);
    }

    /**
     * Updates the transfer line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param transferLine The updated transfer line data.
     * @return A Mono emitting the updated transfer line.
     */
    public Mono<TransactionLineTransferDTO> updateTransferLine(Long transactionId, TransactionLineTransferDTO transferLine) {
        return put("/" + transactionId + "/line-transfer", transferLine, TransactionLineTransferDTO.class);
    }

    /**
     * Deletes the transfer line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono completing when the transfer line is deleted.
     */
    public Mono<Void> deleteTransferLine(Long transactionId) {
        return delete("/" + transactionId + "/line-transfer", Void.class);
    }
}
