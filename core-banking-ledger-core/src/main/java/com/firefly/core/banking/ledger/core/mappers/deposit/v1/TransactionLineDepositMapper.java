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


package com.firefly.core.banking.ledger.core.mappers.deposit.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.deposit.v1.TransactionLineDepositDTO;
import com.firefly.core.banking.ledger.models.entities.deposit.v1.TransactionLineDeposit;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between TransactionLineDeposit entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface TransactionLineDepositMapper {
    TransactionLineDepositDTO toDTO(TransactionLineDeposit entity);
    TransactionLineDeposit toEntity(TransactionLineDepositDTO dto);
}
