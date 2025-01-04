package com.catalis.core.banking.ledger.core.services.card.v1;

import com.catalis.core.banking.ledger.core.mappers.card.v1.TransactionLineCardMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.card.v1.TransactionLineCardDTO;
import com.catalis.core.banking.ledger.models.repositories.card.v1.TransactionLineCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineCardCreateService {

    @Autowired
    private TransactionLineCardRepository repository;

    @Autowired
    private TransactionLineCardMapper mapper;

    /**
     * Creates a new transaction line card by mapping the provided DTO to an entity,
     * persisting the entity to the database, and then mapping the saved entity back to a DTO.
     *
     * @param transactionLineCardDTO the data transfer object containing details of the transaction line card to be created
     * @return a Mono containing the created TransactionLineCardDTO
     */
    public Mono<TransactionLineCardDTO> createTransactionLineCard(TransactionLineCardDTO transactionLineCardDTO) {
        return Mono.just(transactionLineCardDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

}
