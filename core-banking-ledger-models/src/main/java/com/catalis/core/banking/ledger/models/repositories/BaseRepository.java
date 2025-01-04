package com.catalis.core.banking.ledger.models.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends ReactiveCrudRepository<T, ID> {
    Flux<T> findAllBy(Pageable pageable);
    Mono<Long> count();
}