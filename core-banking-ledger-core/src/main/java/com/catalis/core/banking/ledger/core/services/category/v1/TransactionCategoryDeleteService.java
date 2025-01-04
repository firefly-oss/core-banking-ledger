package com.catalis.core.banking.ledger.core.services.category.v1;

import com.catalis.core.banking.ledger.models.repositories.category.v1.TransactionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionCategoryDeleteService {

    @Autowired
    private TransactionCategoryRepository repository;

    /**
     * Deletes a transaction category identified by its ID.
     *
     * @param transactionCategoryId the unique identifier of the transaction category to be deleted
     * @return a {@link Mono} completing with {@code null} once the deletion is successful
     */
    public Mono<Void> deleteTransactionCategory(Long transactionCategoryId) {
        return repository.deleteById(transactionCategoryId);
    }

}
