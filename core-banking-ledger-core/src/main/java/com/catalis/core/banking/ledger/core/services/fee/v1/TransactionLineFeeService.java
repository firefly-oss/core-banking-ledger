package com.catalis.core.banking.ledger.core.services.fee.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.fee.v1.TransactionLineFeeDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing fee transaction lines.
 */
public interface TransactionLineFeeService {

    /**
     * Retrieve the fee line for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @return A Mono containing the fee transaction line if found
     */
    Mono<TransactionLineFeeDTO> getFeeLine(Long transactionId);

    /**
     * Create a new fee line record for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @param feeDTO The fee transaction line data
     * @return A Mono containing the created fee transaction line
     */
    Mono<TransactionLineFeeDTO> createFeeLine(Long transactionId, TransactionLineFeeDTO feeDTO);

    /**
     * Update an existing fee line for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @param feeDTO The updated fee transaction line data
     * @return A Mono containing the updated fee transaction line
     */
    Mono<TransactionLineFeeDTO> updateFeeLine(Long transactionId, TransactionLineFeeDTO feeDTO);

    /**
     * Delete the fee line record for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @return A Mono that completes when the deletion is done
     */
    Mono<Void> deleteFeeLine(Long transactionId);
}
