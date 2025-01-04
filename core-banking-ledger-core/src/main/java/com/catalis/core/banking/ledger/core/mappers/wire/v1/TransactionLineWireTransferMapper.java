package com.catalis.core.banking.ledger.core.mappers.wire.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.wire.v1.TransactionLineWireTransferDTO;
import com.catalis.core.banking.ledger.models.entities.wire.v1.TransactionLineWireTransfer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionLineWireTransferMapper {
    TransactionLineWireTransferDTO toDTO(TransactionLineWireTransfer entity);
    TransactionLineWireTransfer toEntity(TransactionLineWireTransferDTO dto);
}
