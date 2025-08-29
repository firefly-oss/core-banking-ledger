package com.firefly.core.banking.ledger.core.mappers.interest.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.interest.v1.TransactionLineInterestDTO;
import com.firefly.core.banking.ledger.models.entities.interest.v1.TransactionLineInterest;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between TransactionLineInterest entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface TransactionLineInterestMapper {
    TransactionLineInterestDTO toDTO(TransactionLineInterest entity);
    TransactionLineInterest toEntity(TransactionLineInterestDTO dto);
}
