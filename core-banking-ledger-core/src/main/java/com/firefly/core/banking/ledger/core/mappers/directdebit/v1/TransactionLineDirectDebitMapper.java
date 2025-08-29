package com.firefly.core.banking.ledger.core.mappers.directdebit.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionLineDirectDebitDTO;
import com.firefly.core.banking.ledger.models.entities.directdebit.v1.TransactionLineDirectDebit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionLineDirectDebitMapper {
    TransactionLineDirectDebitDTO toDTO(TransactionLineDirectDebit entity);
    TransactionLineDirectDebit toEntity(TransactionLineDirectDebitDTO dto);
}
