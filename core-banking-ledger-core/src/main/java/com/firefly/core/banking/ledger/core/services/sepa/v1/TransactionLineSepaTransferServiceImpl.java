package com.firefly.core.banking.ledger.core.services.sepa.v1;

import com.firefly.core.banking.ledger.core.mappers.sepa.v1.TransactionLineSepaTransferMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.sepa.v1.TransactionLineSepaTransferDTO;
import com.firefly.core.banking.ledger.models.entities.sepa.v1.TransactionLineSepaTransfer;
import com.firefly.core.banking.ledger.models.repositories.sepa.v1.TransactionLineSepaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineSepaTransferServiceImpl implements TransactionLineSepaTransferService {

    @Autowired
    private TransactionLineSepaRepository repository;

    @Autowired
    private TransactionLineSepaTransferMapper mapper;

    @Override
    public Mono<TransactionLineSepaTransferDTO> getSepaTransferLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("SEPA transfer line not found for transactionId: " + transactionId)));
    }

    @Override
    public Mono<TransactionLineSepaTransferDTO> createSepaTransferLine(Long transactionId, TransactionLineSepaTransferDTO sepaDTO) {
        sepaDTO.setTransactionId(transactionId);
        TransactionLineSepaTransfer entity = mapper.toEntity(sepaDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineSepaTransferDTO> updateSepaTransferLine(Long transactionId, TransactionLineSepaTransferDTO sepaDTO) {
        return repository.findByTransactionId(transactionId)
                .switchIfEmpty(Mono.error(new RuntimeException("SEPA transfer line not found for transactionId: " + transactionId)))
                .flatMap(existingEntity -> {
                    TransactionLineSepaTransfer updatedEntity = mapper.toEntity(sepaDTO);
                    updatedEntity.setTransactionLineSepaId(existingEntity.getTransactionLineSepaId());
                    return repository.save(updatedEntity)
                            .map(mapper::toDTO);
                });
    }

    @Override
    public Mono<Void> deleteSepaTransferLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .switchIfEmpty(Mono.error(new RuntimeException("SEPA transfer line not found for transactionId: " + transactionId)))
                .flatMap(repository::delete);
    }
}
