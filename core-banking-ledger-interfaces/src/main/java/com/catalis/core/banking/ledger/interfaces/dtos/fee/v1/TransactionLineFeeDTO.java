package com.catalis.core.banking.ledger.interfaces.dtos.fee.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO representing detailed information about a fee transaction.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineFeeDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionLineFeeId;

    @FilterableId
    private Long transactionId;

    private String feeType;
    private String feeDescription;
    private String feeReference;
    
    @FilterableId
    private Long feeRelatedTransactionId;
    
    private String feeRelatedService;
    private String feeCalculationMethod;
    private BigDecimal feeCalculationBase;
    private BigDecimal feeRatePercentage;
    private BigDecimal feeFixedAmount;
    private String feeCurrency;
    private Boolean feeWaived;
    private String feeWaiverReason;
    private String feeWaiverAuthorizedBy;
    private LocalDateTime feeTimestamp;
    private String feeProcessedBy;
    private String feeSpanishTaxCode;
}
