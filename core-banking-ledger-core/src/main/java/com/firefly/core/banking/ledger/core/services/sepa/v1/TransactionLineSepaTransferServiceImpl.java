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


package com.firefly.core.banking.ledger.core.services.sepa.v1;

import java.util.UUID;

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
    public Mono<TransactionLineSepaTransferDTO> getSepaTransferLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("SEPA transfer line not found for transactionId: " + transactionId)));
    }

    @Override
    public Mono<TransactionLineSepaTransferDTO> createSepaTransferLine(UUID transactionId, TransactionLineSepaTransferDTO sepaDTO) {
        sepaDTO.setTransactionId(transactionId);
        TransactionLineSepaTransfer entity = mapper.toEntity(sepaDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineSepaTransferDTO> updateSepaTransferLine(UUID transactionId, TransactionLineSepaTransferDTO sepaDTO) {
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
    public Mono<Void> deleteSepaTransferLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .switchIfEmpty(Mono.error(new RuntimeException("SEPA transfer line not found for transactionId: " + transactionId)))
                .flatMap(repository::delete);
    }
}
