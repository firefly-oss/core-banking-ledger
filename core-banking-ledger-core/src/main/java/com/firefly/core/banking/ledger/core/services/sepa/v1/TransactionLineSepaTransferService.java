package com.firefly.core.banking.ledger.core.services.sepa.v1;

import java.util.UUID;

import com.firefly.core.banking.ledger.interfaces.dtos.sepa.v1.TransactionLineSepaTransferDTO;
import reactor.core.publisher.Mono;

public interface TransactionLineSepaTransferService {

    /**
     * Retrieve the SEPA transfer line for the specified transaction.
     */
    Mono<TransactionLineSepaTransferDTO> getSepaTransferLine(UUID transactionId);

    /**
     * Create a new SEPA transfer line record for the specified transaction.
     */
    Mono<TransactionLineSepaTransferDTO> createSepaTransferLine(UUID transactionId, TransactionLineSepaTransferDTO sepaDTO);

    /**
     * Update an existing SEPA transfer line for the specified transaction.
     */
    Mono<TransactionLineSepaTransferDTO> updateSepaTransferLine(UUID transactionId, TransactionLineSepaTransferDTO sepaDTO);

    /**
     * Delete the SEPA transfer line record for the specified transaction.
     */
    Mono<Void> deleteSepaTransferLine(UUID transactionId);
}