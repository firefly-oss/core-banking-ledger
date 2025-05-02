package com.catalis.core.banking.ledger.core.mappers.transfer.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.transfer.v1.TransactionLineTransferDTO;
import com.catalis.core.banking.ledger.models.entities.transfer.v1.TransactionLineTransfer;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between TransactionLineTransfer entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface TransactionLineTransferMapper {
    TransactionLineTransferDTO toDTO(TransactionLineTransfer entity);
    TransactionLineTransfer toEntity(TransactionLineTransferDTO dto);
}
