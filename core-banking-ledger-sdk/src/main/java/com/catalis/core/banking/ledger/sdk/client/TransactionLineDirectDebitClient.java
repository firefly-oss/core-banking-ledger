package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionLineDirectDebitDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Line Direct Debit API endpoints.
 */
public class TransactionLineDirectDebitClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/transactions";

    /**
     * Constructs a new TransactionLineDirectDebitClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public TransactionLineDirectDebitClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves the direct debit line associated with a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono emitting the direct debit line.
     */
    public Mono<TransactionLineDirectDebitDTO> getDirectDebitLine(Long transactionId) {
        return get("/" + transactionId + "/line-direct-debit", TransactionLineDirectDebitDTO.class);
    }

    /**
     * Creates a direct debit line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param directDebitLine The direct debit line to create.
     * @return A Mono emitting the created direct debit line.
     */
    public Mono<TransactionLineDirectDebitDTO> createDirectDebitLine(Long transactionId, TransactionLineDirectDebitDTO directDebitLine) {
        return post("/" + transactionId + "/line-direct-debit", directDebitLine, TransactionLineDirectDebitDTO.class);
    }

    /**
     * Updates the direct debit line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param directDebitLine The updated direct debit line data.
     * @return A Mono emitting the updated direct debit line.
     */
    public Mono<TransactionLineDirectDebitDTO> updateDirectDebitLine(Long transactionId, TransactionLineDirectDebitDTO directDebitLine) {
        return put("/" + transactionId + "/line-direct-debit", directDebitLine, TransactionLineDirectDebitDTO.class);
    }

    /**
     * Deletes the direct debit line for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @return A Mono completing when the direct debit line is deleted.
     */
    public Mono<Void> deleteDirectDebitLine(Long transactionId) {
        return delete("/" + transactionId + "/line-direct-debit", Void.class);
    }
}
