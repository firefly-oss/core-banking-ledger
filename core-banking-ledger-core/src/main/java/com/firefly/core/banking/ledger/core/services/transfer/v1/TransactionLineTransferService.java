package com.firefly.core.banking.ledger.core.services.transfer.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.transfer.v1.TransactionLineTransferDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing transfer transaction lines.
 */
public interface TransactionLineTransferService {

    /**
     * Retrieve the transfer line for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @return A Mono containing the transfer transaction line if found
     */
    Mono<TransactionLineTransferDTO> getTransferLine(Long transactionId);

    /**
     * Create a new transfer line record for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @param transferDTO The transfer transaction line data
     * @return A Mono containing the created transfer transaction line
     */
    Mono<TransactionLineTransferDTO> createTransferLine(Long transactionId, TransactionLineTransferDTO transferDTO);

    /**
     * Update an existing transfer line for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @param transferDTO The updated transfer transaction line data
     * @return A Mono containing the updated transfer transaction line
     */
    Mono<TransactionLineTransferDTO> updateTransferLine(Long transactionId, TransactionLineTransferDTO transferDTO);

    /**
     * Delete the transfer line record for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @return A Mono that completes when the deletion is done
     */
    Mono<Void> deleteTransferLine(Long transactionId);
}
