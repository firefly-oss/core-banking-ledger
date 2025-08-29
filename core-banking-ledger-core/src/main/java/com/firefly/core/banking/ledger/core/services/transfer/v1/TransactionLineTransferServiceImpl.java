package com.firefly.core.banking.ledger.core.services.transfer.v1;

import com.firefly.core.banking.ledger.core.mappers.transfer.v1.TransactionLineTransferMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.transfer.v1.TransactionLineTransferDTO;
import com.firefly.core.banking.ledger.models.entities.transfer.v1.TransactionLineTransfer;
import com.firefly.core.banking.ledger.models.repositories.transfer.v1.TransactionLineTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the TransactionLineTransferService interface.
 * Provides functionality for managing transfer transaction lines.
 */
@Service
@Transactional
public class TransactionLineTransferServiceImpl implements TransactionLineTransferService {

    @Autowired
    private TransactionLineTransferRepository repository;

    @Autowired
    private TransactionLineTransferMapper mapper;

    @Override
    public Mono<TransactionLineTransferDTO> getTransferLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line Transfer not found")));
    }

    @Override
    public Mono<TransactionLineTransferDTO> createTransferLine(Long transactionId, TransactionLineTransferDTO transferDTO) {
        transferDTO.setTransactionId(transactionId);
        TransactionLineTransfer entity = mapper.toEntity(transferDTO);
        return repository.save(entity)
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to create Transaction Line Transfer", e)));
    }

    @Override
    public Mono<TransactionLineTransferDTO> updateTransferLine(Long transactionId, TransactionLineTransferDTO transferDTO) {
        return repository.findByTransactionId(transactionId)
                .flatMap(existingEntity -> {
                    TransactionLineTransfer updatedEntity = mapper.toEntity(transferDTO);
                    updatedEntity.setTransactionLineTransferId(existingEntity.getTransactionLineTransferId());
                    updatedEntity.setTransactionId(transactionId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to update Transaction Line Transfer")));
    }

    @Override
    public Mono<Void> deleteTransferLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .flatMap(entity -> repository.delete(entity))
                .switchIfEmpty(Mono.empty());
    }
}
