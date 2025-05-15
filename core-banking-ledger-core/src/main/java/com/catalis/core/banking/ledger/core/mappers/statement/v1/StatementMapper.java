package com.catalis.core.banking.ledger.core.mappers.statement.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.statement.v1.StatementMetadataDTO;
import com.catalis.core.banking.ledger.models.entities.statement.v1.Statement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper for converting between Statement entity and StatementMetadataDTO.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatementMapper {
    /**
     * Convert a Statement entity to a StatementMetadataDTO.
     *
     * @param entity The Statement entity.
     * @return The StatementMetadataDTO.
     */
    @Mapping(target = "dateCreated", source = "dateCreated")
    @Mapping(target = "dateUpdated", source = "dateUpdated")
    StatementMetadataDTO toDTO(Statement entity);

    /**
     * Convert a StatementMetadataDTO to a Statement entity.
     *
     * @param dto The StatementMetadataDTO.
     * @return The Statement entity.
     */
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    Statement toEntity(StatementMetadataDTO dto);
}
