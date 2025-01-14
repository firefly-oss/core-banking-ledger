package com.catalis.core.banking.ledger.core.services.core.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionStatusHistoryDTO;
import reactor.core.publisher.Mono;

public interface TransactionStatusHistoryService {

    /**
     * Retrieve a paginated list of transaction status history records for a specific transaction.
     */
    Mono<PaginationResponse<TransactionStatusHistoryDTO>> listStatusHistory(
            Long transactionId,
            PaginationRequest paginationRequest
    );

    /**
     * Create a new transaction status history record for the specified transaction.
     */
    Mono<TransactionStatusHistoryDTO> createStatusHistory(Long transactionId, TransactionStatusHistoryDTO historyDTO);

    /**
     * Retrieve a specific transaction status history record by its unique ID.
     */
    Mono<TransactionStatusHistoryDTO> getStatusHistory(Long transactionId, Long historyId);

    /**
     * Update an existing transaction status history record.
     */
    Mono<TransactionStatusHistoryDTO> updateStatusHistory(
            Long transactionId,
            Long historyId,
            TransactionStatusHistoryDTO historyDTO
    );

    /**
     * Delete a specific transaction status history record by its unique ID.
     */
    Mono<Void> deleteStatusHistory(Long transactionId, Long historyId);
}
