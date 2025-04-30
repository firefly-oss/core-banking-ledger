package com.catalis.core.banking.ledger.core.services.ledger.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerAccountDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Service for reconciling ledger accounts with external systems or statements.
 */
public interface LedgerReconciliationService {

    /**
     * Reconcile a ledger account with an external statement or system.
     *
     * @param ledgerAccountId The ID of the ledger account to reconcile
     * @param startDate The start date for the reconciliation period
     * @param endDate The end date for the reconciliation period
     * @return The reconciled account
     */
    Mono<LedgerAccountDTO> reconcileAccount(Long ledgerAccountId, LocalDate startDate, LocalDate endDate);

    /**
     * Identify unreconciled transactions for a specific ledger account.
     *
     * @param ledgerAccountId The ID of the ledger account
     * @return A flux of unreconciled transaction IDs
     */
    Flux<Long> findUnreconciledTransactions(Long ledgerAccountId);

    /**
     * Mark a transaction as reconciled.
     *
     * @param transactionId The ID of the transaction to mark as reconciled
     * @param reconciliationDate The date when the reconciliation was performed
     * @return A boolean indicating whether the operation was successful
     */
    Mono<Boolean> markTransactionAsReconciled(Long transactionId, LocalDateTime reconciliationDate);

    /**
     * Generate a reconciliation report for a specific ledger account.
     *
     * @param ledgerAccountId The ID of the ledger account
     * @param startDate The start date for the report period
     * @param endDate The end date for the report period
     * @return A string containing the reconciliation report
     */
    Mono<String> generateReconciliationReport(Long ledgerAccountId, LocalDate startDate, LocalDate endDate);
}