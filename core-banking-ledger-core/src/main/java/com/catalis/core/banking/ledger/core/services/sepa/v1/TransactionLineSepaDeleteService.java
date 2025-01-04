package com.catalis.core.banking.ledger.core.services.sepa.v1;

import com.catalis.core.banking.ledger.models.repositories.sepa.v1.TransactionLineSepaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineSepaDeleteService {

    @Autowired
    private TransactionLineSepaRepository repository;

    /**
     * Deletes a SEPA transaction line identified by the given ID.
     *
     * @param transactionLineSepaId the ID of the SEPA transaction line to be deleted
     * @return a {@code Mono<Void>} indicating the completion of the deletion operation
     */
    public Mono<Void> deleteTransactionLineSepa(Long transactionLineSepaId) {
        return repository.deleteById(transactionLineSepaId);
    }

}
