package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.leg.v1.TransactionLegDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Client for interacting with the Transaction Leg API endpoints.
 * <p>
 * This client provides methods for creating, retrieving, and listing transaction legs
 * associated with a specific transaction.
 */
public class TransactionLegClient extends BaseClient {

    /**
     * The base path format for transaction leg API endpoints.
     */
    private static final String BASE_PATH_FORMAT = "/api/v1/transactions/%s/legs";

    /**
     * Constructs a new TransactionLegClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for making HTTP requests.
     */
    public TransactionLegClient(WebClient webClient) {
        super(webClient, "");  // Base path will be set dynamically for each transaction
    }

    /**
     * Creates a new transaction leg for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param legDTO        The transaction leg data to create.
     * @return A Mono that emits the created transaction leg.
     */
    public Mono<TransactionLegDTO> createTransactionLeg(Long transactionId, TransactionLegDTO legDTO) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return post(path, legDTO, TransactionLegDTO.class);
    }

    /**
     * Retrieves a specific transaction leg by its ID.
     *
     * @param transactionId The ID of the transaction.
     * @param legId         The ID of the transaction leg.
     * @return A Mono that emits the transaction leg.
     */
    public Mono<TransactionLegDTO> getTransactionLeg(Long transactionId, Long legId) {
        String path = String.format(BASE_PATH_FORMAT, transactionId) + "/" + legId;
        return get(path, TransactionLegDTO.class);
    }

    /**
     * Lists transaction legs for a specific transaction with pagination.
     *
     * @param transactionId     The ID of the transaction.
     * @param paginationRequest The pagination parameters.
     * @return A Mono that emits a paginated response of transaction legs.
     */
    public Mono<PaginationResponse<TransactionLegDTO>> listTransactionLegs(
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
        
        return get(path, queryParams, new ParameterizedTypeReference<PaginationResponse<TransactionLegDTO>>() {});
    }
}