package com.catalis.core.banking.ledger.core.services.ledger.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerAccountDTO;
import com.catalis.core.banking.ledger.interfaces.enums.ledger.v1.LedgerAccountTypeEnum;
import com.catalis.core.banking.ledger.interfaces.enums.ledger.v1.LedgerDebitCreditIndicatorEnum;
import com.catalis.core.banking.ledger.models.entities.ledger.v1.LedgerEntry;
import com.catalis.core.banking.ledger.models.repositories.ledger.v1.LedgerEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LedgerReconciliationServiceImplTest {

    @Mock
    private LedgerAccountService ledgerAccountService;

    @Mock
    private LedgerEntryRepository ledgerEntryRepository;

    @Mock
    private LedgerBalanceService ledgerBalanceService;

    @InjectMocks
    private LedgerReconciliationServiceImpl service;

    private LedgerAccountDTO accountDTO;
    private LedgerEntry entry1;
    private LedgerEntry entry2;
    private final Long accountId = 1L;
    private final Long transactionId1 = 101L;
    private final Long transactionId2 = 102L;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @BeforeEach
    void setUp() {
        // Initialize test data
        accountDTO = new LedgerAccountDTO();
        accountDTO.setLedgerAccountId(accountId);
        accountDTO.setAccountCode("ACC001");
        accountDTO.setAccountName("Cash Account");
        accountDTO.setAccountType(LedgerAccountTypeEnum.ASSET);
        accountDTO.setIsActive(true);

        entry1 = new LedgerEntry();
        entry1.setLedgerEntryId(1L);
        entry1.setTransactionId(transactionId1);
        entry1.setLedgerAccountId(accountId);
        entry1.setDebitCreditIndicator(LedgerDebitCreditIndicatorEnum.DEBIT);
        entry1.setAmount(BigDecimal.valueOf(100.00));
        entry1.setCurrency("USD");
        entry1.setPostingDate(LocalDateTime.now().minusDays(2));

        entry2 = new LedgerEntry();
        entry2.setLedgerEntryId(2L);
        entry2.setTransactionId(transactionId2);
        entry2.setLedgerAccountId(accountId);
        entry2.setDebitCreditIndicator(LedgerDebitCreditIndicatorEnum.CREDIT);
        entry2.setAmount(BigDecimal.valueOf(50.00));
        entry2.setCurrency("USD");
        entry2.setPostingDate(LocalDateTime.now().minusDays(1));

        startDate = LocalDate.now().minusDays(7);
        endDate = LocalDate.now();
        startDateTime = startDate.atStartOfDay();
        endDateTime = endDate.atTime(LocalTime.MAX);
    }

    @Test
    void reconcileAccount_Success() {
        // Arrange
        when(ledgerAccountService.getLedgerAccount(accountId)).thenReturn(Mono.just(accountDTO));
        when(ledgerEntryRepository.findAll()).thenReturn(Flux.just(entry1, entry2));

        // Act & Assert
        StepVerifier.create(service.reconcileAccount(accountId, startDate, endDate))
                .expectNext(accountDTO)
                .verifyComplete();
    }

    @Test
    void findUnreconciledTransactions_Success() {
        // Arrange
        when(ledgerEntryRepository.findAll()).thenReturn(Flux.just(entry1, entry2));

        // Act & Assert - Both transactions should be unreconciled initially
        StepVerifier.create(service.findUnreconciledTransactions(accountId))
                .expectNext(transactionId1)
                .expectNext(transactionId2)
                .verifyComplete();
    }

    @Test
    void markTransactionAsReconciled_Success() {
        // Act & Assert
        StepVerifier.create(service.markTransactionAsReconciled(transactionId1, LocalDateTime.now()))
                .expectNext(true)
                .verifyComplete();

        // Verify that the transaction is now reconciled
        when(ledgerEntryRepository.findAll()).thenReturn(Flux.just(entry1));
        StepVerifier.create(service.findUnreconciledTransactions(accountId))
                .verifyComplete(); // No unreconciled transactions should be found
    }

    @Test
    void generateReconciliationReport_Success() {
        // Arrange
        when(ledgerAccountService.getLedgerAccount(accountId)).thenReturn(Mono.just(accountDTO));
        when(ledgerBalanceService.getBalanceAsOf(eq(accountId), any(LocalDateTime.class)))
                .thenReturn(Mono.just(BigDecimal.valueOf(100.00))); // Opening balance
        when(ledgerBalanceService.getBalanceAsOf(eq(accountId), eq(endDateTime)))
                .thenReturn(Mono.just(BigDecimal.valueOf(150.00))); // Closing balance
        when(ledgerEntryRepository.findAll()).thenReturn(Flux.just(entry1, entry2));

        // Act & Assert
        StepVerifier.create(service.generateReconciliationReport(accountId, startDate, endDate))
                .expectNextMatches(report -> 
                    report.contains("Reconciliation Report") &&
                    report.contains(accountDTO.getAccountName()) &&
                    report.contains("Opening Balance: 100") &&
                    report.contains("Closing Balance: 150") &&
                    report.contains("Unreconciled Transactions: 2")
                )
                .verifyComplete();
    }

    @Test
    void generateReconciliationReport_NoUnreconciledTransactions() {
        // Arrange
        when(ledgerAccountService.getLedgerAccount(accountId)).thenReturn(Mono.just(accountDTO));
        when(ledgerBalanceService.getBalanceAsOf(eq(accountId), any(LocalDateTime.class)))
                .thenReturn(Mono.just(BigDecimal.valueOf(100.00))); // Opening balance
        when(ledgerBalanceService.getBalanceAsOf(eq(accountId), eq(endDateTime)))
                .thenReturn(Mono.just(BigDecimal.valueOf(150.00))); // Closing balance
        
        // Mark transactions as reconciled
        service.markTransactionAsReconciled(transactionId1, LocalDateTime.now()).block();
        service.markTransactionAsReconciled(transactionId2, LocalDateTime.now()).block();
        
        when(ledgerEntryRepository.findAll()).thenReturn(Flux.just(entry1, entry2));

        // Act & Assert
        StepVerifier.create(service.generateReconciliationReport(accountId, startDate, endDate))
                .expectNextMatches(report -> 
                    report.contains("Reconciliation Report") &&
                    report.contains(accountDTO.getAccountName()) &&
                    report.contains("Opening Balance: 100") &&
                    report.contains("Closing Balance: 150") &&
                    report.contains("Unreconciled Transactions: 0")
                )
                .verifyComplete();
    }
}