package com.catalis.core.banking.ledger.core.services.core.v1;

import com.catalis.core.banking.ledger.models.repositories.core.v1.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionDeleteService {

    @Autowired
    private TransactionRepository repository;

    /**
     * Deletes a transaction by its unique identifier.
     *
     * @param transactionId the unique identifier of the transaction to be deleted
     * @return a {@code Mono<Void>} that completes when the transaction is successfully deleted or emits an error if the operation fails
     */
    public Mono<Void> deleteTransaction(Long transactionId) {
        return repository.deleteById(transactionId);
    }

}