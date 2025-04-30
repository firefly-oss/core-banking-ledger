package com.catalis.core.banking.ledger.core.services.ledger.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Implementation of the LedgerAuditService interface.
 * This implementation uses in-memory storage for audit entries.
 * In a production environment, this would be replaced with a database-backed repository.
 */
@Service
@Transactional
@Slf4j
public class LedgerAuditServiceImpl implements LedgerAuditService {

    // In-memory storage for audit entries
    private final ConcurrentMap<String, AuditEntry> auditEntries = new ConcurrentHashMap<>();

    @Override
    public Mono<Boolean> logAuditEntry(String operationType, Long entityId, String entityType, String userId, Map<String, Object> details) {
        log.debug("Logging audit entry - Operation: {}, Entity ID: {}, Entity Type: {}, User ID: {}", 
                operationType, entityId, entityType, userId);
        
        try {
            String entryId = UUID.randomUUID().toString();
            AuditEntry entry = new AuditEntry(
                    entryId,
                    operationType,
                    entityId,
                    entityType,
                    userId,
                    details,
                    LocalDateTime.now()
            );
            
            auditEntries.put(entryId, entry);
            return Mono.just(true);
        } catch (Exception e) {
            log.error("Error logging audit entry", e);
            return Mono.just(false);
        }
    }

    @Override
    public Flux<Map<String, Object>> getAuditEntriesForEntity(Long entityId, String entityType) {
        log.debug("Retrieving audit entries for entity ID: {} of type: {}", entityId, entityType);
        
        return Flux.fromIterable(
                auditEntries.values().stream()
                        .filter(entry -> entry.entityId.equals(entityId) && entry.entityType.equals(entityType))
                        .map(this::convertToMap)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Flux<Map<String, Object>> getAuditEntriesForPeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        log.debug("Retrieving audit entries for period from {} to {}", startDateTime, endDateTime);
        
        return Flux.fromIterable(
                auditEntries.values().stream()
                        .filter(entry -> !entry.timestamp.isBefore(startDateTime) && !entry.timestamp.isAfter(endDateTime))
                        .map(this::convertToMap)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Flux<Map<String, Object>> getAuditEntriesForUser(String userId) {
        log.debug("Retrieving audit entries for user ID: {}", userId);
        
        return Flux.fromIterable(
                auditEntries.values().stream()
                        .filter(entry -> entry.userId.equals(userId))
                        .map(this::convertToMap)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Mono<String> generateAuditReport(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        log.debug("Generating audit report for period from {} to {}", startDateTime, endDateTime);
        
        return getAuditEntriesForPeriod(startDateTime, endDateTime)
                .collectList()
                .map(entries -> {
                    StringBuilder report = new StringBuilder();
                    report.append("Audit Report for period: ")
                            .append(startDateTime)
                            .append(" to ")
                            .append(endDateTime)
                            .append("\n\n");
                    
                    entries.forEach(entry -> {
                        report.append("Entry ID: ").append(entry.get("entryId")).append("\n");
                        report.append("Operation: ").append(entry.get("operationType")).append("\n");
                        report.append("Entity ID: ").append(entry.get("entityId")).append("\n");
                        report.append("Entity Type: ").append(entry.get("entityType")).append("\n");
                        report.append("User ID: ").append(entry.get("userId")).append("\n");
                        report.append("Timestamp: ").append(entry.get("timestamp")).append("\n");
                        report.append("Details: ").append(entry.get("details")).append("\n\n");
                    });
                    
                    return report.toString();
                });
    }

    /**
     * Convert an AuditEntry to a Map for external representation
     */
    private Map<String, Object> convertToMap(AuditEntry entry) {
        ConcurrentMap<String, Object> map = new ConcurrentHashMap<>();
        map.put("entryId", entry.entryId);
        map.put("operationType", entry.operationType);
        map.put("entityId", entry.entityId);
        map.put("entityType", entry.entityType);
        map.put("userId", entry.userId);
        map.put("details", entry.details);
        map.put("timestamp", entry.timestamp);
        return map;
    }

    /**
     * Internal class to represent an audit entry
     */
    private static class AuditEntry {
        private final String entryId;
        private final String operationType;
        private final Long entityId;
        private final String entityType;
        private final String userId;
        private final Map<String, Object> details;
        private final LocalDateTime timestamp;

        public AuditEntry(String entryId, String operationType, Long entityId, String entityType, 
                          String userId, Map<String, Object> details, LocalDateTime timestamp) {
            this.entryId = entryId;
            this.operationType = operationType;
            this.entityId = entityId;
            this.entityType = entityType;
            this.userId = userId;
            this.details = details;
            this.timestamp = timestamp;
        }
    }
}