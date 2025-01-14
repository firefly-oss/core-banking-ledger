package com.catalis.core.banking.ledger.core.services.category.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.category.v1.TransactionCategoryDTO;
import reactor.core.publisher.Mono;

public interface TransactionCategoryService {

    /**
     * Retrieve a paginated list of transaction categories.
     */
    Mono<PaginationResponse<TransactionCategoryDTO>> listCategories(PaginationRequest paginationRequest);

    /**
     * Create a new transaction category.
     */
    Mono<TransactionCategoryDTO> createCategory(TransactionCategoryDTO dto);

    /**
     * Retrieve a specific transaction category by its ID.
     */
    Mono<TransactionCategoryDTO> getCategory(Long categoryId);

    /**
     * Update an existing transaction category by its ID.
     */
    Mono<TransactionCategoryDTO> updateCategory(Long categoryId, TransactionCategoryDTO dto);

    /**
     * Delete a transaction category by its ID.
     */
    Mono<Void> deleteCategory(Long categoryId);
}
