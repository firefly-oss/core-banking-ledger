package com.firefly.core.banking.ledger.core.mappers.core.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.core.v1.TransactionStatusHistoryDTO;
import com.firefly.core.banking.ledger.models.entities.core.v1.TransactionStatusHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionStatusHistoryMapper {
    TransactionStatusHistoryDTO toDTO(TransactionStatusHistory entity);
    TransactionStatusHistory toEntity(TransactionStatusHistoryDTO dto);
}
