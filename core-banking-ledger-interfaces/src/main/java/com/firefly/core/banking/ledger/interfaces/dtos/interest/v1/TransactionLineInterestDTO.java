package com.firefly.core.banking.ledger.interfaces.dtos.interest.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.utils.annotations.FilterableId;
import com.firefly.annotations.ValidAmount;
import com.firefly.annotations.ValidCurrencyCode;
import com.firefly.annotations.ValidDate;
import com.firefly.annotations.ValidDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.UUID;
/**
 * DTO representing detailed information about an interest transaction.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineInterestDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID transactionLineInterestId;

    @FilterableId
    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @NotBlank(message = "Interest type is required")
    @Size(max = 50, message = "Interest type cannot exceed 50 characters")
    private String interestType;

    @Size(max = 200, message = "Interest description cannot exceed 200 characters")
    private String interestDescription;

    @Size(max = 100, message = "Interest reference cannot exceed 100 characters")
    private String interestReference;

    @FilterableId
    private UUID interestRelatedAccountId;

    @NotBlank(message = "Interest calculation method is required")
    @Size(max = 50, message = "Interest calculation method cannot exceed 50 characters")
    private String interestCalculationMethod;

    @NotNull(message = "Interest calculation base is required")
    @ValidAmount
    private BigDecimal interestCalculationBase;

    @NotNull(message = "Interest rate percentage is required")
    @DecimalMin(value = "0.0", message = "Interest rate percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Interest rate percentage cannot exceed 100")
    private BigDecimal interestRatePercentage;

    @NotNull(message = "Interest accrual start date is required")
    @ValidDate
    private LocalDate interestAccrualStartDate;

    @NotNull(message = "Interest accrual end date is required")
    @ValidDate
    private LocalDate interestAccrualEndDate;

    @Min(value = 0, message = "Interest days calculated cannot be negative")
    private Integer interestDaysCalculated;
    @NotBlank(message = "Interest currency is required")
    @ValidCurrencyCode
    private String interestCurrency;

    @ValidAmount
    private BigDecimal interestTaxWithheldAmount;

    @DecimalMin(value = "0.0", message = "Interest tax withheld rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Interest tax withheld rate cannot exceed 100")
    private BigDecimal interestTaxWithheldRate;

    @NotNull(message = "Interest gross amount is required")
    @ValidAmount
    private BigDecimal interestGrossAmount;

    @NotNull(message = "Interest net amount is required")
    @ValidAmount
    private BigDecimal interestNetAmount;

    @ValidDateTime
    private LocalDateTime interestTimestamp;

    @Size(max = 100, message = "Interest processed by cannot exceed 100 characters")
    private String interestProcessedBy;

    @Size(max = 20, message = "Interest Spanish tax code cannot exceed 20 characters")
    private String interestSpanishTaxCode;
}
