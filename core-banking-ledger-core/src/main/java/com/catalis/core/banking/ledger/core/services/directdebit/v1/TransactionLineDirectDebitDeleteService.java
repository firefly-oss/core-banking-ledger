package com.catalis.core.banking.ledger.core.services.directdebit.v1;

import com.catalis.core.banking.ledger.models.repositories.directdebit.v1.TransactionLineDirectDebitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineDirectDebitDeleteService {

    @Autowired
    private TransactionLineDirectDebitRepository repository;

    /**
     * Deletes a transaction line direct debit identified by the provided ID.
     * Handles errors by wrapping them in a RuntimeException.
     *
     * @param transactionLineDirectDebitId the ID of the transaction line direct debit to delete
     * @return a {@code Mono<Void>} that completes when the deletion is successful,
     *         or completes with an error if the operation fails
     */
    public Mono<Void> deleteTransactionLineDirectDebit(Long transactionLineDirectDebitId) {
        return repository.deleteById(transactionLineDirectDebitId)
                .onErrorResume(ex -> {
                    // Log the error or handle it appropriately
                    return Mono.error(new RuntimeException("Failed to delete transaction line direct debit", ex));
                });
    }

}
