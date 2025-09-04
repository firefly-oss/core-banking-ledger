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


package com.firefly.core.banking.ledger.core.services.interest.v1;

import java.util.UUID;

import com.firefly.core.banking.ledger.core.mappers.interest.v1.TransactionLineInterestMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.interest.v1.TransactionLineInterestDTO;
import com.firefly.core.banking.ledger.models.entities.interest.v1.TransactionLineInterest;
import com.firefly.core.banking.ledger.models.repositories.interest.v1.TransactionLineInterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the TransactionLineInterestService interface.
 * Provides functionality for managing interest transaction lines.
 */
@Service
@Transactional
public class TransactionLineInterestServiceImpl implements TransactionLineInterestService {

    @Autowired
    private TransactionLineInterestRepository repository;

    @Autowired
    private TransactionLineInterestMapper mapper;

    @Override
    public Mono<TransactionLineInterestDTO> getInterestLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line Interest not found")));
    }

    @Override
    public Mono<TransactionLineInterestDTO> createInterestLine(UUID transactionId, TransactionLineInterestDTO interestDTO) {
        interestDTO.setTransactionId(transactionId);
        TransactionLineInterest entity = mapper.toEntity(interestDTO);
        return repository.save(entity)
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to create Transaction Line Interest", e)));
    }

    @Override
    public Mono<TransactionLineInterestDTO> updateInterestLine(UUID transactionId, TransactionLineInterestDTO interestDTO) {
        return repository.findByTransactionId(transactionId)
                .flatMap(existingEntity -> {
                    TransactionLineInterest updatedEntity = mapper.toEntity(interestDTO);
                    updatedEntity.setTransactionLineInterestId(existingEntity.getTransactionLineInterestId());
                    updatedEntity.setTransactionId(transactionId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to update Transaction Line Interest")));
    }

    @Override
    public Mono<Void> deleteInterestLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .flatMap(entity -> repository.delete(entity))
                .switchIfEmpty(Mono.empty());
    }
}
