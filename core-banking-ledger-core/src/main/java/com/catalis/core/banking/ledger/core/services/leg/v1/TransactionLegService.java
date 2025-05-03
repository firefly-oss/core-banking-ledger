package com.catalis.core.banking.ledger.core.services.leg.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.leg.v1.TransactionLegDTO;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

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
    Mono<TransactionLegDTO> createTransactionLeg(Long transactionId, TransactionLegDTO legDTO);

    /**
     * Get a specific transaction leg by ID.
     *
     * @param transactionId The ID of the transaction to which the leg belongs.
     * @param legId The ID of the transaction leg.
     * @return The transaction leg.
     */
    Mono<TransactionLegDTO> getTransactionLeg(Long transactionId, Long legId);

    /**
     * List all legs for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of transaction legs.
     */
    Mono<PaginationResponse<TransactionLegDTO>> listTransactionLegs(Long transactionId, PaginationRequest paginationRequest);

    /**
     * List all legs for a specific account.
     *
     * @param accountId The ID of the account.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of transaction legs.
     */
    Mono<PaginationResponse<TransactionLegDTO>> listAccountLegs(Long accountId, PaginationRequest paginationRequest);

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
            Long accountId, 
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            PaginationRequest paginationRequest
    );
}
