package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.sepa.v1.TransactionLineSepaTransferDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line SEPA Transfer API endpoints.
 */
public class TransactionLineSepaTransferClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/transactions";

    /**
     * Constructs a new TransactionLineSepaTransferClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public TransactionLineSepaTransferClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves the SEPA transfer line associated with a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono emitting the SEPA transfer line.
     */
    public Mono<TransactionLineSepaTransferDTO> getSepaTransferLine(Long transactionId) {
        return get("/" + transactionId + "/line-sepa-transfer", TransactionLineSepaTransferDTO.class);
    }

    /**
     * Creates a new SEPA transfer line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param sepaTransferLine The SEPA transfer line data to create.
     * @return A Mono emitting the created SEPA transfer line.
     */
    public Mono<TransactionLineSepaTransferDTO> createSepaTransferLine(Long transactionId, TransactionLineSepaTransferDTO sepaTransferLine) {
        return post("/" + transactionId + "/line-sepa-transfer", sepaTransferLine, TransactionLineSepaTransferDTO.class);
    }

    /**
     * Updates the SEPA transfer line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param sepaTransferLine The updated SEPA transfer line data.
     * @return A Mono emitting the updated SEPA transfer line.
     */
    public Mono<TransactionLineSepaTransferDTO> updateSepaTransferLine(Long transactionId, TransactionLineSepaTransferDTO sepaTransferLine) {
        return put("/" + transactionId + "/line-sepa-transfer", sepaTransferLine, TransactionLineSepaTransferDTO.class);
    }

    /**
     * Deletes the SEPA transfer line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono completing when the SEPA transfer line is deleted.
     */
    public Mono<Void> deleteSepaTransferLine(Long transactionId) {
        return delete("/" + transactionId + "/line-sepa-transfer", Void.class);
    }
}
