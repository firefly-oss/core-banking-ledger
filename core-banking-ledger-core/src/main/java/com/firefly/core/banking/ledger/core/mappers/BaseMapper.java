/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


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