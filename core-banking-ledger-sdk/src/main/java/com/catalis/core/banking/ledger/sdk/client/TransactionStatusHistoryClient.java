package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionStatusHistoryDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Client for interacting with the Transaction Status History API endpoints.
 */
public class TransactionStatusHistoryClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/transaction-status-history";

    /**
     * Constructs a new TransactionStatusHistoryClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public TransactionStatusHistoryClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves a transaction status history entry by its ID.
     *
     * @param statusHistoryId The ID of the status history entry to retrieve.
     * @return A Mono emitting the status history entry.
     */
    public Mono<TransactionStatusHistoryDTO> getStatusHistory(Long statusHistoryId) {
        return get("/" + statusHistoryId, TransactionStatusHistoryDTO.class);
    }

    /**
     * Creates a new transaction status history entry.
     *
     * @param statusHistory The status history entry to create.
     * @return A Mono emitting the created status history entry.
     */
    public Mono<TransactionStatusHistoryDTO> createStatusHistory(TransactionStatusHistoryDTO statusHistory) {
        return post("", statusHistory, TransactionStatusHistoryDTO.class);
    }

    /**
     * Retrieves a paginated list of status history entries for a transaction.
     *
     * @param transactionId     The ID of the transaction.
     * @param paginationRequest The pagination parameters.
     * @return A Mono emitting a paginated response of status history entries.
     */
    public Mono<PaginationResponse<TransactionStatusHistoryDTO>> listStatusHistoryForTransaction(
            Long transactionId, PaginationRequest paginationRequest) {
        
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("transactionId", transactionId);
        
        // Add pagination parameters
        queryParams.put("page", paginationRequest.getPage());
        queryParams.put("size", paginationRequest.getSize());
        
        if (paginationRequest.getSortBy() != null) {
            queryParams.put("sortBy", paginationRequest.getSortBy());
        }
        
        if (paginationRequest.getSortDirection() != null) {
            queryParams.put("sortDirection", paginationRequest.getSortDirection().name());
        }
        
        return get("", queryParams, new ParameterizedTypeReference<PaginationResponse<TransactionStatusHistoryDTO>>() {});
    }
}
