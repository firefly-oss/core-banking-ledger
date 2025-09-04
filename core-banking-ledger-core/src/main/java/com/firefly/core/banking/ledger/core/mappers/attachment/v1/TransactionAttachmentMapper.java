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


package com.firefly.core.banking.ledger.core.mappers.attachment.v1;

import com.firefly.core.banking.ledger.core.mappers.BaseMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.attachment.v1.TransactionAttachmentDTO;
import com.firefly.core.banking.ledger.models.entities.attachment.v1.TransactionAttachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper for converting between TransactionAttachment entity and DTO.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionAttachmentMapper extends BaseMapper<TransactionAttachment, TransactionAttachmentDTO> {
    @Override
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    TransactionAttachment toEntity(TransactionAttachmentDTO dto);

    @Override
    TransactionAttachmentDTO toDTO(TransactionAttachment entity);
}
