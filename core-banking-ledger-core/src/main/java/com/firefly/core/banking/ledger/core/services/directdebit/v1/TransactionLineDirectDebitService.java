package com.firefly.core.banking.ledger.core.services.directdebit.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionLineDirectDebitDTO;
import reactor.core.publisher.Mono;

public interface TransactionLineDirectDebitService {

    /**
     * Retrieve the transaction line direct debit for the specified transaction.
     */
    Mono<TransactionLineDirectDebitDTO> getDirectDebitLine(Long transactionId);

    /**
     * Create a new direct debit line record for the specified transaction.
     */
    Mono<TransactionLineDirectDebitDTO> createDirectDebitLine(Long transactionId, TransactionLineDirectDebitDTO directDebitDTO);

    /**
     * Update an existing direct debit line for the specified transaction.
     */
    Mono<TransactionLineDirectDebitDTO> updateDirectDebitLine(Long transactionId, TransactionLineDirectDebitDTO directDebitDTO);

    /**
     * Delete the direct debit line record for the specified transaction.
     */
    Mono<Void> deleteDirectDebitLine(Long transactionId);
}

