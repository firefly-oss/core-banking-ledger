package com.catalis.core.banking.ledger.core.services.ledger.v1;

import com.catalis.core.banking.ledger.interfaces.enums.ledger.v1.LedgerAccountTypeEnum;
import com.catalis.core.banking.ledger.models.entities.ledger.v1.LedgerAccount;
import com.catalis.core.banking.ledger.models.repositories.ledger.v1.LedgerAccountRepository;
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
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LedgerReportingServiceImplTest {

    @Mock
    private LedgerAccountRepository ledgerAccountRepository;

    @Mock
    private LedgerEntryRepository ledgerEntryRepository;

    @Mock
    private LedgerBalanceService ledgerBalanceService;

    @InjectMocks
    private LedgerReportingServiceImpl service;

    private LedgerAccount assetAccount;
    private LedgerAccount liabilityAccount;
    private LedgerAccount equityAccount;
    private LedgerAccount incomeAccount;
    private LedgerAccount expenseAccount;
    private LedgerAccount cashAccount;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @BeforeEach
    void setUp() {
        // Initialize test data
        assetAccount = new LedgerAccount();
        assetAccount.setLedgerAccountId(1L);
        assetAccount.setAccountCode("ASSET001");
        assetAccount.setAccountName("Asset Account");
        assetAccount.setAccountType(LedgerAccountTypeEnum.ASSET);
        assetAccount.setIsActive(true);

        liabilityAccount = new LedgerAccount();
        liabilityAccount.setLedgerAccountId(2L);
        liabilityAccount.setAccountCode("LIAB001");
        liabilityAccount.setAccountName("Liability Account");
        liabilityAccount.setAccountType(LedgerAccountTypeEnum.LIABILITY);
        liabilityAccount.setIsActive(true);

        equityAccount = new LedgerAccount();
        equityAccount.setLedgerAccountId(3L);
        equityAccount.setAccountCode("EQUITY001");
        equityAccount.setAccountName("Equity Account");
        equityAccount.setAccountType(LedgerAccountTypeEnum.EQUITY);
        equityAccount.setIsActive(true);

        incomeAccount = new LedgerAccount();
        incomeAccount.setLedgerAccountId(4L);
        incomeAccount.setAccountCode("INCOME001");
        incomeAccount.setAccountName("Income Account");
        incomeAccount.setAccountType(LedgerAccountTypeEnum.INCOME);
        incomeAccount.setIsActive(true);

        expenseAccount = new LedgerAccount();
        expenseAccount.setLedgerAccountId(5L);
        expenseAccount.setAccountCode("EXPENSE001");
        expenseAccount.setAccountName("Expense Account");
        expenseAccount.setAccountType(LedgerAccountTypeEnum.EXPENSE);
        expenseAccount.setIsActive(true);

        cashAccount = new LedgerAccount();
        cashAccount.setLedgerAccountId(6L);
        cashAccount.setAccountCode("CASH001");
        cashAccount.setAccountName("Cash Account");
        cashAccount.setAccountType(LedgerAccountTypeEnum.ASSET);
        cashAccount.setIsActive(true);

        startDate = LocalDate.now().minusDays(30);
        endDate = LocalDate.now();
        startDateTime = startDate.atStartOfDay();
        endDateTime = endDate.atTime(LocalTime.MAX);
    }

    @Test
    void generateTrialBalanceReport_Success() {
        // Arrange
        when(ledgerAccountRepository.findAll()).thenReturn(Flux.just(
                assetAccount, liabilityAccount, equityAccount, incomeAccount, expenseAccount
        ));

        when(ledgerBalanceService.getBalanceAsOf(eq(assetAccount.getLedgerAccountId()), any(LocalDateTime.class)))
                .thenReturn(Mono.just(BigDecimal.valueOf(1000.00)));
        when(ledgerBalanceService.getBalanceAsOf(eq(liabilityAccount.getLedgerAccountId()), any(LocalDateTime.class)))
                .thenReturn(Mono.just(BigDecimal.valueOf(-500.00)));
        when(ledgerBalanceService.getBalanceAsOf(eq(equityAccount.getLedgerAccountId()), any(LocalDateTime.class)))
                .thenReturn(Mono.just(BigDecimal.valueOf(-300.00)));
        when(ledgerBalanceService.getBalanceAsOf(eq(incomeAccount.getLedgerAccountId()), any(LocalDateTime.class)))
                .thenReturn(Mono.just(BigDecimal.valueOf(-400.00)));
        when(ledgerBalanceService.getBalanceAsOf(eq(expenseAccount.getLedgerAccountId()), any(LocalDateTime.class)))
                .thenReturn(Mono.just(BigDecimal.valueOf(200.00)));

        // Act & Assert
        StepVerifier.create(service.generateTrialBalanceReport(startDate, endDate))
                .expectNextMatches(report ->
                    report.contains("Trial Balance Report") &&
                    report.contains(assetAccount.getAccountName()) &&
                    report.contains(liabilityAccount.getAccountName()) &&
                    report.contains("1000") && // Asset balance
                    report.contains("500") && // Liability balance (absolute value)
                    report.contains("1200") // Total debit
                )
                .verifyComplete();
    }

    @Test
    void generateIncomeStatementReport_Success() {
        // Arrange
        when(ledgerAccountRepository.findAll()).thenReturn(Flux.just(incomeAccount, expenseAccount));

        // For income statement, we need to calculate the difference between start and end balances
        when(ledgerBalanceService.getBalanceAsOf(eq(incomeAccount.getLedgerAccountId()), eq(startDateTime)))
                .thenReturn(Mono.just(BigDecimal.valueOf(-300.00)));
        when(ledgerBalanceService.getBalanceAsOf(eq(incomeAccount.getLedgerAccountId()), eq(endDateTime)))
                .thenReturn(Mono.just(BigDecimal.valueOf(-400.00)));
        when(ledgerBalanceService.getBalanceAsOf(eq(expenseAccount.getLedgerAccountId()), eq(startDateTime)))
                .thenReturn(Mono.just(BigDecimal.valueOf(150.00)));
        when(ledgerBalanceService.getBalanceAsOf(eq(expenseAccount.getLedgerAccountId()), eq(endDateTime)))
                .thenReturn(Mono.just(BigDecimal.valueOf(200.00)));

        // Act & Assert
        StepVerifier.create(service.generateIncomeStatementReport(startDate, endDate))
                .expectNextMatches(report ->
                    report.contains("Income Statement") &&
                    report.contains("Revenue:") &&
                    report.contains("Expenses:") &&
                    report.contains("Net Income:")
                )
                .verifyComplete();
    }

    @Test
    void generateBalanceSheetReport_Success() {
        // Arrange
        when(ledgerAccountRepository.findAll()).thenReturn(Flux.just(
                assetAccount, liabilityAccount, equityAccount
        ));

        when(ledgerBalanceService.getBalanceAsOf(eq(assetAccount.getLedgerAccountId()), any(LocalDateTime.class)))
                .thenReturn(Mono.just(BigDecimal.valueOf(1000.00)));
        when(ledgerBalanceService.getBalanceAsOf(eq(liabilityAccount.getLedgerAccountId()), any(LocalDateTime.class)))
                .thenReturn(Mono.just(BigDecimal.valueOf(-500.00)));
        when(ledgerBalanceService.getBalanceAsOf(eq(equityAccount.getLedgerAccountId()), any(LocalDateTime.class)))
                .thenReturn(Mono.just(BigDecimal.valueOf(-300.00)));

        // Act & Assert
        StepVerifier.create(service.generateBalanceSheetReport(endDate))
                .expectNextMatches(report ->
                    report.contains("Balance Sheet") &&
                    report.contains("Assets: 1000") &&
                    report.contains("Liabilities: 500") && // Absolute value
                    report.contains("Equity: 300") && // Absolute value
                    report.contains("Total Liabilities and Equity: 800") // 500 + 300 = 800
                )
                .verifyComplete();
    }

    @Test
    void generateCashFlowStatement_Success() {
        // Arrange
        when(ledgerAccountRepository.findAll()).thenReturn(Flux.just(cashAccount));

        when(ledgerBalanceService.getBalanceAsOf(eq(cashAccount.getLedgerAccountId()), eq(startDateTime)))
                .thenReturn(Mono.just(BigDecimal.valueOf(500.00)));
        when(ledgerBalanceService.getBalanceAsOf(eq(cashAccount.getLedgerAccountId()), eq(endDateTime)))
                .thenReturn(Mono.just(BigDecimal.valueOf(700.00)));

        // Act & Assert
        StepVerifier.create(service.generateCashFlowStatement(startDate, endDate))
                .expectNextMatches(report ->
                    report.contains("Cash Flow Statement") &&
                    report.contains(cashAccount.getAccountName()) &&
                    report.contains("Opening Balance: 500") &&
                    report.contains("Closing Balance: 700") &&
                    report.contains("Net Change: 200") &&
                    report.contains("Total Cash Flow: 200")
                )
                .verifyComplete();
    }

    @Test
    void generateCustomReport_Success() {
        // Arrange
        String reportType = "CUSTOM_REPORT";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("param1", "value1");
        parameters.put("param2", "value2");

        // Act & Assert
        StepVerifier.create(service.generateCustomReport(reportType, parameters))
                .expectNextMatches(report ->
                    report.contains("Custom Report: CUSTOM_REPORT") &&
                    report.contains("Parameters: {param1=value1, param2=value2}")
                )
                .verifyComplete();
    }
}