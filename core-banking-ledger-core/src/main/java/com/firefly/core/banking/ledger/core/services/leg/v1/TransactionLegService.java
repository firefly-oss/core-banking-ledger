package com.firefly.core.banking.ledger.core.services.leg.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.ledger.interfaces.dtos.leg.v1.TransactionLegDTO;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import java.util.UUID;
/**
 * Service interface for managing transaction legs.
 */
public interface TransactionLegService {
    /**
     * Create a new transaction leg.
     *
     * @param transactionId The ID of the transaction to which the leg belongs.
     * @param legDTO The transaction leg data.
     * @return The created transaction leg.
     */
    Mono<TransactionLegDTO> createTransactionLeg(UUID transactionId, TransactionLegDTO legDTO);

    /**
     * Get a specific transaction leg by ID.
     *
     * @param transactionId The ID of the transaction to which the leg belongs.
     * @param legId The ID of the transaction leg.
     * @return The transaction leg.
     */
    Mono<TransactionLegDTO> getTransactionLeg(UUID transactionId, UUID legId);

    /**
     * List all legs for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of transaction legs.
     */
    Mono<PaginationResponse<TransactionLegDTO>> listTransactionLegs(UUID transactionId, PaginationRequest paginationRequest);

    /**
     * List all legs for a specific account.
     *
     * @param accountId The ID of the account.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of transaction legs.
     */
    Mono<PaginationResponse<TransactionLegDTO>> listAccountLegs(UUID accountId, PaginationRequest paginationRequest);

    /**
     * List all legs for a specific account within a date range.
     *
     * @param accountId The ID of the account.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of transaction legs.
     */
    Mono<PaginationResponse<TransactionLegDTO>> listAccountLegsByDateRange(
            UUID accountId, 
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            PaginationRequest paginationRequest
    );
}
