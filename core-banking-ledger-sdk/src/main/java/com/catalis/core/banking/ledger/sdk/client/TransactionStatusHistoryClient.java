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
 * <p>
 * This client provides methods for creating, retrieving, updating, and deleting transaction status history records
 * associated with a specific transaction.
 */
public class TransactionStatusHistoryClient extends BaseClient {

    /**
     * The base path format for transaction status history API endpoints.
     */
    private static final String BASE_PATH_FORMAT = "/api/v1/transactions/%s/status-history";

    /**
     * Constructs a new TransactionStatusHistoryClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for making HTTP requests.
     */
    public TransactionStatusHistoryClient(WebClient webClient) {
        super(webClient, "");  // Base path will be set dynamically for each transaction
    }

    /**
     * Lists all status history records for a specific transaction with pagination.
     *
     * @param transactionId     The ID of the transaction.
     * @param paginationRequest The pagination parameters.
     * @return A Mono that emits a paginated response of status history records.
     */
    public Mono<PaginationResponse<TransactionStatusHistoryDTO>> listStatusHistory(
            Long transactionId, PaginationRequest paginationRequest) {
        
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        
        Map<String, String> queryParams = new HashMap<>();
        if (paginationRequest != null) {
            if (paginationRequest.getPage() != null) {
                queryParams.put("page", paginationRequest.getPage().toString());
            }
            if (paginationRequest.getSize() != null) {
                queryParams.put("size", paginationRequest.getSize().toString());
            }
            if (paginationRequest.getSort() != null && !paginationRequest.getSort().isEmpty()) {
                queryParams.put("sort", paginationRequest.getSort());
                queryParams.put("direction", paginationRequest.getDirection());
            }
        }
        
        return get(path, queryParams, new ParameterizedTypeReference<PaginationResponse<TransactionStatusHistoryDTO>>() {});
    }

    /**
     * Creates a new status history record for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param historyDTO    The status history data to create.
     * @return A Mono that emits the created status history record.
     */
    public Mono<TransactionStatusHistoryDTO> createStatusHistory(Long transactionId, TransactionStatusHistoryDTO historyDTO) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return post(path, historyDTO, TransactionStatusHistoryDTO.class);
    }

    /**
     * Retrieves a specific status history record by its ID.
     *
     * @param transactionId The ID of the transaction.
     * @param historyId     The ID of the status history record.
     * @return A Mono that emits the status history record.
     */
    public Mono<TransactionStatusHistoryDTO> getStatusHistory(Long transactionId, Long historyId) {
        String path = String.format(BASE_PATH_FORMAT, transactionId) + "/" + historyId;
        return get(path, TransactionStatusHistoryDTO.class);
    }

    /**
     * Updates an existing status history record.
     *
     * @param transactionId The ID of the transaction.
     * @param historyId     The ID of the status history record to update.
     * @param historyDTO    The updated status history data.
     * @return A Mono that emits the updated status history record.
     */
    public Mono<TransactionStatusHistoryDTO> updateStatusHistory(
            Long transactionId, Long historyId, TransactionStatusHistoryDTO historyDTO) {
        String path = String.format(BASE_PATH_FORMAT, transactionId) + "/" + historyId;
        return put(path, historyDTO, TransactionStatusHistoryDTO.class);
    }

    /**
     * Deletes a status history record.
     *
     * @param transactionId The ID of the transaction.
     * @param historyId     The ID of the status history record to delete.
     * @return A Mono that completes when the status history record is deleted.
     */
    public Mono<Void> deleteStatusHistory(Long transactionId, Long historyId) {
        String path = String.format(BASE_PATH_FORMAT, transactionId) + "/" + historyId;
        return delete(path);
    }
}