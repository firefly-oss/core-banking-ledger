package com.firefly.core.banking.ledger.core.services.deposit.v1;

import com.firefly.core.banking.ledger.core.mappers.deposit.v1.TransactionLineDepositMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.deposit.v1.TransactionLineDepositDTO;
import com.firefly.core.banking.ledger.models.entities.deposit.v1.TransactionLineDeposit;
import com.firefly.core.banking.ledger.models.repositories.deposit.v1.TransactionLineDepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the TransactionLineDepositService interface.
 * Provides functionality for managing deposit transaction lines.
 */
@Service
@Transactional
public class TransactionLineDepositServiceImpl implements TransactionLineDepositService {

    @Autowired
    private TransactionLineDepositRepository repository;

    @Autowired
    private TransactionLineDepositMapper mapper;

    @Override
    public Mono<TransactionLineDepositDTO> getDepositLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line Deposit not found")));
    }

    @Override
    public Mono<TransactionLineDepositDTO> createDepositLine(Long transactionId, TransactionLineDepositDTO depositDTO) {
        depositDTO.setTransactionId(transactionId);
        TransactionLineDeposit entity = mapper.toEntity(depositDTO);
        return repository.save(entity)
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to create Transaction Line Deposit", e)));
    }

    @Override
    public Mono<TransactionLineDepositDTO> updateDepositLine(Long transactionId, TransactionLineDepositDTO depositDTO) {
        return repository.findByTransactionId(transactionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to update Transaction Line Deposit: Entity not found")))
                .flatMap(existingEntity -> {
                    TransactionLineDeposit updatedEntity = mapper.toEntity(depositDTO);
                    updatedEntity.setTransactionLineDepositId(existingEntity.getTransactionLineDepositId());
                    updatedEntity.setTransactionId(transactionId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to update Transaction Line Deposit", e)));
    }

    @Override
    public Mono<Void> deleteDepositLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line Deposit not found")))
                .flatMap(repository::delete);
    }
}
