package com.firefly.core.banking.ledger.core.services.sepa.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.sepa.v1.TransactionLineSepaTransferDTO;
import reactor.core.publisher.Mono;

public interface TransactionLineSepaTransferService {

    /**
     * Retrieve the SEPA transfer line for the specified transaction.
     */
    Mono<TransactionLineSepaTransferDTO> getSepaTransferLine(Long transactionId);

    /**
     * Create a new SEPA transfer line record for the specified transaction.
     */
    Mono<TransactionLineSepaTransferDTO> createSepaTransferLine(Long transactionId, TransactionLineSepaTransferDTO sepaDTO);

    /**
     * Update an existing SEPA transfer line for the specified transaction.
     */
    Mono<TransactionLineSepaTransferDTO> updateSepaTransferLine(Long transactionId, TransactionLineSepaTransferDTO sepaDTO);

    /**
     * Delete the SEPA transfer line record for the specified transaction.
     */
    Mono<Void> deleteSepaTransferLine(Long transactionId);
}