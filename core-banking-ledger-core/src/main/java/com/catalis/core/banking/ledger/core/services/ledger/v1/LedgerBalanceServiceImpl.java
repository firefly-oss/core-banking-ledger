package com.catalis.core.banking.ledger.core.services.ledger.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerAccountDTO;
import com.catalis.core.banking.ledger.interfaces.enums.ledger.v1.LedgerAccountTypeEnum;
import com.catalis.core.banking.ledger.interfaces.enums.ledger.v1.LedgerDebitCreditIndicatorEnum;
import com.catalis.core.banking.ledger.models.entities.ledger.v1.LedgerAccount;
import com.catalis.core.banking.ledger.models.entities.ledger.v1.LedgerEntry;
import com.catalis.core.banking.ledger.models.repositories.ledger.v1.LedgerAccountRepository;
import com.catalis.core.banking.ledger.models.repositories.ledger.v1.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Implementation of the LedgerBalanceService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerBalanceServiceImpl implements LedgerBalanceService {

    private final LedgerAccountRepository ledgerAccountRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerAccountService ledgerAccountService;

    @Override
    public Mono<BigDecimal> getCurrentBalance(Long ledgerAccountId) {
        log.debug("Calculating current balance for ledger account ID: {}", ledgerAccountId);
        return ledgerEntryRepository.findAll()
                .filter(entry -> entry.getLedgerAccountId().equals(ledgerAccountId))
                .map(entry -> {
                    BigDecimal amount = entry.getAmount();
                    return entry.getDebitCreditIndicator() == LedgerDebitCreditIndicatorEnum.CREDIT ? amount : amount.negate();
                })
                .reduce(BigDecimal.ZERO, (acc, val) -> acc.add(val));
    }

    @Override
    public Mono<BigDecimal> getBalanceAsOf(Long ledgerAccountId, LocalDateTime asOfDate) {
        log.debug("Calculating balance as of {} for ledger account ID: {}", asOfDate, ledgerAccountId);
        return ledgerEntryRepository.findAll()
                .filter(entry -> entry.getLedgerAccountId().equals(ledgerAccountId) && 
                        entry.getPostingDate().isBefore(asOfDate))
                .map(entry -> {
                    BigDecimal amount = entry.getAmount();
                    return entry.getDebitCreditIndicator() == LedgerDebitCreditIndicatorEnum.CREDIT ? amount : amount.negate();
                })
                .reduce(BigDecimal.ZERO, (acc, val) -> acc.add(val));
    }

    @Override
    public Mono<BigDecimal> getTotalBalanceByAccountType(String accountType) {
        log.debug("Calculating total balance for account type: {}", accountType);
        LedgerAccountTypeEnum typeEnum = LedgerAccountTypeEnum.valueOf(accountType);
        return ledgerAccountRepository.findAll()
                .filter(account -> account.getAccountType() == typeEnum)
                .flatMap(account -> getCurrentBalance(account.getLedgerAccountId()))
                .reduce(BigDecimal.ZERO, (acc, val) -> acc.add(val));
    }

    @Override
    public Mono<LedgerAccountDTO> updateBalance(Long ledgerAccountId, BigDecimal amount, boolean isCredit) {
        log.debug("Updating balance for ledger account ID: {}, amount: {}, isCredit: {}", ledgerAccountId, amount, isCredit);
        return ledgerAccountService.getLedgerAccount(ledgerAccountId)
                .flatMap(account -> {
                    // In a real implementation, we might update a balance field in the account
                    // For now, we just return the account as is
                    return Mono.just(account);
                });
    }
}
