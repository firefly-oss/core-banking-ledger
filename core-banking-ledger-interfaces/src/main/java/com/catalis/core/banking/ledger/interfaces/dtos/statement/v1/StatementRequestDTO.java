package com.catalis.core.banking.ledger.interfaces.dtos.statement.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.ledger.interfaces.enums.statement.v1.StatementPeriodEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
    private StatementPeriodEnum periodType;

    /**
     * The start date for the statement period (required for CUSTOM period type).
     */
    private LocalDate startDate;

    /**
     * The end date for the statement period (required for CUSTOM period type).
     */
    private LocalDate endDate;

    /**
     * The specific month for MONTHLY period type (1-12).
     */
    private Integer month;

    /**
     * The specific quarter for QUARTERLY period type (1-4).
     */
    private Integer quarter;

    /**
     * The specific year for YEARLY, QUARTERLY, or MONTHLY period types.
     */
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
