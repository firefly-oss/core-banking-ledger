package com.firefly.core.banking.ledger.core.mappers.ach.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.ach.v1.TransactionLineAchDTO;
import com.firefly.core.banking.ledger.models.entities.ach.v1.TransactionLineAch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionLineAchMapper {
    TransactionLineAchDTO toDTO(TransactionLineAch entity);
    TransactionLineAch toEntity(TransactionLineAchDTO dto);
}
