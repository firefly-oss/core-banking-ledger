package com.firefly.core.banking.ledger.interfaces.dtos.statement.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.annotations.ValidAmount;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for a complete account or account space statement.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class StatementDTO extends BaseDTO {
    /**
     * The metadata for the statement.
     */
    @NotNull(message = "Statement metadata is required")
    @Valid
    private StatementMetadataDTO metadata;

    /**
     * The opening balance at the start of the statement period.
     */
    @NotNull(message = "Opening balance is required")
    @ValidAmount
    private BigDecimal openingBalance;

    /**
     * The closing balance at the end of the statement period.
     */
    @NotNull(message = "Closing balance is required")
    @ValidAmount
    private BigDecimal closingBalance;

    /**
     * The total of all credits (incoming funds) during the period.
     */
    @NotNull(message = "Total credits is required")
    @ValidAmount
    private BigDecimal totalCredits;

    /**
     * The total of all debits (outgoing funds) during the period.
     */
    @NotNull(message = "Total debits is required")
    @ValidAmount
    private BigDecimal totalDebits;

    /**
     * The list of transaction entries in the statement.
     */
    @Valid
    private List<StatementEntryDTO> entries;
}
