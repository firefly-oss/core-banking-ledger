package com.catalis.core.banking.ledger.core.mappers.deposit.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.deposit.v1.TransactionLineDepositDTO;
import com.catalis.core.banking.ledger.models.entities.deposit.v1.TransactionLineDeposit;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between TransactionLineDeposit entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface TransactionLineDepositMapper {
    TransactionLineDepositDTO toDTO(TransactionLineDeposit entity);
    TransactionLineDeposit toEntity(TransactionLineDepositDTO dto);
}
