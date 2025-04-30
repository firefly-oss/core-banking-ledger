package com.catalis.core.banking.ledger.core.services.ledger.v1;

import com.catalis.core.banking.ledger.interfaces.enums.ledger.v1.LedgerAccountTypeEnum;
import com.catalis.core.banking.ledger.models.repositories.ledger.v1.LedgerAccountRepository;
import com.catalis.core.banking.ledger.models.repositories.ledger.v1.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the LedgerReportingService interface.
 * This implementation provides financial reporting functionality.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LedgerReportingServiceImpl implements LedgerReportingService {

    private final LedgerAccountRepository ledgerAccountRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerBalanceService ledgerBalanceService;

    @Override
    public Mono<String> generateTrialBalanceReport(LocalDate startDate, LocalDate endDate) {
        log.debug("Generating trial balance report from {} to {}", startDate, endDate);
        
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        return ledgerAccountRepository.findAll()
                .flatMap(account -> {
                    return ledgerBalanceService.getBalanceAsOf(account.getLedgerAccountId(), endDateTime)
                            .map(balance -> Map.entry(account, balance));
                })
                .collectList()
                .map(accountBalances -> {
                    StringBuilder report = new StringBuilder();
                    report.append("Trial Balance Report\n");
                    report.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");
                    report.append(String.format("%-40s %-15s %-15s %-15s\n", "Account", "Account Type", "Debit", "Credit"));
                    report.append("------------------------------------------------------------------------------\n");
                    
                    BigDecimal totalDebit = BigDecimal.ZERO;
                    BigDecimal totalCredit = BigDecimal.ZERO;
                    
                    for (var entry : accountBalances) {
                        var account = entry.getKey();
                        var balance = entry.getValue();
                        
                        String debitAmount = "";
                        String creditAmount = "";
                        
                        if (balance.compareTo(BigDecimal.ZERO) > 0) {
                            debitAmount = balance.toString();
                            totalDebit = totalDebit.add(balance);
                        } else if (balance.compareTo(BigDecimal.ZERO) < 0) {
                            creditAmount = balance.abs().toString();
                            totalCredit = totalCredit.add(balance.abs());
                        }
                        
                        report.append(String.format("%-40s %-15s %-15s %-15s\n", 
                                account.getAccountName(), 
                                account.getAccountType(),
                                debitAmount,
                                creditAmount));
                    }
                    
                    report.append("------------------------------------------------------------------------------\n");
                    report.append(String.format("%-40s %-15s %-15s %-15s\n", 
                            "Total", "", totalDebit.toString(), totalCredit.toString()));
                    
                    return report.toString();
                });
    }

    @Override
    public Mono<String> generateIncomeStatementReport(LocalDate startDate, LocalDate endDate) {
        log.debug("Generating income statement report from {} to {}", startDate, endDate);
        
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        return calculateTotalsByAccountType(startDateTime, endDateTime)
                .map(totals -> {
                    StringBuilder report = new StringBuilder();
                    report.append("Income Statement\n");
                    report.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");
                    
                    BigDecimal totalRevenue = totals.getOrDefault(LedgerAccountTypeEnum.INCOME, BigDecimal.ZERO);
                    BigDecimal totalExpenses = totals.getOrDefault(LedgerAccountTypeEnum.EXPENSE, BigDecimal.ZERO);
                    BigDecimal netIncome = totalRevenue.subtract(totalExpenses);
                    
                    report.append("Revenue: ").append(totalRevenue).append("\n");
                    report.append("Expenses: ").append(totalExpenses).append("\n");
                    report.append("Net Income: ").append(netIncome).append("\n");
                    
                    return report.toString();
                });
    }

    @Override
    public Mono<String> generateBalanceSheetReport(LocalDate asOfDate) {
        log.debug("Generating balance sheet report as of {}", asOfDate);
        
        LocalDateTime asOfDateTime = asOfDate.atTime(LocalTime.MAX);
        
        return calculateTotalsByAccountType(null, asOfDateTime)
                .map(totals -> {
                    StringBuilder report = new StringBuilder();
                    report.append("Balance Sheet\n");
                    report.append("As of: ").append(asOfDate).append("\n\n");
                    
                    BigDecimal totalAssets = totals.getOrDefault(LedgerAccountTypeEnum.ASSET, BigDecimal.ZERO);
                    BigDecimal totalLiabilities = totals.getOrDefault(LedgerAccountTypeEnum.LIABILITY, BigDecimal.ZERO);
                    BigDecimal totalEquity = totals.getOrDefault(LedgerAccountTypeEnum.EQUITY, BigDecimal.ZERO);
                    
                    report.append("Assets: ").append(totalAssets).append("\n");
                    report.append("Liabilities: ").append(totalLiabilities).append("\n");
                    report.append("Equity: ").append(totalEquity).append("\n");
                    report.append("Total Liabilities and Equity: ").append(totalLiabilities.add(totalEquity)).append("\n");
                    
                    return report.toString();
                });
    }

    @Override
    public Mono<String> generateCashFlowStatement(LocalDate startDate, LocalDate endDate) {
        log.debug("Generating cash flow statement from {} to {}", startDate, endDate);
        
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        // For a real implementation, we would need to categorize cash flows by operating, investing, and financing activities
        // This is a simplified version that just shows the net change in cash accounts
        
        return ledgerAccountRepository.findAll()
                .filter(account -> account.getAccountType() == LedgerAccountTypeEnum.ASSET && 
                                  account.getAccountName().toLowerCase().contains("cash"))
                .flatMap(cashAccount -> {
                    return Mono.zip(
                            ledgerBalanceService.getBalanceAsOf(cashAccount.getLedgerAccountId(), startDateTime),
                            ledgerBalanceService.getBalanceAsOf(cashAccount.getLedgerAccountId(), endDateTime),
                            (openingBalance, closingBalance) -> {
                                return Map.entry(cashAccount.getAccountName(), 
                                                Map.of("opening", openingBalance, "closing", closingBalance));
                            }
                    );
                })
                .collectList()
                .map(cashFlows -> {
                    StringBuilder report = new StringBuilder();
                    report.append("Cash Flow Statement\n");
                    report.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");
                    
                    BigDecimal totalOpeningBalance = BigDecimal.ZERO;
                    BigDecimal totalClosingBalance = BigDecimal.ZERO;
                    
                    for (var entry : cashFlows) {
                        String accountName = entry.getKey();
                        BigDecimal openingBalance = (BigDecimal) entry.getValue().get("opening");
                        BigDecimal closingBalance = (BigDecimal) entry.getValue().get("closing");
                        BigDecimal netChange = closingBalance.subtract(openingBalance);
                        
                        totalOpeningBalance = totalOpeningBalance.add(openingBalance);
                        totalClosingBalance = totalClosingBalance.add(closingBalance);
                        
                        report.append(accountName).append(":\n");
                        report.append("  Opening Balance: ").append(openingBalance).append("\n");
                        report.append("  Closing Balance: ").append(closingBalance).append("\n");
                        report.append("  Net Change: ").append(netChange).append("\n\n");
                    }
                    
                    BigDecimal totalNetChange = totalClosingBalance.subtract(totalOpeningBalance);
                    report.append("Total Cash Flow: ").append(totalNetChange).append("\n");
                    
                    return report.toString();
                });
    }

    @Override
    public Mono<String> generateCustomReport(String reportType, Map<String, Object> parameters) {
        log.debug("Generating custom report of type: {} with parameters: {}", reportType, parameters);
        
        // This is a placeholder for custom report generation
        // In a real implementation, this would handle various custom report types
        
        return Mono.just("Custom Report: " + reportType + "\n\nParameters: " + parameters);
    }
    
    /**
     * Helper method to calculate totals by account type for a specific period
     */
    private Mono<Map<LedgerAccountTypeEnum, BigDecimal>> calculateTotalsByAccountType(
            LocalDateTime startDateTime, LocalDateTime endDateTime) {
        
        return ledgerAccountRepository.findAll()
                .flatMap(account -> {
                    Mono<BigDecimal> balanceMono;
                    if (startDateTime != null) {
                        // Calculate the difference between start and end balances for the period
                        balanceMono = Mono.zip(
                                ledgerBalanceService.getBalanceAsOf(account.getLedgerAccountId(), startDateTime),
                                ledgerBalanceService.getBalanceAsOf(account.getLedgerAccountId(), endDateTime),
                                (startBalance, endBalance) -> endBalance.subtract(startBalance)
                        );
                    } else {
                        // Just get the balance as of the end date
                        balanceMono = ledgerBalanceService.getBalanceAsOf(account.getLedgerAccountId(), endDateTime);
                    }
                    
                    return balanceMono.map(balance -> Map.entry(account.getAccountType(), balance));
                })
                .collectList()
                .map(entries -> {
                    Map<LedgerAccountTypeEnum, BigDecimal> totals = new ConcurrentHashMap<>();
                    
                    for (var entry : entries) {
                        LedgerAccountTypeEnum accountType = entry.getKey();
                        BigDecimal balance = entry.getValue();
                        
                        totals.compute(accountType, (k, v) -> (v == null) ? balance : v.add(balance));
                    }
                    
                    return totals;
                });
    }
}