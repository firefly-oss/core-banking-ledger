package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.category.v1.TransactionCategoryDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Transaction Category API endpoints.
 */
public class TransactionCategoryClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/transaction-categories";

    /**
     * Constructs a new TransactionCategoryClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public TransactionCategoryClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves a paginated list of transaction categories.
     *
     * @param paginationRequest The pagination parameters.
     * @return A Mono emitting the paginated response of transaction categories.
     */
    public Mono<PaginationResponse<TransactionCategoryDTO>> listCategories(PaginationRequest paginationRequest) {
        return getPaginated("", paginationRequest, new ParameterizedTypeReference<PaginationResponse<TransactionCategoryDTO>>() {});
    }

    /**
     * Creates a new transaction category.
     *
     * @param category The transaction category data to create.
     * @return A Mono emitting the created transaction category.
     */
    public Mono<TransactionCategoryDTO> createCategory(TransactionCategoryDTO category) {
        return post("", category, TransactionCategoryDTO.class);
    }

    /**
     * Retrieves a specific transaction category by its ID.
     *
     * @param categoryId The ID of the transaction category.
     * @return A Mono emitting the transaction category.
     */
    public Mono<TransactionCategoryDTO> getCategory(Long categoryId) {
        return get("/" + categoryId, TransactionCategoryDTO.class);
    }

    /**
     * Updates an existing transaction category.
     *
     * @param categoryId The ID of the transaction category to update.
     * @param category The updated transaction category data.
     * @return A Mono emitting the updated transaction category.
     */
    public Mono<TransactionCategoryDTO> updateCategory(Long categoryId, TransactionCategoryDTO category) {
        return put("/" + categoryId, category, TransactionCategoryDTO.class);
    }

    /**
     * Deletes a transaction category.
     *
     * @param categoryId The ID of the transaction category to delete.
     * @return A Mono completing when the transaction category is deleted.
     */
    public Mono<Void> deleteCategory(Long categoryId) {
        return delete("/" + categoryId, Void.class);
    }
}
