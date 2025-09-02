package com.firefly.core.banking.ledger.core.services.card.v1;

import java.util.UUID;

import com.firefly.core.banking.ledger.interfaces.dtos.card.v1.TransactionLineCardDTO;
import reactor.core.publisher.Mono;

public interface TransactionLineCardService {

    /**
     * Retrieve the transaction line card for the specified transaction.
     */
    Mono<TransactionLineCardDTO> getCardLine(UUID transactionId);

    /**
     * Create a new transaction line card record for the specified transaction.
     */
    Mono<TransactionLineCardDTO> createCardLine(UUID transactionId, TransactionLineCardDTO cardDTO);

    /**
     * Update an existing transaction line card for the specified transaction.
     */
    Mono<TransactionLineCardDTO> updateCardLine(UUID transactionId, TransactionLineCardDTO cardDTO);

    /**
     * Delete the transaction line card record for the specified transaction.
     */
    Mono<Void> deleteCardLine(UUID transactionId);
}
