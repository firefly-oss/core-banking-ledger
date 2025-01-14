package com.catalis.core.banking.ledger.core.services.card.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.card.v1.TransactionLineCardDTO;
import reactor.core.publisher.Mono;

public interface TransactionLineCardService {

    /**
     * Retrieve the transaction line card for the specified transaction.
     */
    Mono<TransactionLineCardDTO> getCardLine(Long transactionId);

    /**
     * Create a new transaction line card record for the specified transaction.
     */
    Mono<TransactionLineCardDTO> createCardLine(Long transactionId, TransactionLineCardDTO cardDTO);

    /**
     * Update an existing transaction line card for the specified transaction.
     */
    Mono<TransactionLineCardDTO> updateCardLine(Long transactionId, TransactionLineCardDTO cardDTO);

    /**
     * Delete the transaction line card record for the specified transaction.
     */
    Mono<Void> deleteCardLine(Long transactionId);
}
