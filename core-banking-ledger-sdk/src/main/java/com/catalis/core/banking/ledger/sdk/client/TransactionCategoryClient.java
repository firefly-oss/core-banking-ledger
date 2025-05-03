package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.category.v1.TransactionCategoryDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Client for interacting with the Transaction Category API endpoints.
 * <p>
 * This client provides methods for creating, retrieving, updating, and deleting transaction categories.
 */
public class TransactionCategoryClient extends BaseClient {

    /**
     * The base path for transaction category API endpoints.
     */
    private static final String BASE_PATH = "/api/v1/transaction-categories";

    /**
     * Constructs a new TransactionCategoryClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for making HTTP requests.
     */
    public TransactionCategoryClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Lists all transaction categories with pagination.
     *
     * @param paginationRequest The pagination parameters.
     * @return A Mono that emits a paginated response of transaction categories.
     */
    public Mono<PaginationResponse<TransactionCategoryDTO>> listCategories(PaginationRequest paginationRequest) {
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
        
        return get("", queryParams, new ParameterizedTypeReference<PaginationResponse<TransactionCategoryDTO>>() {});
    }

    /**
     * Creates a new transaction category.
     *
     * @param categoryDTO The transaction category data to create.
     * @return A Mono that emits the created transaction category.
     */
    public Mono<TransactionCategoryDTO> createCategory(TransactionCategoryDTO categoryDTO) {
        return post("", categoryDTO, TransactionCategoryDTO.class);
    }

    /**
     * Retrieves a specific transaction category by its ID.
     *
     * @param categoryId The ID of the transaction category.
     * @return A Mono that emits the transaction category.
     */
    public Mono<TransactionCategoryDTO> getCategory(Long categoryId) {
        return get("/" + categoryId, TransactionCategoryDTO.class);
    }

    /**
     * Updates an existing transaction category.
     *
     * @param categoryId  The ID of the transaction category to update.
     * @param categoryDTO The updated transaction category data.
     * @return A Mono that emits the updated transaction category.
     */
    public Mono<TransactionCategoryDTO> updateCategory(Long categoryId, TransactionCategoryDTO categoryDTO) {
        return put("/" + categoryId, categoryDTO, TransactionCategoryDTO.class);
    }

    /**
     * Deletes a transaction category.
     *
     * @param categoryId The ID of the transaction category to delete.
     * @return A Mono that completes when the transaction category is deleted.
     */
    public Mono<Void> deleteCategory(Long categoryId) {
        return delete("/" + categoryId);
    }
}