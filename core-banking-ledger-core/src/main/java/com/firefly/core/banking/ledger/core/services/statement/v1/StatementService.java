package com.firefly.core.banking.ledger.core.services.statement.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.ledger.interfaces.dtos.statement.v1.StatementMetadataDTO;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import java.util.UUID;
/**
 * Service interface for managing statement records.
 * Provides basic CRUD operations for statement data persistence.
 */
public interface StatementService {
    /**
     * Get a specific statement by ID.
     *
     * @param statementId The ID of the statement.
     * @return The statement metadata.
     */
    Mono<StatementMetadataDTO> getStatement(UUID statementId);

    /**
     * List statements for an account.
     *
     * @param accountId The ID of the account.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of statement metadata.
     */
    Mono<PaginationResponse<StatementMetadataDTO>> listAccountStatements(UUID accountId, PaginationRequest paginationRequest);

    /**
     * List statements for an account space.
     *
     * @param accountSpaceId The ID of the account space.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of statement metadata.
     */
    Mono<PaginationResponse<StatementMetadataDTO>> listAccountSpaceStatements(UUID accountSpaceId, PaginationRequest paginationRequest);

    /**
     * List statements for an account within a date range.
     *
     * @param accountId The ID of the account.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of statement metadata.
     */
    Mono<PaginationResponse<StatementMetadataDTO>> listAccountStatementsByDateRange(
            UUID accountId, LocalDate startDate, LocalDate endDate, PaginationRequest paginationRequest);

    /**
     * List statements for an account space within a date range.
     *
     * @param accountSpaceId The ID of the account space.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of statement metadata.
     */
    Mono<PaginationResponse<StatementMetadataDTO>> listAccountSpaceStatementsByDateRange(
            UUID accountSpaceId, LocalDate startDate, LocalDate endDate, PaginationRequest paginationRequest);
}
