package com.catalis.core.banking.ledger.core.services.directdebit.v1;

import com.catalis.core.banking.ledger.core.mappers.directdebit.v1.TransactionLineDirectDebitMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionLineDirectDebitDTO;
import com.catalis.core.banking.ledger.models.entities.directdebit.v1.TransactionLineDirectDebit;
import com.catalis.core.banking.ledger.models.repositories.directdebit.v1.TransactionLineDirectDebitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineDirectDebitServiceImpl implements TransactionLineDirectDebitService {

    @Autowired
    private TransactionLineDirectDebitRepository repository;

    @Autowired
    private TransactionLineDirectDebitMapper mapper;

    @Override
    public Mono<TransactionLineDirectDebitDTO> getDirectDebitLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineDirectDebitDTO> createDirectDebitLine(Long transactionId, TransactionLineDirectDebitDTO directDebitDTO) {
        TransactionLineDirectDebit entity = mapper.toEntity(directDebitDTO);
        entity.setTransactionId(transactionId);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineDirectDebitDTO> updateDirectDebitLine(Long transactionId, TransactionLineDirectDebitDTO directDebitDTO) {
        return repository.findByTransactionId(transactionId)
                .flatMap(existingEntity -> {
                    TransactionLineDirectDebit updatedEntity = mapper.toEntity(directDebitDTO);
                    updatedEntity.setTransactionLineDirectDebitId(existingEntity.getTransactionLineDirectDebitId());
                    updatedEntity.setTransactionId(transactionId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteDirectDebitLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .flatMap(repository::delete);
    }
}
