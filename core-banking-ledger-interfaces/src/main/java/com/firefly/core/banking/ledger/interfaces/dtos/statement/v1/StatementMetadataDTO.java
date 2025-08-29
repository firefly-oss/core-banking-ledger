package com.firefly.core.banking.ledger.interfaces.dtos.statement.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.ledger.interfaces.enums.statement.v1.StatementPeriodEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for statement metadata.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class StatementMetadataDTO extends BaseDTO {
    /**
     * The unique identifier for the statement.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long statementId;

    /**
     * The account ID associated with this statement (null if for account space).
     */
    @FilterableId
    private Long accountId;

    /**
     * The account space ID associated with this statement (null if for account).
     */
    @FilterableId
    private Long accountSpaceId;

    /**
     * The period type for the statement.
     */
    private StatementPeriodEnum periodType;

    /**
     * The start date of the statement period.
     */
    private LocalDate startDate;

    /**
     * The end date of the statement period.
     */
    private LocalDate endDate;

    /**
     * The date when the statement was generated.
     */
    private LocalDateTime generationDate;

    /**
     * The number of transactions included in the statement.
     */
    private Integer transactionCount;

    /**
     * Whether pending transactions were included.
     */
    private Boolean includedPending;

    /**
     * Whether transaction details were included.
     */
    private Boolean includedDetails;
}
