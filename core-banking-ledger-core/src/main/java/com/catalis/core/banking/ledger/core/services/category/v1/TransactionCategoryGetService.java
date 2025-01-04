package com.catalis.core.banking.ledger.core.services.category.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.category.v1.TransactionCategoryMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.category.v1.TransactionCategoryDTO;
import com.catalis.core.banking.ledger.models.repositories.category.v1.TransactionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
public class TransactionCategoryGetService {

    @Autowired
    private TransactionCategoryRepository repository;

    @Autowired
    private TransactionCategoryMapper mapper;

    /**
     * Retrieves a transaction category by its unique ID.
     *
     * @param id the unique identifier of the transaction category to retrieve
     * @return a {@link Mono} emitting the transaction category as a {@link TransactionCategoryDTO},
     *         or empty if the category is not found
     */
    public Mono<TransactionCategoryDTO> getCategory(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    /**
     * Retrieves all transaction categories with pagination support.
     *
     * @param paginationRequest the pagination request containing page number, size, and sorting options
     * @return a {@link Mono} emitting a {@link PaginationResponse} containing a list of {@link TransactionCategoryDTO}
     */
    public Mono<PaginationResponse<TransactionCategoryDTO>> getAllCategories(PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findAllBy(pageable),
                repository::count
        );
    }

    /**
     * Retrieves a paginated list of subcategories for a given parent category ID.
     *
     * @param parentId the unique identifier of the parent category whose subcategories are to be retrieved
     * @param paginationRequest the pagination details including page size and page number
     * @return a {@link Mono} emitting a {@link PaginationResponse} containing a list of {@link TransactionCategoryDTO} objects representing the subcategories
     */
    public Mono<PaginationResponse<TransactionCategoryDTO>> getSubcategories(
            Long parentId,
            PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByParentCategoryId(parentId, pageable),
                () -> repository.countByParentCategoryId(parentId)
        );
    }

}
