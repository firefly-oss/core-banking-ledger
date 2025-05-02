package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.wire.v1.TransactionLineWireTransferDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line Wire Transfer API endpoints.
 */
public class TransactionLineWireTransferClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/transactions";

    /**
     * Constructs a new TransactionLineWireTransferClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public TransactionLineWireTransferClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves the wire transfer line associated with a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono emitting the wire transfer line.
     */
    public Mono<TransactionLineWireTransferDTO> getWireTransferLine(Long transactionId) {
        return get("/" + transactionId + "/line-wire-transfer", TransactionLineWireTransferDTO.class);
    }

    /**
     * Creates a new wire transfer line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param wireTransferLine The wire transfer line data to create.
     * @return A Mono emitting the created wire transfer line.
     */
    public Mono<TransactionLineWireTransferDTO> createWireTransferLine(Long transactionId, TransactionLineWireTransferDTO wireTransferLine) {
        return post("/" + transactionId + "/line-wire-transfer", wireTransferLine, TransactionLineWireTransferDTO.class);
    }

    /**
     * Updates the wire transfer line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param wireTransferLine The updated wire transfer line data.
     * @return A Mono emitting the updated wire transfer line.
     */
    public Mono<TransactionLineWireTransferDTO> updateWireTransferLine(Long transactionId, TransactionLineWireTransferDTO wireTransferLine) {
        return put("/" + transactionId + "/line-wire-transfer", wireTransferLine, TransactionLineWireTransferDTO.class);
    }

    /**
     * Deletes the wire transfer line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono completing when the wire transfer line is deleted.
     */
    public Mono<Void> deleteWireTransferLine(Long transactionId) {
        return delete("/" + transactionId + "/line-wire-transfer", Void.class);
    }
}
