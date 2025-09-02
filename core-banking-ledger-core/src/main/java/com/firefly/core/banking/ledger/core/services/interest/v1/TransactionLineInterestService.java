package com.firefly.core.banking.ledger.core.services.interest.v1;

import java.util.UUID;

import com.firefly.core.banking.ledger.interfaces.dtos.interest.v1.TransactionLineInterestDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing interest transaction lines.
 */
public interface TransactionLineInterestService {

    /**
     * Retrieve the interest line for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @return A Mono containing the interest transaction line if found
     */
    Mono<TransactionLineInterestDTO> getInterestLine(UUID transactionId);

    /**
     * Create a new interest line record for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @param interestDTO The interest transaction line data
     * @return A Mono containing the created interest transaction line
     */
    Mono<TransactionLineInterestDTO> createInterestLine(UUID transactionId, TransactionLineInterestDTO interestDTO);

    /**
     * Update an existing interest line for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @param interestDTO The updated interest transaction line data
     * @return A Mono containing the updated interest transaction line
     */
    Mono<TransactionLineInterestDTO> updateInterestLine(UUID transactionId, TransactionLineInterestDTO interestDTO);

    /**
     * Delete the interest line record for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @return A Mono that completes when the deletion is done
     */
    Mono<Void> deleteInterestLine(UUID transactionId);
}
