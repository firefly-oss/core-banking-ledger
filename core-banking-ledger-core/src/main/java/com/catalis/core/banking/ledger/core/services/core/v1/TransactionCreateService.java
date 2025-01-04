package com.catalis.core.banking.ledger.core.services.core.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.core.v1.TransactionMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.models.repositories.core.v1.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionCreateService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private TransactionMapper mapper;

    /**
     * Creates a new transaction by transforming the provided TransactionDTO object into an entity,
     * saving it to the repository, and mapping the saved entity back to a TransactionDTO object.
     *
     * @param transactionDTO the transaction data transfer object containing details of the transaction to be created
     * @return a Mono emitting the saved transaction as a TransactionDTO object
     */
    public Mono<TransactionDTO> createTransaction(TransactionDTO transactionDTO) {
        return Mono.just(transactionDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }
}
