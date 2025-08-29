package com.firefly.core.banking.ledger.core.services.wire.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.wire.v1.TransactionLineWireTransferDTO;
import reactor.core.publisher.Mono;

public interface TransactionLineWireTransferService {

    /**
     * Retrieve the wire transfer line for the specified transaction.
     */
    Mono<TransactionLineWireTransferDTO> getWireTransferLine(Long transactionId);

    /**
     * Create a new wire transfer line record for the specified transaction.
     */
    Mono<TransactionLineWireTransferDTO> createWireTransferLine(Long transactionId, TransactionLineWireTransferDTO wireDTO);

    /**
     * Update an existing wire transfer line for the specified transaction.
     */
    Mono<TransactionLineWireTransferDTO> updateWireTransferLine(Long transactionId, TransactionLineWireTransferDTO wireDTO);

    /**
     * Delete the wire transfer line record for the specified transaction.
     */
    Mono<Void> deleteWireTransferLine(Long transactionId);
}