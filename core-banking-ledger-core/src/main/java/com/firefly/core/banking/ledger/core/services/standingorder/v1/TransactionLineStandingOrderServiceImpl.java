/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.core.banking.ledger.core.services.standingorder.v1;

import java.util.UUID;

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
    public Mono<TransactionLineStandingOrderDTO> getStandingOrderLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineStandingOrderDTO> createStandingOrderLine(UUID transactionId, TransactionLineStandingOrderDTO standingOrderDTO) {
        standingOrderDTO.setTransactionId(transactionId);
        TransactionLineStandingOrder entity = mapper.toEntity(standingOrderDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineStandingOrderDTO> updateStandingOrderLine(UUID transactionId, TransactionLineStandingOrderDTO standingOrderDTO) {
        return repository.findByTransactionId(transactionId)
                .flatMap(existingEntity -> {
                    TransactionLineStandingOrder updatedEntity = mapper.toEntity(standingOrderDTO);
                    updatedEntity.setStandingOrderId(existingEntity.getStandingOrderId());
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteStandingOrderLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .flatMap(repository::delete);
    }
}
