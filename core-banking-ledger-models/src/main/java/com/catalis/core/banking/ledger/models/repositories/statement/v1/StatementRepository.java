package com.catalis.core.banking.ledger.models.repositories.statement.v1;

import com.catalis.core.banking.ledger.models.entities.statement.v1.Statement;
import com.catalis.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Repository for managing statement records.
 */
public interface StatementRepository extends BaseRepository<Statement, Long> {
    /**
     * Find statements by account ID.
     *
     * @param accountId The account ID.
     * @param pageable Pagination parameters.
     * @return A flux of statements.
     */
    Flux<Statement> findByAccountId(Long accountId, Pageable pageable);
    
    /**
     * Count statements by account ID.
     *
     * @param accountId The account ID.
     * @return The count of statements.
     */
    Mono<Long> countByAccountId(Long accountId);
    
    /**
     * Find statements by account space ID.
     *
     * @param accountSpaceId The account space ID.
     * @param pageable Pagination parameters.
     * @return A flux of statements.
     */
    Flux<Statement> findByAccountSpaceId(Long accountSpaceId, Pageable pageable);
    
    /**
     * Count statements by account space ID.
     *
     * @param accountSpaceId The account space ID.
     * @return The count of statements.
     */
    Mono<Long> countByAccountSpaceId(Long accountSpaceId);
    
    /**
     * Find statements by account ID and date range.
     *
     * @param accountId The account ID.
     * @param startDate The start date.
     * @param endDate The end date.
     * @param pageable Pagination parameters.
     * @return A flux of statements.
     */
    @Query("SELECT * FROM statement " +
           "WHERE account_id = :accountId " +
           "AND start_date >= :startDate " +
           "AND end_date <= :endDate " +
           "ORDER BY generation_date DESC " +
           "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<Statement> findByAccountIdAndDateRange(Long accountId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find statements by account space ID and date range.
     *
     * @param accountSpaceId The account space ID.
     * @param startDate The start date.
     * @param endDate The end date.
     * @param pageable Pagination parameters.
     * @return A flux of statements.
     */
    @Query("SELECT * FROM statement " +
           "WHERE account_space_id = :accountSpaceId " +
           "AND start_date >= :startDate " +
           "AND end_date <= :endDate " +
           "ORDER BY generation_date DESC " +
           "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<Statement> findByAccountSpaceIdAndDateRange(Long accountSpaceId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
