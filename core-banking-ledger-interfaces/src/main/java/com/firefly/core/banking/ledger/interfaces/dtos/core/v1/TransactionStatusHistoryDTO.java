package com.firefly.core.banking.ledger.interfaces.dtos.core.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionStatusHistoryDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionStatusHistoryId;

    @FilterableId
    private Long transactionId;

    private TransactionStatusEnum statusCode;
    private LocalDateTime statusStartDatetime;
    private LocalDateTime statusEndDatetime;
    private String reason;
    private Boolean regulatedReportingFlag;
}
