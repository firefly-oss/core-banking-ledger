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


package com.firefly.core.banking.ledger.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.firefly.annotations.ValidDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ValidDateTime
    @PastOrPresent(message = "Date created cannot be in the future")
    private LocalDateTime dateCreated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ValidDateTime
    @PastOrPresent(message = "Date updated cannot be in the future")
    private LocalDateTime dateUpdated;
}
