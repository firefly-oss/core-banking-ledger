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
