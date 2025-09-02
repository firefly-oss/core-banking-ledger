package com.firefly.core.banking.ledger.interfaces.dtos.statement.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.ledger.interfaces.enums.statement.v1.StatementPeriodEnum;
import com.firefly.annotations.ValidDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * DTO for requesting an account or account space statement.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class StatementRequestDTO extends BaseDTO {
    /**
     * The period type for the statement.
     */
    @NotNull(message = "Period type is required")
    private StatementPeriodEnum periodType;

    /**
     * The start date for the statement period (required for CUSTOM period type).
     */
    @ValidDate
    private LocalDate startDate;

    /**
     * The end date for the statement period (required for CUSTOM period type).
     */
    @ValidDate
    private LocalDate endDate;

    /**
     * The specific month for MONTHLY period type (1-12).
     */
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;

    /**
     * The specific quarter for QUARTERLY period type (1-4).
     */
    @Min(value = 1, message = "Quarter must be between 1 and 4")
    @Max(value = 4, message = "Quarter must be between 1 and 4")
    private Integer quarter;

    /**
     * The specific year for YEARLY, QUARTERLY, or MONTHLY period types.
     */
    @Min(value = 1900, message = "Year must be 1900 or later")
    @Max(value = 2100, message = "Year must be 2100 or earlier")
    private Integer year;

    /**
     * Whether to include pending transactions.
     */
    private Boolean includePending;

    /**
     * Whether to include transaction details.
     */
    private Boolean includeDetails;
}
