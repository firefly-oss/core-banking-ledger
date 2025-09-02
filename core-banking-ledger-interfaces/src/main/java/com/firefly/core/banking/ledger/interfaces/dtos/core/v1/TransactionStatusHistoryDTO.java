package com.firefly.core.banking.ledger.interfaces.dtos.core.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.firefly.annotations.ValidDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

import java.util.UUID;
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionStatusHistoryDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID transactionStatusHistoryId;

    @FilterableId
    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @NotNull(message = "Status code is required")
    private TransactionStatusEnum statusCode;

    @NotNull(message = "Status start datetime is required")
    @ValidDateTime
    private LocalDateTime statusStartDatetime;

    @ValidDateTime
    private LocalDateTime statusEndDatetime;

    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;

    private Boolean regulatedReportingFlag;
}
