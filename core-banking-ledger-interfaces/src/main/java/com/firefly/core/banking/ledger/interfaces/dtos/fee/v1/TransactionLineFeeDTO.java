package com.firefly.core.banking.ledger.interfaces.dtos.fee.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.utils.annotations.FilterableId;
import com.firefly.annotations.ValidAmount;
import com.firefly.annotations.ValidCurrencyCode;
import com.firefly.annotations.ValidDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.UUID;
/**
 * DTO representing detailed information about a fee transaction.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineFeeDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID transactionLineFeeId;

    @FilterableId
    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @NotBlank(message = "Fee type is required")
    @Size(max = 50, message = "Fee type cannot exceed 50 characters")
    private String feeType;

    @Size(max = 200, message = "Fee description cannot exceed 200 characters")
    private String feeDescription;

    @Size(max = 100, message = "Fee reference cannot exceed 100 characters")
    private String feeReference;

    @FilterableId
    private UUID feeRelatedTransactionId;

    @Size(max = 100, message = "Fee related service cannot exceed 100 characters")
    private String feeRelatedService;

    @Size(max = 50, message = "Fee calculation method cannot exceed 50 characters")
    private String feeCalculationMethod;

    @ValidAmount
    private BigDecimal feeCalculationBase;

    @DecimalMin(value = "0.0", message = "Fee rate percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Fee rate percentage cannot exceed 100")
    private BigDecimal feeRatePercentage;

    @ValidAmount
    private BigDecimal feeFixedAmount;

    @ValidCurrencyCode
    private String feeCurrency;
    private Boolean feeWaived;

    @Size(max = 200, message = "Fee waiver reason cannot exceed 200 characters")
    private String feeWaiverReason;

    @Size(max = 100, message = "Fee waiver authorized by cannot exceed 100 characters")
    private String feeWaiverAuthorizedBy;

    @ValidDateTime
    private LocalDateTime feeTimestamp;

    @Size(max = 100, message = "Fee processed by cannot exceed 100 characters")
    private String feeProcessedBy;

    @Size(max = 20, message = "Fee Spanish tax code cannot exceed 20 characters")
    private String feeSpanishTaxCode;
}
