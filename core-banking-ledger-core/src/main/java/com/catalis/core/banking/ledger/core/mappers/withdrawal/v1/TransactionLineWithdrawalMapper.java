package com.catalis.core.banking.ledger.core.mappers.withdrawal.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.withdrawal.v1.TransactionLineWithdrawalDTO;
import com.catalis.core.banking.ledger.models.entities.withdrawal.v1.TransactionLineWithdrawal;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between TransactionLineWithdrawal entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface TransactionLineWithdrawalMapper {
    TransactionLineWithdrawalDTO toDTO(TransactionLineWithdrawal entity);
    TransactionLineWithdrawal toEntity(TransactionLineWithdrawalDTO dto);
}
