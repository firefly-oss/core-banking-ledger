package com.catalis.core.banking.ledger.core.services.standingorder.v1;

import com.catalis.core.banking.ledger.core.mappers.standingorder.v1.TransactionLineStandingOrderMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionLineStandingOrderDTO;
import com.catalis.core.banking.ledger.models.repositories.standingorder.v1.TransactionLineStandingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineStandingOrderCreateService {
    @Autowired
    private TransactionLineStandingOrderRepository repository;

    @Autowired
    private TransactionLineStandingOrderMapper mapper;

    public Mono<TransactionLineStandingOrderDTO> createTransactionLineStandingOrder(TransactionLineStandingOrderDTO dto) {
        return Mono.just(dto)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }
}
