package com.firefly.core.banking.ledger.core.services.ach.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.ach.v1.TransactionLineAchDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for managing ACH transaction lines.
 */
public interface TransactionLineAchService {

    /**
     * Retrieve the ACH line for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @return A Mono containing the ACH transaction line if found
     */
    Mono<TransactionLineAchDTO> getAchLine(UUID transactionId);

    /**
     * Create a new ACH line record for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @param achDTO The ACH transaction line data
     * @return A Mono containing the created ACH transaction line
     */
    Mono<TransactionLineAchDTO> createAchLine(UUID transactionId, TransactionLineAchDTO achDTO);

    /**
     * Update an existing ACH line for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @param achDTO The updated ACH transaction line data
     * @return A Mono containing the updated ACH transaction line
     */
    Mono<TransactionLineAchDTO> updateAchLine(UUID transactionId, TransactionLineAchDTO achDTO);

    /**
     * Delete the ACH line record for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @return A Mono that completes when the ACH line is deleted
     */
    Mono<Void> deleteAchLine(UUID transactionId);
}
