package com.catalis.core.banking.ledger.models.repositories.ledger.v1;

import com.catalis.core.banking.ledger.models.entities.ledger.v1.LedgerEntry;
import com.catalis.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LedgerEntryRepository extends BaseRepository<LedgerEntry, Long> {
    Flux<LedgerEntry> findByTransactionIdAndLedgerAccountId(Long transactionId, Long ledgerAccountId, Pageable pageable);
    Mono<Long> countByTransactionIdAndLedgerAccountId(Long transactionId, Long ledgerAccountId);
}
