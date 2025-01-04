package com.catalis.core.banking.ledger.core.mappers.category.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.category.v1.TransactionCategoryDTO;
import com.catalis.core.banking.ledger.models.entities.category.v1.TransactionCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionCategoryMapper {
    TransactionCategoryDTO toDTO(TransactionCategory entity);
    TransactionCategory toEntity(TransactionCategoryDTO dto);
}
