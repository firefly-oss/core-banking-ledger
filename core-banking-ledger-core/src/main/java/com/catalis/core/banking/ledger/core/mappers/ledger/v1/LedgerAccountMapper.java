package com.catalis.core.banking.ledger.core.mappers.ledger.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerAccountDTO;
import com.catalis.core.banking.ledger.models.entities.ledger.v1.LedgerAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LedgerAccountMapper {
    LedgerAccountDTO toDTO(LedgerAccount entity);
    LedgerAccount toEntity(LedgerAccountDTO dto);
}
