package com.catalis.core.banking.ledger.core.mappers.sepa.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.sepa.v1.TransactionLineSepaTransferDTO;
import com.catalis.core.banking.ledger.models.entities.sepa.v1.TransactionLineSepaTransfer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionLineSepaTransferMapper {
    TransactionLineSepaTransferDTO toDTO(TransactionLineSepaTransfer entity);
    TransactionLineSepaTransfer toEntity(TransactionLineSepaTransferDTO dto);
}
