package com.firefly.core.banking.ledger.models.repositories.money.v1;

import com.firefly.core.banking.ledger.models.entities.money.v1.Money;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;
/**
 * Repository interface for money values.
 */
public interface MoneyRepository extends BaseRepository<Money, UUID> {
    /**
     * Find all money values with a specific currency.
     */
    Flux<Money> findByCurrency(String currency, Pageable pageable);
    
    /**
     * Count all money values with a specific currency.
     */
    Mono<Long> countByCurrency(String currency);
}
