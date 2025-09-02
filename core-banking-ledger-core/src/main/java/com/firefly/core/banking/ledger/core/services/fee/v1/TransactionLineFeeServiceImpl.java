package com.firefly.core.banking.ledger.core.services.fee.v1;

import java.util.UUID;

import com.firefly.core.banking.ledger.core.mappers.fee.v1.TransactionLineFeeMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.fee.v1.TransactionLineFeeDTO;
import com.firefly.core.banking.ledger.models.entities.fee.v1.TransactionLineFee;
import com.firefly.core.banking.ledger.models.repositories.fee.v1.TransactionLineFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the TransactionLineFeeService interface.
 * Provides functionality for managing fee transaction lines.
 */
@Service
@Transactional
public class TransactionLineFeeServiceImpl implements TransactionLineFeeService {

    @Autowired
    private TransactionLineFeeRepository repository;

    @Autowired
    private TransactionLineFeeMapper mapper;

    @Override
    public Mono<TransactionLineFeeDTO> getFeeLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line Fee not found")));
    }

    @Override
    public Mono<TransactionLineFeeDTO> createFeeLine(UUID transactionId, TransactionLineFeeDTO feeDTO) {
        feeDTO.setTransactionId(transactionId);
        TransactionLineFee entity = mapper.toEntity(feeDTO);
        return repository.save(entity)
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to create Transaction Line Fee", e)));
    }

    @Override
    public Mono<TransactionLineFeeDTO> updateFeeLine(UUID transactionId, TransactionLineFeeDTO feeDTO) {
        return repository.findByTransactionId(transactionId)
                .flatMap(existingEntity -> {
                    TransactionLineFee updatedEntity = mapper.toEntity(feeDTO);
                    updatedEntity.setTransactionLineFeeId(existingEntity.getTransactionLineFeeId());
                    updatedEntity.setTransactionId(transactionId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to update Transaction Line Fee")));
    }

    @Override
    public Mono<Void> deleteFeeLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .flatMap(entity -> repository.delete(entity))
                .switchIfEmpty(Mono.empty());
    }
}
