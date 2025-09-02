package com.firefly.core.banking.ledger.models.repositories.core.v1;

import com.firefly.core.banking.ledger.models.entities.core.v1.TransactionStatusHistory;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TransactionStatusHistoryRepository extends BaseRepository<TransactionStatusHistory, UUID> {
    Flux<TransactionStatusHistory> findByTransactionId(UUID transactionId, Pageable pageable);
    Mono<Long> countByTransactionId(UUID transactionId);

    Flux<TransactionStatusHistory> findByTransactionIdOrderByStatusStartDatetimeDesc(UUID transactionId);
    Mono<Long> countByTransactionIdOrderByStatusStartDatetimeDesc(UUID transactionId);
}
