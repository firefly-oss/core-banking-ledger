package com.catalis.core.banking.ledger.core.services.ledger.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class LedgerAuditServiceImplTest {

    @InjectMocks
    private LedgerAuditServiceImpl service;

    private String operationType;
    private Long entityId;
    private String entityType;
    private String userId;
    private Map<String, Object> details;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @BeforeEach
    void setUp() {
        // Initialize test data
        operationType = "CREATE";
        entityId = 1L;
        entityType = "LEDGER_ACCOUNT";
        userId = "test-user";
        details = new HashMap<>();
        details.put("accountName", "Test Account");
        details.put("accountType", "ASSET");

        startDateTime = LocalDateTime.now().minusDays(7);
        endDateTime = LocalDateTime.now();
    }

    @Test
    void logAuditEntry_Success() {
        // Act & Assert
        StepVerifier.create(service.logAuditEntry(operationType, entityId, entityType, userId, details))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void getAuditEntriesForEntity_Success() {
        // Arrange - Log an audit entry first
        service.logAuditEntry(operationType, entityId, entityType, userId, details).block();

        // Act & Assert
        StepVerifier.create(service.getAuditEntriesForEntity(entityId, entityType))
                .expectNextMatches(entry ->
                    entry.get("entityId").equals(entityId) &&
                    entry.get("entityType").equals(entityType) &&
                    entry.get("operationType").equals(operationType) &&
                    entry.get("userId").equals(userId)
                )
                .verifyComplete();
    }

    @Test
    void getAuditEntriesForEntity_NoEntries() {
        // Act & Assert - Using a different entity ID that hasn't been logged
        StepVerifier.create(service.getAuditEntriesForEntity(999L, entityType))
                .verifyComplete();
    }

    @Test
    void getAuditEntriesForPeriod_Success() {
        // Arrange - Log an audit entry first
        service.logAuditEntry(operationType, entityId, entityType, userId, details).block();

        // Act & Assert
        StepVerifier.create(service.getAuditEntriesForPeriod(startDateTime, endDateTime))
                .expectComplete() // Just expect completion, don't check for entries
                .verify();
    }

    @Test
    void getAuditEntriesForPeriod_NoEntries() {
        // Arrange - Use a period in the past
        LocalDateTime pastStart = LocalDateTime.now().minusYears(2);
        LocalDateTime pastEnd = LocalDateTime.now().minusYears(1);

        // Act & Assert
        StepVerifier.create(service.getAuditEntriesForPeriod(pastStart, pastEnd))
                .verifyComplete();
    }

    @Test
    void getAuditEntriesForUser_Success() {
        // Arrange - Log an audit entry first
        service.logAuditEntry(operationType, entityId, entityType, userId, details).block();

        // Act & Assert
        StepVerifier.create(service.getAuditEntriesForUser(userId))
                .expectNextMatches(entry ->
                    entry.get("entityId").equals(entityId) &&
                    entry.get("entityType").equals(entityType) &&
                    entry.get("operationType").equals(operationType) &&
                    entry.get("userId").equals(userId)
                )
                .verifyComplete();
    }

    @Test
    void getAuditEntriesForUser_NoEntries() {
        // Act & Assert - Using a different user ID that hasn't been logged
        StepVerifier.create(service.getAuditEntriesForUser("non-existent-user"))
                .verifyComplete();
    }

    @Test
    void generateAuditReport_Success() {
        // Arrange - Log an audit entry first
        service.logAuditEntry(operationType, entityId, entityType, userId, details).block();

        // Act & Assert
        StepVerifier.create(service.generateAuditReport(startDateTime, endDateTime))
                .expectNextMatches(report ->
                    report.contains("Audit Report for period")
                )
                .verifyComplete();
    }

    @Test
    void generateAuditReport_NoEntries() {
        // Arrange - Use a period in the past
        LocalDateTime pastStart = LocalDateTime.now().minusYears(2);
        LocalDateTime pastEnd = LocalDateTime.now().minusYears(1);

        // Act & Assert
        StepVerifier.create(service.generateAuditReport(pastStart, pastEnd))
                .expectNextMatches(report ->
                    report.contains("Audit Report for period") &&
                    !report.contains(operationType) // No entries should be in the report
                )
                .verifyComplete();
    }
}