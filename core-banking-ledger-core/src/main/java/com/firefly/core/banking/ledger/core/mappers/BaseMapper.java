package com.firefly.core.banking.ledger.core.mappers;

/**
 * Base mapper interface for converting between entity and DTO.
 *
 * @param <E> Entity type
 * @param <D> DTO type
 */
public interface BaseMapper<E, D> {
    /**
     * Converts a DTO to an entity.
     *
     * @param dto The DTO to convert
     * @return The converted entity
     */
    E toEntity(D dto);

    /**
     * Converts an entity to a DTO.
     *
     * @param entity The entity to convert
     * @return The converted DTO
     */
    D toDTO(E entity);
}