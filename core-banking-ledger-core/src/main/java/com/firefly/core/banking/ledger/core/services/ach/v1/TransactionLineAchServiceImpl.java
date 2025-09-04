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


package com.firefly.core.banking.ledger.core.services.ach.v1;

import java.util.UUID;

import com.firefly.core.banking.ledger.core.mappers.ach.v1.TransactionLineAchMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.ach.v1.TransactionLineAchDTO;
import com.firefly.core.banking.ledger.models.entities.ach.v1.TransactionLineAch;
import com.firefly.core.banking.ledger.models.repositories.ach.v1.TransactionLineAchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the TransactionLineAchService interface.
 * Provides functionality for managing ACH transaction lines.
 */
@Service
@Transactional
public class TransactionLineAchServiceImpl implements TransactionLineAchService {

    @Autowired
    private TransactionLineAchRepository repository;

    @Autowired
    private TransactionLineAchMapper mapper;

    @Override
    public Mono<TransactionLineAchDTO> getAchLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line ACH not found")));
    }

    @Override
    public Mono<TransactionLineAchDTO> createAchLine(UUID transactionId, TransactionLineAchDTO achDTO) {
        achDTO.setTransactionId(transactionId);
        TransactionLineAch entity = mapper.toEntity(achDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineAchDTO> updateAchLine(UUID transactionId, TransactionLineAchDTO achDTO) {
        return repository.findByTransactionId(transactionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line ACH not found")))
                .flatMap(existingEntity -> {
                    achDTO.setTransactionId(transactionId);
                    achDTO.setTransactionLineAchId(existingEntity.getTransactionLineAchId());
                    TransactionLineAch updatedEntity = mapper.toEntity(achDTO);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteAchLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line ACH not found")))
                .flatMap(repository::delete);
    }
}
