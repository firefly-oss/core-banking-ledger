package com.firefly.core.banking.ledger.core.services.core.v1;

import java.util.UUID;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.ledger.interfaces.dtos.core.v1.TransactionStatusHistoryDTO;
import reactor.core.publisher.Mono;

public interface TransactionStatusHistoryService {

    /**
     * Retrieve a paginated list of transaction status history records for a specific transaction.
     */
    Mono<PaginationResponse<TransactionStatusHistoryDTO>> listStatusHistory(
            UUID transactionId,
            PaginationRequest paginationRequest
    );

    /**
     * Create a new transaction status history record for the specified transaction.
     */
    Mono<TransactionStatusHistoryDTO> createStatusHistory(UUID transactionId, TransactionStatusHistoryDTO historyDTO);

    /**
     * Retrieve a specific transaction status history record by its unique ID.
     */
    Mono<TransactionStatusHistoryDTO> getStatusHistory(UUID transactionId, UUID historyId);

    /**
     * Update an existing transaction status history record.
     */
    Mono<TransactionStatusHistoryDTO> updateStatusHistory(
            UUID transactionId,
            UUID historyId,
            TransactionStatusHistoryDTO historyDTO
    );

    /**
     * Delete a specific transaction status history record by its unique ID.
     */
    Mono<Void> deleteStatusHistory(UUID transactionId, UUID historyId);
}
