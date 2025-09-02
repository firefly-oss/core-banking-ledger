package com.firefly.core.banking.ledger.interfaces.dtos.standingorder.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderFrequencyEnum;
import com.firefly.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderStatusEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.firefly.annotations.ValidDate;
import com.firefly.annotations.ValidDateTime;
import com.firefly.annotations.ValidIban;
import com.firefly.annotations.ValidBic;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.UUID;
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineStandingOrderDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID transactionLineStandingOrderId;

    @FilterableId
    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @FilterableId
    @NotBlank(message = "Standing order ID is required")
    @Size(max = 50, message = "Standing order ID cannot exceed 50 characters")
    private String standingOrderId;

    @NotNull(message = "Standing order frequency is required")
    private StandingOrderFrequencyEnum standingOrderFrequency;

    @NotNull(message = "Standing order start date is required")
    @ValidDate
    private LocalDate standingOrderStartDate;

    @ValidDate
    private LocalDate standingOrderEndDate;

    @ValidDate
    private LocalDate standingOrderNextExecutionDate;

    @Size(max = 100, message = "Standing order reference cannot exceed 100 characters")
    private String standingOrderReference;

    @NotBlank(message = "Standing order recipient name is required")
    @Size(max = 200, message = "Standing order recipient name cannot exceed 200 characters")
    private String standingOrderRecipientName;

    @NotBlank(message = "Standing order recipient IBAN is required")
    @ValidIban
    private String standingOrderRecipientIban;

    @ValidBic
    private String standingOrderRecipientBic;
    @Size(max = 200, message = "Standing order purpose cannot exceed 200 characters")
    private String standingOrderPurpose;

    @NotNull(message = "Standing order status is required")
    private StandingOrderStatusEnum standingOrderStatus;

    @Size(max = 500, message = "Standing order notes cannot exceed 500 characters")
    private String standingOrderNotes;

    @ValidDate
    private LocalDate standingOrderLastExecutionDate;

    @Min(value = 0, message = "Standing order total executions cannot be negative")
    private Integer standingOrderTotalExecutions;

    @ValidDateTime
    private LocalDateTime standingOrderCancelledDate;

    @ValidDate
    private LocalDate standingOrderSuspendedUntilDate;

    @Size(max = 100, message = "Standing order created by cannot exceed 100 characters")
    private String standingOrderCreatedBy;

    @Size(max = 100, message = "Standing order updated by cannot exceed 100 characters")
    private String standingOrderUpdatedBy;

    @ValidDateTime
    private LocalDateTime standingOrderCreationTimestamp;

    @ValidDateTime
    private LocalDateTime standingOrderUpdateTimestamp;

    private Boolean standingOrderSpanishTaxFlag;
}
