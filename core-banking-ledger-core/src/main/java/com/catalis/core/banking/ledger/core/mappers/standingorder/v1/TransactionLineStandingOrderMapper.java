package com.catalis.core.banking.ledger.core.mappers.standingorder.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionLineStandingOrderDTO;
import com.catalis.core.banking.ledger.models.entities.standingorder.v1.TransactionLineStandingOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionLineStandingOrderMapper {
    TransactionLineStandingOrderDTO toDTO(TransactionLineStandingOrder entity);
    TransactionLineStandingOrder toEntity(TransactionLineStandingOrderDTO dto);
}
