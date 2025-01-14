package com.catalis.core.banking.ledger.core.mappers.ledger.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerEntryDTO;
import com.catalis.core.banking.ledger.models.entities.ledger.v1.LedgerEntry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LedgerEntryMapper {
    LedgerEntryDTO toDTO(LedgerEntry entity);
    LedgerEntry toEntity(LedgerEntryDTO dto);
}
