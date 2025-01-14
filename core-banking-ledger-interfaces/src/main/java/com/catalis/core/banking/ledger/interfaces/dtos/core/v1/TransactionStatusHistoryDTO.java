package com.catalis.core.banking.ledger.interfaces.dtos.core.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
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

    private Long transactionId;
    private TransactionStatusEnum statusCode;
    private LocalDateTime statusStartDatetime;
    private LocalDateTime statusEndDatetime;
    private String reason;
    private Boolean regulatedReportingFlag;
}
