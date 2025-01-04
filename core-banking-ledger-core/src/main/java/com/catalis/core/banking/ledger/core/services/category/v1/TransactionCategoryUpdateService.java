package com.catalis.core.banking.ledger.core.services.category.v1;

import com.catalis.core.banking.ledger.core.mappers.category.v1.TransactionCategoryMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.category.v1.TransactionCategoryDTO;
import com.catalis.core.banking.ledger.models.repositories.category.v1.TransactionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionCategoryUpdateService {

    @Autowired
    private TransactionCategoryRepository categoryRepository;

    @Autowired
    private TransactionCategoryMapper categoryMapper;

    /**
     * Updates an existing transaction category based on the provided ID with the details
     * from the given TransactionCategoryDTO. If the category with the specified ID is not found,
     * an error is returned. The method maps the updated category entity back to a DTO.
     *
     * @param transactionCategoryId the ID of the transaction category to update
     * @param transactionCategoryDTO the DTO representing the updated details of the transaction category
     * @return a Mono emitting the updated TransactionCategoryDTO, or an error if the transaction category
     *         does not exist or an unexpected issue occurs during the update process
     */
    public Mono<TransactionCategoryDTO> updateTransactionCategory(
            Long transactionCategoryId, TransactionCategoryDTO transactionCategoryDTO) {
        return categoryRepository.findById(transactionCategoryId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "TransactionCategory with ID " + transactionCategoryId + " not found")))
                .flatMap(existingCategory -> {
                    // Update all fields of the existing entity using the DTO
                    existingCategory.setTransactionCategoryId(transactionCategoryDTO.getTransactionCategoryId());
                    existingCategory.setCategoryName(transactionCategoryDTO.getCategoryName());
                    existingCategory.setCategoryDescription(transactionCategoryDTO.getCategoryDescription());
                    existingCategory.setCategoryType(transactionCategoryDTO.getCategoryType());
                    existingCategory.setParentCategoryId(transactionCategoryDTO.getParentCategoryId());
                    // Save the updated TransactionCategory entity
                    return categoryRepository.save(existingCategory);
                })
                .map(categoryMapper::toDTO) // Map the updated entity to the DTO
                .onErrorResume(e -> {
                    // Handle and propagate errors gracefully
                    return Mono.error(new RuntimeException(
                            "Error occurred while updating TransactionCategory", e));
                });
    }

}
