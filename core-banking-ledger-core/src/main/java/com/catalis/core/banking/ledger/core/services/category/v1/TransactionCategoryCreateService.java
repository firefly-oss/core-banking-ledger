package com.catalis.core.banking.ledger.core.services.category.v1;

import com.catalis.core.banking.ledger.core.mappers.category.v1.TransactionCategoryMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.category.v1.TransactionCategoryDTO;
import com.catalis.core.banking.ledger.models.repositories.category.v1.TransactionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionCategoryCreateService {

    @Autowired
    private TransactionCategoryRepository repository;
    
    @Autowired
    private TransactionCategoryMapper mapper;

    /**
     * Creates a new transaction category by mapping the provided DTO to an entity,
     * saving the entity to the repository, and then converting the saved entity back
     * to a DTO.
     *
     * @param transactionCategory the DTO containing details of the transaction category to be created
     * @return a {@link Mono} emitting the created transaction category as a DTO
     */
    public Mono<TransactionCategoryDTO> createTransactionCategory(TransactionCategoryDTO transactionCategory) {
        return Mono.just(transactionCategory)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

}
