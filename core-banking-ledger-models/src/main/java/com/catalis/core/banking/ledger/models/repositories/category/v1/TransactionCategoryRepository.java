package com.catalis.core.banking.ledger.models.repositories.category.v1;

import com.catalis.core.banking.ledger.models.entities.category.v1.TransactionCategory;
import com.catalis.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionCategoryRepository extends BaseRepository<TransactionCategory, Long> {
    Flux<TransactionCategory> findByParentCategoryId(Long parentId, Pageable pageable);
    Mono<Long> countByParentCategoryId(Long parentId);
}