package com.catalis.core.banking.ledger.core.services.interest.v1;

import com.catalis.core.banking.ledger.core.mappers.interest.v1.TransactionLineInterestMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.interest.v1.TransactionLineInterestDTO;
import com.catalis.core.banking.ledger.models.entities.interest.v1.TransactionLineInterest;
import com.catalis.core.banking.ledger.models.repositories.interest.v1.TransactionLineInterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the TransactionLineInterestService interface.
 * Provides functionality for managing interest transaction lines.
 */
@Service
@Transactional
public class TransactionLineInterestServiceImpl implements TransactionLineInterestService {

    @Autowired
    private TransactionLineInterestRepository repository;

    @Autowired
    private TransactionLineInterestMapper mapper;

    @Override
    public Mono<TransactionLineInterestDTO> getInterestLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line Interest not found")));
    }

    @Override
    public Mono<TransactionLineInterestDTO> createInterestLine(Long transactionId, TransactionLineInterestDTO interestDTO) {
        interestDTO.setTransactionId(transactionId);
        TransactionLineInterest entity = mapper.toEntity(interestDTO);
        return repository.save(entity)
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to create Transaction Line Interest", e)));
    }

    @Override
    public Mono<TransactionLineInterestDTO> updateInterestLine(Long transactionId, TransactionLineInterestDTO interestDTO) {
        return repository.findByTransactionId(transactionId)
                .flatMap(existingEntity -> {
                    TransactionLineInterest updatedEntity = mapper.toEntity(interestDTO);
                    updatedEntity.setTransactionLineInterestId(existingEntity.getTransactionLineInterestId());
                    updatedEntity.setTransactionId(transactionId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to update Transaction Line Interest")));
    }

    @Override
    public Mono<Void> deleteInterestLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .flatMap(entity -> repository.delete(entity))
                .switchIfEmpty(Mono.empty());
    }
}
