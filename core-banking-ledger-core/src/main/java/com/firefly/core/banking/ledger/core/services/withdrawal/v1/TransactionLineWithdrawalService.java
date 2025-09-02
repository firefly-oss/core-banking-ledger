package com.firefly.core.banking.ledger.core.services.withdrawal.v1;

import java.util.UUID;

import com.firefly.core.banking.ledger.interfaces.dtos.withdrawal.v1.TransactionLineWithdrawalDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing withdrawal transaction lines.
 */
public interface TransactionLineWithdrawalService {

    /**
     * Retrieve the withdrawal line for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @return A Mono containing the withdrawal transaction line if found
     */
    Mono<TransactionLineWithdrawalDTO> getWithdrawalLine(UUID transactionId);

    /**
     * Create a new withdrawal line record for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @param withdrawalDTO The withdrawal transaction line data
     * @return A Mono containing the created withdrawal transaction line
     */
    Mono<TransactionLineWithdrawalDTO> createWithdrawalLine(UUID transactionId, TransactionLineWithdrawalDTO withdrawalDTO);

    /**
     * Update an existing withdrawal line for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @param withdrawalDTO The updated withdrawal transaction line data
     * @return A Mono containing the updated withdrawal transaction line
     */
    Mono<TransactionLineWithdrawalDTO> updateWithdrawalLine(UUID transactionId, TransactionLineWithdrawalDTO withdrawalDTO);

    /**
     * Delete the withdrawal line record for the specified transaction.
     *
     * @param transactionId The transaction ID
     * @return A Mono that completes when the deletion is done
     */
    Mono<Void> deleteWithdrawalLine(UUID transactionId);
}
