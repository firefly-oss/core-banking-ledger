package com.firefly.core.banking.ledger.interfaces.dtos.standingorder.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderFrequencyEnum;
import com.firefly.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderStatusEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineStandingOrderDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionLineStandingOrderId;

    @FilterableId
    private Long transactionId;

    @FilterableId
    private String standingOrderId;

    private StandingOrderFrequencyEnum standingOrderFrequency;
    private LocalDate standingOrderStartDate;
    private LocalDate standingOrderEndDate;
    private LocalDate standingOrderNextExecutionDate;
    private String standingOrderReference;
    private String standingOrderRecipientName;
    private String standingOrderRecipientIban;
    private String standingOrderRecipientBic;
    private String standingOrderPurpose;
    private StandingOrderStatusEnum standingOrderStatus;
    private String standingOrderNotes;
    private LocalDate standingOrderLastExecutionDate;
    private Integer standingOrderTotalExecutions;
    private LocalDateTime standingOrderCancelledDate;
    private LocalDate standingOrderSuspendedUntilDate;
    private String standingOrderCreatedBy;
    private String standingOrderUpdatedBy;
    private LocalDateTime standingOrderCreationTimestamp;
    private LocalDateTime standingOrderUpdateTimestamp;
    private Boolean standingOrderSpanishTaxFlag;
}
