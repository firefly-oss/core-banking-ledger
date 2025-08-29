package com.firefly.core.banking.ledger.models.repositories.core.v1;

import com.firefly.core.banking.ledger.models.entities.core.v1.TransactionStatusHistory;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionStatusHistoryRepository extends BaseRepository<TransactionStatusHistory, Long> {
    Flux<TransactionStatusHistory> findByTransactionId(Long transactionId, Pageable pageable);
    Mono<Long> countByTransactionId(Long transactionId);

    Flux<TransactionStatusHistory> findByTransactionIdOrderByStatusStartDatetimeDesc(Long transactionId);
    Mono<Long> countByTransactionIdOrderByStatusStartDatetimeDesc(Long transactionId);
}
