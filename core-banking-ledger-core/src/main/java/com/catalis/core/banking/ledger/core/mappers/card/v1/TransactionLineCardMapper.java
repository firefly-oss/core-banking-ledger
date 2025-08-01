package com.catalis.core.banking.ledger.core.mappers.card.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.card.v1.TransactionLineCardDTO;
import com.catalis.core.banking.ledger.models.entities.card.v1.TransactionLineCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionLineCardMapper {
    TransactionLineCardDTO toDTO(TransactionLineCard entity);
    TransactionLineCard toEntity(TransactionLineCardDTO dto);
}
