package com.catalis.core.banking.ledger.core.services.ledger.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerAccountDTO;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service for managing and calculating ledger account balances.
 */
public interface LedgerBalanceService {

    /**
     * Calculate the current balance for a specific ledger account.
     *
     * @param ledgerAccountId The ID of the ledger account
     * @return The current balance of the account
     */
    Mono<BigDecimal> getCurrentBalance(Long ledgerAccountId);

    /**
     * Calculate the balance for a specific ledger account at a given point in time.
     *
     * @param ledgerAccountId The ID of the ledger account
     * @param asOfDate The date and time for which to calculate the balance
     * @return The balance of the account as of the specified date
     */
    Mono<BigDecimal> getBalanceAsOf(Long ledgerAccountId, LocalDateTime asOfDate);

    /**
     * Calculate the total balance for all accounts of a specific type.
     *
     * @param accountType The type of accounts to include in the calculation
     * @return The total balance for all accounts of the specified type
     */
    Mono<BigDecimal> getTotalBalanceByAccountType(String accountType);

    /**
     * Update the balance of a ledger account after a new entry is posted.
     *
     * @param ledgerAccountId The ID of the ledger account
     * @param amount The amount to add or subtract from the balance
     * @param isCredit Whether the amount is a credit (true) or debit (false)
     * @return The updated account with the new balance
     */
    Mono<LedgerAccountDTO> updateBalance(Long ledgerAccountId, BigDecimal amount, boolean isCredit);
}