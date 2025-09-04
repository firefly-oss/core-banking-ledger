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


package com.firefly.core.banking.ledger.core.services.directdebit.v1;

import java.util.UUID;

import com.firefly.core.banking.ledger.core.mappers.directdebit.v1.TransactionLineDirectDebitMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionLineDirectDebitDTO;
import com.firefly.core.banking.ledger.models.entities.directdebit.v1.TransactionLineDirectDebit;
import com.firefly.core.banking.ledger.models.repositories.directdebit.v1.TransactionLineDirectDebitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineDirectDebitServiceImpl implements TransactionLineDirectDebitService {

    @Autowired
    private TransactionLineDirectDebitRepository repository;

    @Autowired
    private TransactionLineDirectDebitMapper mapper;

    @Override
    public Mono<TransactionLineDirectDebitDTO> getDirectDebitLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineDirectDebitDTO> createDirectDebitLine(UUID transactionId, TransactionLineDirectDebitDTO directDebitDTO) {
        TransactionLineDirectDebit entity = mapper.toEntity(directDebitDTO);
        entity.setTransactionId(transactionId);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLineDirectDebitDTO> updateDirectDebitLine(UUID transactionId, TransactionLineDirectDebitDTO directDebitDTO) {
        return repository.findByTransactionId(transactionId)
                .flatMap(existingEntity -> {
                    TransactionLineDirectDebit updatedEntity = mapper.toEntity(directDebitDTO);
                    updatedEntity.setTransactionLineDirectDebitId(existingEntity.getTransactionLineDirectDebitId());
                    updatedEntity.setTransactionId(transactionId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteDirectDebitLine(UUID transactionId) {
        return repository.findByTransactionId(transactionId)
                .flatMap(repository::delete);
    }
}
