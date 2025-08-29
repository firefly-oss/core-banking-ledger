package com.firefly.core.banking.ledger.core.mappers.money.v1;

import com.firefly.core.banking.ledger.core.mappers.BaseMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.money.v1.MoneyDTO;
import com.firefly.core.banking.ledger.models.entities.money.v1.Money;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper for converting between Money entity and DTO.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MoneyMapper extends BaseMapper<Money, MoneyDTO> {
    @Override
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    Money toEntity(MoneyDTO dto);

    @Override
    MoneyDTO toDTO(Money entity);
}
