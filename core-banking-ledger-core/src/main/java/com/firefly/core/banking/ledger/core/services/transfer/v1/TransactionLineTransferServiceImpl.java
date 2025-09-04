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


package com.firefly.core.banking.ledger.core.services.transfer.v1;

import java.util.UUID;

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
    public Mono<TransactionLineTransferDTO> getTransferLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line Transfer not found")));
    }

    @Override
    public Mono<TransactionLineTransferDTO> createTransferLine(UUID transactionId, TransactionLineTransferDTO transferDTO) {
        transferDTO.setTransactionId(transactionId);
        TransactionLineTransfer entity = mapper.toEntity(transferDTO);
        return repository.save(entity)
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to create Transaction Line Transfer", e)));
    }

    @Override
    public Mono<TransactionLineTransferDTO> updateTransferLine(UUID transactionId, TransactionLineTransferDTO transferDTO) {
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
    public Mono<Void> deleteTransferLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .flatMap(entity -> repository.delete(entity))
                .switchIfEmpty(Mono.empty());
    }
}
