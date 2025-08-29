package com.firefly.core.banking.ledger.core.services.standingorder.v1;

import com.firefly.core.banking.ledger.core.mappers.standingorder.v1.TransactionLineStandingOrderMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionLineStandingOrderDTO;
import com.firefly.core.banking.ledger.models.entities.standingorder.v1.TransactionLineStandingOrder;
import com.firefly.core.banking.ledger.models.repositories.standingorder.v1.TransactionLineStandingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineStandingOrderServiceImpl implements TransactionLineStandingOrderService {

    @Autowired
    private TransactionLineStandingOrderRepository repository;

    @Autowired
    private TransactionLineStandingOrderMapper mapper;

    @Override
    public Mono<TransactionLineStandingOrderDTO> getStandingOrderLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineStandingOrderDTO> createStandingOrderLine(Long transactionId, TransactionLineStandingOrderDTO standingOrderDTO) {
        standingOrderDTO.setTransactionId(transactionId);
        TransactionLineStandingOrder entity = mapper.toEntity(standingOrderDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineStandingOrderDTO> updateStandingOrderLine(Long transactionId, TransactionLineStandingOrderDTO standingOrderDTO) {
        return repository.findByTransactionId(transactionId)
                .flatMap(existingEntity -> {
                    TransactionLineStandingOrder updatedEntity = mapper.toEntity(standingOrderDTO);
                    updatedEntity.setStandingOrderId(existingEntity.getStandingOrderId());
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteStandingOrderLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .flatMap(repository::delete);
    }
}
