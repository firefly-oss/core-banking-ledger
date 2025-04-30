package com.catalis.core.banking.ledger.core.services.ledger.v1;

import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

/**
 * Service for generating financial reports from ledger data.
 */
public interface LedgerReportingService {

    /**
     * Generate a trial balance report for a specific date range.
     *
     * @param startDate The start date for the report period
     * @param endDate The end date for the report period
     * @return A string containing the trial balance report
     */
    Mono<String> generateTrialBalanceReport(LocalDate startDate, LocalDate endDate);

    /**
     * Generate an income statement (profit and loss) report for a specific date range.
     *
     * @param startDate The start date for the report period
     * @param endDate The end date for the report period
     * @return A string containing the income statement report
     */
    Mono<String> generateIncomeStatementReport(LocalDate startDate, LocalDate endDate);

    /**
     * Generate a balance sheet report as of a specific date.
     *
     * @param asOfDate The date for which to generate the balance sheet
     * @return A string containing the balance sheet report
     */
    Mono<String> generateBalanceSheetReport(LocalDate asOfDate);

    /**
     * Generate a cash flow statement for a specific date range.
     *
     * @param startDate The start date for the report period
     * @param endDate The end date for the report period
     * @return A string containing the cash flow statement
     */
    Mono<String> generateCashFlowStatement(LocalDate startDate, LocalDate endDate);

    /**
     * Generate a custom report based on specified parameters.
     *
     * @param reportType The type of report to generate
     * @param parameters Additional parameters for the report
     * @return A string containing the custom report
     */
    Mono<String> generateCustomReport(String reportType, Map<String, Object> parameters);
}