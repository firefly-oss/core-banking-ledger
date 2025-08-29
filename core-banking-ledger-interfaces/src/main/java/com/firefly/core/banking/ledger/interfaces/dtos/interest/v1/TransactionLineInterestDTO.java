package com.firefly.core.banking.ledger.interfaces.dtos.interest.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO representing detailed information about an interest transaction.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineInterestDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionLineInterestId;

    @FilterableId
    private Long transactionId;

    private String interestType;
    private String interestDescription;
    private String interestReference;
    
    @FilterableId
    private Long interestRelatedAccountId;
    
    private String interestCalculationMethod;
    private BigDecimal interestCalculationBase;
    private BigDecimal interestRatePercentage;
    private LocalDate interestAccrualStartDate;
    private LocalDate interestAccrualEndDate;
    private Integer interestDaysCalculated;
    private String interestCurrency;
    private BigDecimal interestTaxWithheldAmount;
    private BigDecimal interestTaxWithheldRate;
    private BigDecimal interestGrossAmount;
    private BigDecimal interestNetAmount;
    private LocalDateTime interestTimestamp;
    private String interestProcessedBy;
    private String interestSpanishTaxCode;
}
