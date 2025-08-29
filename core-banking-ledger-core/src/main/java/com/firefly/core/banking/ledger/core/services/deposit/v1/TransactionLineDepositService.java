package com.firefly.core.banking.ledger.core.services.deposit.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.deposit.v1.TransactionLineDepositDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing deposit transaction lines.
 */
public interface TransactionLineDepositService {

    /**
     * Retrieve the deposit line for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @return A Mono containing the deposit transaction line if found
     */
    Mono<TransactionLineDepositDTO> getDepositLine(Long transactionId);

    /**
     * Create a new deposit line record for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @param depositDTO The deposit transaction line data
     * @return A Mono containing the created deposit transaction line
     */
    Mono<TransactionLineDepositDTO> createDepositLine(Long transactionId, TransactionLineDepositDTO depositDTO);

    /**
     * Update an existing deposit line for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @param depositDTO The updated deposit transaction line data
     * @return A Mono containing the updated deposit transaction line
     */
    Mono<TransactionLineDepositDTO> updateDepositLine(Long transactionId, TransactionLineDepositDTO depositDTO);

    /**
     * Delete the deposit line record for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @return A Mono that completes when the deletion is done
     */
    Mono<Void> deleteDepositLine(Long transactionId);
}
