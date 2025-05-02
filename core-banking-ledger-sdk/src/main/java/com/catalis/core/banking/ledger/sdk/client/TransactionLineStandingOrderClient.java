package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionLineStandingOrderDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line Standing Order API endpoints.
 */
public class TransactionLineStandingOrderClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/transactions";

    /**
     * Constructs a new TransactionLineStandingOrderClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public TransactionLineStandingOrderClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves the standing order line associated with a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono emitting the standing order line.
     */
    public Mono<TransactionLineStandingOrderDTO> getStandingOrderLine(Long transactionId) {
        return get("/" + transactionId + "/line-standing-order", TransactionLineStandingOrderDTO.class);
    }

    /**
     * Creates a standing order line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param standingOrderLine The standing order line to create.
     * @return A Mono emitting the created standing order line.
     */
    public Mono<TransactionLineStandingOrderDTO> createStandingOrderLine(Long transactionId, TransactionLineStandingOrderDTO standingOrderLine) {
        return post("/" + transactionId + "/line-standing-order", standingOrderLine, TransactionLineStandingOrderDTO.class);
    }

    /**
     * Updates the standing order line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param standingOrderLine The updated standing order line data.
     * @return A Mono emitting the updated standing order line.
     */
    public Mono<TransactionLineStandingOrderDTO> updateStandingOrderLine(Long transactionId, TransactionLineStandingOrderDTO standingOrderLine) {
        return put("/" + transactionId + "/line-standing-order", standingOrderLine, TransactionLineStandingOrderDTO.class);
    }

    /**
     * Deletes the standing order line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono completing when the standing order line is deleted.
     */
    public Mono<Void> deleteStandingOrderLine(Long transactionId) {
        return delete("/" + transactionId + "/line-standing-order", Void.class);
    }
}
