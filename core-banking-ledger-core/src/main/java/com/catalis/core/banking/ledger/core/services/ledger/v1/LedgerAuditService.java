package com.catalis.core.banking.ledger.core.services.ledger.v1;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Service for auditing ledger operations and maintaining an audit trail.
 */
public interface LedgerAuditService {

    /**
     * Log an audit entry for a ledger operation.
     *
     * @param operationType The type of operation being audited
     * @param entityId The ID of the entity being modified
     * @param entityType The type of entity being modified
     * @param userId The ID of the user performing the operation
     * @param details Additional details about the operation
     * @return A boolean indicating whether the audit entry was successfully logged
     */
    Mono<Boolean> logAuditEntry(String operationType, Long entityId, String entityType, String userId, Map<String, Object> details);

    /**
     * Retrieve audit entries for a specific entity.
     *
     * @param entityId The ID of the entity
     * @param entityType The type of entity
     * @return A flux of audit entries for the specified entity
     */
    Flux<Map<String, Object>> getAuditEntriesForEntity(Long entityId, String entityType);

    /**
     * Retrieve audit entries for a specific time period.
     *
     * @param startDateTime The start date and time for the audit period
     * @param endDateTime The end date and time for the audit period
     * @return A flux of audit entries for the specified time period
     */
    Flux<Map<String, Object>> getAuditEntriesForPeriod(LocalDateTime startDateTime, LocalDateTime endDateTime);

    /**
     * Retrieve audit entries for a specific user.
     *
     * @param userId The ID of the user
     * @return A flux of audit entries for the specified user
     */
    Flux<Map<String, Object>> getAuditEntriesForUser(String userId);

    /**
     * Generate an audit report for a specific time period.
     *
     * @param startDateTime The start date and time for the report period
     * @param endDateTime The end date and time for the report period
     * @return A string containing the audit report
     */
    Mono<String> generateAuditReport(LocalDateTime startDateTime, LocalDateTime endDateTime);
}