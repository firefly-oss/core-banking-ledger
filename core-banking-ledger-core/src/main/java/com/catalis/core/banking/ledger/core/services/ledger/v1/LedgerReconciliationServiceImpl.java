package com.catalis.core.banking.ledger.core.services.ledger.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerAccountDTO;
import com.catalis.core.banking.ledger.models.repositories.ledger.v1.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of the LedgerReconciliationService interface.
 * This implementation provides account reconciliation functionality.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LedgerReconciliationServiceImpl implements LedgerReconciliationService {

    private final LedgerAccountService ledgerAccountService;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerBalanceService ledgerBalanceService;

    // In-memory storage for reconciliation status
    private final ConcurrentMap<Long, Boolean> reconciledTransactions = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, LocalDateTime> reconciliationDates = new ConcurrentHashMap<>();

    @Override
    public Mono<LedgerAccountDTO> reconcileAccount(Long ledgerAccountId, LocalDate startDate, LocalDate endDate) {
        log.debug("Reconciling account ID: {} for period from {} to {}", ledgerAccountId, startDate, endDate);
        
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        return ledgerAccountService.getLedgerAccount(ledgerAccountId)
                .flatMap(account -> {
                    // Get all entries for the account in the date range
                    return ledgerEntryRepository.findAll()
                            .filter(entry -> entry.getLedgerAccountId().equals(ledgerAccountId) &&
                                    !entry.getPostingDate().isBefore(startDateTime) &&
                                    !entry.getPostingDate().isAfter(endDateTime))
                            .map(entry -> entry.getTransactionId())
                            .distinct()
                            .flatMap(transactionId -> {
                                // Mark each transaction as reconciled
                                reconciledTransactions.put(transactionId, true);
                                reconciliationDates.put(transactionId, LocalDateTime.now());
                                return Mono.just(transactionId);
                            })
                            .collectList()
                            .thenReturn(account);
                });
    }

    @Override
    public Flux<Long> findUnreconciledTransactions(Long ledgerAccountId) {
        log.debug("Finding unreconciled transactions for account ID: {}", ledgerAccountId);
        
        return ledgerEntryRepository.findAll()
                .filter(entry -> entry.getLedgerAccountId().equals(ledgerAccountId))
                .map(entry -> entry.getTransactionId())
                .distinct()
                .filter(transactionId -> !reconciledTransactions.getOrDefault(transactionId, false));
    }

    @Override
    public Mono<Boolean> markTransactionAsReconciled(Long transactionId, LocalDateTime reconciliationDate) {
        log.debug("Marking transaction ID: {} as reconciled at {}", transactionId, reconciliationDate);
        
        try {
            reconciledTransactions.put(transactionId, true);
            reconciliationDates.put(transactionId, reconciliationDate);
            return Mono.just(true);
        } catch (Exception e) {
            log.error("Error marking transaction as reconciled", e);
            return Mono.just(false);
        }
    }

    @Override
    public Mono<String> generateReconciliationReport(Long ledgerAccountId, LocalDate startDate, LocalDate endDate) {
        log.debug("Generating reconciliation report for account ID: {} from {} to {}", ledgerAccountId, startDate, endDate);
        
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        return ledgerAccountService.getLedgerAccount(ledgerAccountId)
                .flatMap(account -> {
                    StringBuilder report = new StringBuilder();
                    report.append("Reconciliation Report\n");
                    report.append("Account: ").append(account.getAccountName()).append(" (").append(account.getAccountCode()).append(")\n");
                    report.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");
                    
                    return ledgerBalanceService.getBalanceAsOf(ledgerAccountId, startDateTime)
                            .flatMap(openingBalance -> {
                                report.append("Opening Balance: ").append(openingBalance).append("\n");
                                
                                return ledgerBalanceService.getBalanceAsOf(ledgerAccountId, endDateTime)
                                        .flatMap(closingBalance -> {
                                            report.append("Closing Balance: ").append(closingBalance).append("\n\n");
                                            
                                            return findUnreconciledTransactions(ledgerAccountId)
                                                    .collectList()
                                                    .map(unreconciledTransactions -> {
                                                        report.append("Unreconciled Transactions: ").append(unreconciledTransactions.size()).append("\n");
                                                        unreconciledTransactions.forEach(txId -> 
                                                            report.append("- Transaction ID: ").append(txId).append("\n")
                                                        );
                                                        
                                                        return report.toString();
                                                    });
                                        });
                            });
                });
    }
}