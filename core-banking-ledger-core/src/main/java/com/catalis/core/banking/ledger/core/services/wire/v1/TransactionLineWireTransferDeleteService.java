package com.catalis.core.banking.ledger.core.services.wire.v1;

import com.catalis.core.banking.ledger.models.repositories.wire.v1.TransactionLineWireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineWireTransferDeleteService {

    @Autowired
    private TransactionLineWireRepository repository;

    /**
     * Deletes a transaction line wire transfer by its unique identifier.
     *
     * @param transactionLineWireTransferId the unique identifier of the transaction line wire transfer to be deleted
     * @return a Mono that completes when the transaction line wire transfer is successfully deleted
     */
    public Mono<Void> deleteTransactionLineWireTransfer(Long transactionLineWireTransferId) {
        return repository.deleteById(transactionLineWireTransferId);
    }

}
