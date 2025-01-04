package com.catalis.core.banking.ledger.core.services.directdebit.v1;

import com.catalis.core.banking.ledger.core.mappers.directdebit.v1.TransactionLineDirectDebitMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionLineDirectDebitDTO;
import com.catalis.core.banking.ledger.models.repositories.directdebit.v1.TransactionLineDirectDebitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineDirectDebitCreateService {

    @Autowired
    private TransactionLineDirectDebitRepository repository;

    @Autowired
    private TransactionLineDirectDebitMapper mapper;

    /**
     * Creates and saves a new transaction line for direct debit based on the provided DTO.
     * The method converts the DTO to an entity, persists it to the database,
     * and then converts it back to a DTO to return.
     *
     * @param transactionLineDirectDebitDTO the DTO representing the transaction line for direct debit to be created
     * @return a Mono containing the created TransactionLineDirectDebitDTO after saving it to the repository
     */
    public Mono<TransactionLineDirectDebitDTO> createTransactionLineDirectDebit(
            TransactionLineDirectDebitDTO transactionLineDirectDebitDTO) {
        return Mono.just(transactionLineDirectDebitDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }


}
