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


package com.firefly.core.banking.ledger.core.services.wire.v1;

import java.util.UUID;

import com.firefly.core.banking.ledger.core.mappers.wire.v1.TransactionLineWireTransferMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.wire.v1.TransactionLineWireTransferDTO;
import com.firefly.core.banking.ledger.models.entities.wire.v1.TransactionLineWireTransfer;
import com.firefly.core.banking.ledger.models.repositories.wire.v1.TransactionLineWireRepository;
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
    public Mono<TransactionLineWireTransferDTO> getWireTransferLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineWireTransferDTO> createWireTransferLine(UUID transactionId, TransactionLineWireTransferDTO wireDTO) {
        wireDTO.setTransactionId(transactionId);
        TransactionLineWireTransfer entity = mapper.toEntity(wireDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineWireTransferDTO> updateWireTransferLine(UUID transactionId, TransactionLineWireTransferDTO wireDTO) {
        return repository.findByTransactionId(transactionId)
                .flatMap(existingEntity -> {
                    TransactionLineWireTransfer updatedEntity = mapper.toEntity(wireDTO);
                    updatedEntity.setTransactionLineWireTransferId(existingEntity.getTransactionLineWireTransferId());
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteWireTransferLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .flatMap(repository::delete);
    }
}
