package com.catalis.core.banking.ledger.core.services.wire.v1;

import com.catalis.core.banking.ledger.core.mappers.wire.v1.TransactionLineWireTransferMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.wire.v1.TransactionLineWireTransferDTO;
import com.catalis.core.banking.ledger.models.entities.wire.v1.TransactionLineWireTransfer;
import com.catalis.core.banking.ledger.models.repositories.wire.v1.TransactionLineWireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineWireTransferServiceImpl implements TransactionLineWireTransferService {

    @Autowired
    private TransactionLineWireRepository repository;

    @Autowired
    private TransactionLineWireTransferMapper mapper;

    @Override
    public Mono<TransactionLineWireTransferDTO> getWireTransferLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineWireTransferDTO> createWireTransferLine(Long transactionId, TransactionLineWireTransferDTO wireDTO) {
        wireDTO.setTransactionId(transactionId);
        TransactionLineWireTransfer entity = mapper.toEntity(wireDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineWireTransferDTO> updateWireTransferLine(Long transactionId, TransactionLineWireTransferDTO wireDTO) {
        return repository.findByTransactionId(transactionId)
                .flatMap(existingEntity -> {
                    TransactionLineWireTransfer updatedEntity = mapper.toEntity(wireDTO);
                    updatedEntity.setTransactionLineWireTransferId(existingEntity.getTransactionLineWireTransferId());
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteWireTransferLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .flatMap(repository::delete);
    }
}
