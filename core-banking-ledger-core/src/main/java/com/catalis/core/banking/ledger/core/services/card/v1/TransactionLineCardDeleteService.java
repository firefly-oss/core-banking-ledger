package com.catalis.core.banking.ledger.core.services.card.v1;

import com.catalis.core.banking.ledger.models.repositories.card.v1.TransactionLineCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineCardDeleteService {

    @Autowired
    private TransactionLineCardRepository repository;
    
    /**
     * Deletes a transaction line card by its unique identifier.
     *
     * @param transactionLineCardId the unique identifier of the transaction line card to delete
     * @return a Mono that completes when the deletion is successful
     */
    public Mono<Void> deleteTransactionLineCard(Long transactionLineCardId) {
        return repository.deleteById(transactionLineCardId);
    }
    
}
