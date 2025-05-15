package com.catalis.core.banking.ledger.interfaces.dtos.statement.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
    private StatementMetadataDTO metadata;
    
    /**
     * The opening balance at the start of the statement period.
     */
    private BigDecimal openingBalance;
    
    /**
     * The closing balance at the end of the statement period.
     */
    private BigDecimal closingBalance;
    
    /**
     * The total of all credits (incoming funds) during the period.
     */
    private BigDecimal totalCredits;
    
    /**
     * The total of all debits (outgoing funds) during the period.
     */
    private BigDecimal totalDebits;
    
    /**
     * The list of transaction entries in the statement.
     */
    private List<StatementEntryDTO> entries;
}
