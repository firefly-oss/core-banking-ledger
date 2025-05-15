package com.catalis.core.banking.ledger.core.services.statement.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.statement.v1.StatementDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.statement.v1.StatementMetadataDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.statement.v1.StatementRequestDTO;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Service interface for managing account and account space statements.
 */
public interface StatementService {
    /**
     * Generate a statement for an account.
     *
     * @param accountId The ID of the account.
     * @param requestDTO The statement request parameters.
     * @return The generated statement.
     */
    Mono<StatementDTO> generateAccountStatement(Long accountId, StatementRequestDTO requestDTO);
    
    /**
     * Generate a statement for an account space.
     *
     * @param accountSpaceId The ID of the account space.
     * @param requestDTO The statement request parameters.
     * @return The generated statement.
     */
    Mono<StatementDTO> generateAccountSpaceStatement(Long accountSpaceId, StatementRequestDTO requestDTO);
    
    /**
     * Get a specific statement by ID.
     *
     * @param statementId The ID of the statement.
     * @return The statement metadata.
     */
    Mono<StatementMetadataDTO> getStatement(Long statementId);
    
    /**
     * List statements for an account.
     *
     * @param accountId The ID of the account.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of statement metadata.
     */
    Mono<PaginationResponse<StatementMetadataDTO>> listAccountStatements(Long accountId, PaginationRequest paginationRequest);
    
    /**
     * List statements for an account space.
     *
     * @param accountSpaceId The ID of the account space.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of statement metadata.
     */
    Mono<PaginationResponse<StatementMetadataDTO>> listAccountSpaceStatements(Long accountSpaceId, PaginationRequest paginationRequest);
    
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
            Long accountId, LocalDate startDate, LocalDate endDate, PaginationRequest paginationRequest);
    
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
            Long accountSpaceId, LocalDate startDate, LocalDate endDate, PaginationRequest paginationRequest);
    
    /**
     * Download a statement file.
     *
     * @param statementId The ID of the statement.
     * @return The statement file content as a byte array.
     */
    Mono<byte[]> downloadStatement(Long statementId);
}
