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


package com.firefly.core.banking.ledger.core.services.money.v1;

import java.util.UUID;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import com.firefly.core.banking.ledger.core.mappers.money.v1.MoneyMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.money.v1.MoneyDTO;
import com.firefly.core.banking.ledger.models.entities.money.v1.Money;
import com.firefly.core.banking.ledger.models.repositories.money.v1.MoneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the MoneyService interface.
 */
@Service
@Transactional
public class MoneyServiceImpl implements MoneyService {

    @Autowired
    private MoneyRepository repository;

    @Autowired
    private MoneyMapper mapper;

    @Override
    public Mono<MoneyDTO> createMoney(MoneyDTO moneyDTO) {
        Money entity = mapper.toEntity(moneyDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<MoneyDTO> getMoney(UUID moneyId) {
        return repository.findById(moneyId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<MoneyDTO>> listMoney(PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findAllBy(pageable),
                repository::count
        );
    }

    @Override
    public Mono<PaginationResponse<MoneyDTO>> listMoneyByCurrency(String currency, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByCurrency(currency, pageable),
                () -> repository.countByCurrency(currency)
        );
    }
}
