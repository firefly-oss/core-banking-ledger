package com.catalis.core.banking.ledger.core.mappers.core.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.models.entities.core.v1.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toDTO(Transaction entity);
    Transaction toEntity(TransactionDTO dto);
}
