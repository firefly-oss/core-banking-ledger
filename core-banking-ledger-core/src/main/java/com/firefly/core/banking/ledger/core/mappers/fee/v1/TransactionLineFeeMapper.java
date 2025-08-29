package com.firefly.core.banking.ledger.core.mappers.fee.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.fee.v1.TransactionLineFeeDTO;
import com.firefly.core.banking.ledger.models.entities.fee.v1.TransactionLineFee;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between TransactionLineFee entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface TransactionLineFeeMapper {
    TransactionLineFeeDTO toDTO(TransactionLineFee entity);
    TransactionLineFee toEntity(TransactionLineFeeDTO dto);
}
