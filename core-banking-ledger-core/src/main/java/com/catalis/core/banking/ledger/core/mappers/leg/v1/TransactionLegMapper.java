package com.catalis.core.banking.ledger.core.mappers.leg.v1;

import com.catalis.core.banking.ledger.core.mappers.BaseMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.leg.v1.TransactionLegDTO;
import com.catalis.core.banking.ledger.models.entities.leg.v1.TransactionLeg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper for converting between TransactionLeg entity and DTO.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionLegMapper extends BaseMapper<TransactionLeg, TransactionLegDTO> {
    @Override
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    TransactionLeg toEntity(TransactionLegDTO dto);

    @Override
    TransactionLegDTO toDTO(TransactionLeg entity);
}
