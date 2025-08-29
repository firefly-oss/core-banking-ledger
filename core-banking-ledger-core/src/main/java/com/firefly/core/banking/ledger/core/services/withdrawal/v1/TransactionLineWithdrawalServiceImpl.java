package com.firefly.core.banking.ledger.core.services.withdrawal.v1;

import com.firefly.core.banking.ledger.core.mappers.withdrawal.v1.TransactionLineWithdrawalMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.withdrawal.v1.TransactionLineWithdrawalDTO;
import com.firefly.core.banking.ledger.models.entities.withdrawal.v1.TransactionLineWithdrawal;
import com.firefly.core.banking.ledger.models.repositories.withdrawal.v1.TransactionLineWithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the TransactionLineWithdrawalService interface.
 * Provides functionality for managing withdrawal transaction lines.
 */
@Service
@Transactional
public class TransactionLineWithdrawalServiceImpl implements TransactionLineWithdrawalService {

    @Autowired
    private TransactionLineWithdrawalRepository repository;

    @Autowired
    private TransactionLineWithdrawalMapper mapper;

    @Override
    public Mono<TransactionLineWithdrawalDTO> getWithdrawalLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line Withdrawal not found")));
    }

    @Override
    public Mono<TransactionLineWithdrawalDTO> createWithdrawalLine(Long transactionId, TransactionLineWithdrawalDTO withdrawalDTO) {
        withdrawalDTO.setTransactionId(transactionId);
        TransactionLineWithdrawal entity = mapper.toEntity(withdrawalDTO);
        return repository.save(entity)
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to create Transaction Line Withdrawal", e)));
    }

    @Override
    public Mono<TransactionLineWithdrawalDTO> updateWithdrawalLine(Long transactionId, TransactionLineWithdrawalDTO withdrawalDTO) {
        return repository.findByTransactionId(transactionId)
                .flatMap(existingEntity -> {
                    TransactionLineWithdrawal updatedEntity = mapper.toEntity(withdrawalDTO);
                    updatedEntity.setTransactionLineWithdrawalId(existingEntity.getTransactionLineWithdrawalId());
                    updatedEntity.setTransactionId(transactionId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to update Transaction Line Withdrawal")));
    }

    @Override
    public Mono<Void> deleteWithdrawalLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .flatMap(entity -> repository.delete(entity))
                .switchIfEmpty(Mono.empty());
    }
}
