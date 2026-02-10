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


package com.firefly.core.banking.ledger.core.services.leg.v1;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import com.firefly.core.banking.ledger.core.mappers.leg.v1.TransactionLegMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.leg.v1.TransactionLegDTO;
import com.firefly.core.banking.ledger.models.entities.leg.v1.TransactionLeg;
import com.firefly.core.banking.ledger.models.repositories.leg.v1.TransactionLegRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import java.util.UUID;
/**
 * Implementation of the TransactionLegService interface.
 */
@Service
@Transactional
public class TransactionLegServiceImpl implements TransactionLegService {

    @Autowired
    private TransactionLegRepository repository;

    @Autowired
    private TransactionLegMapper mapper;

    @Override
    public Mono<TransactionLegDTO> createTransactionLeg(UUID transactionId, TransactionLegDTO legDTO) {
        legDTO.setTransactionId(transactionId);
        TransactionLeg entity = mapper.toEntity(legDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionLegDTO> getTransactionLeg(UUID transactionId, UUID legId) {
        return repository.findById(legId)
                .filter(entity -> entity.getTransactionId().equals(transactionId))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<TransactionLegDTO>> listTransactionLegs(UUID transactionId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByTransactionId(transactionId, pageable),
                () -> repository.countByTransactionId(transactionId)
        );
    }

    @Override
    public Mono<PaginationResponse<TransactionLegDTO>> listAccountLegs(UUID accountId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountId(accountId, pageable),
                () -> repository.countByAccountId(accountId)
        );
    }

    @Override
    public Mono<PaginationResponse<TransactionLegDTO>> listAccountLegsByDateRange(
            UUID accountId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            PaginationRequest paginationRequest
    ) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByAccountIdAndBookingDateBetween(accountId, startDate, endDate, pageable),
                () -> repository.countByAccountIdAndBookingDateBetween(accountId, startDate, endDate)
        );
    }
}
