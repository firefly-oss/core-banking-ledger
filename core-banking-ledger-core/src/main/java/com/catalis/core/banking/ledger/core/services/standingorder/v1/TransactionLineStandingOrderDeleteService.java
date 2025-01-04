package com.catalis.core.banking.ledger.core.services.standingorder.v1;

import com.catalis.core.banking.ledger.models.repositories.standingorder.v1.TransactionLineStandingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineStandingOrderDeleteService {

    @Autowired
    private TransactionLineStandingOrderRepository repository;

    /**
     * Deletes a transaction line standing order associated with the given ID.
     *
     * @param transactionLineStandingOrderId the ID of the transaction line standing order to be deleted
     * @return a {@code Mono<Void>} that completes when the deletion is finished
     */
    public Mono<Void> deleteTransactionLineStandingOrder(Long transactionLineStandingOrderId) {
        return repository.deleteById(transactionLineStandingOrderId);
    }

}
