package com.firefly.core.banking.ledger.interfaces.dtos.ach.v1;

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
 * DTO representing detailed information about an ACH (Automated Clearing House) transaction.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineAchDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID transactionLineAchId;

    @FilterableId
    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @Size(max = 100, message = "ACH reference cannot exceed 100 characters")
    private String achReference;

    @FilterableId
    @NotNull(message = "ACH source account ID is required")
    private UUID achSourceAccountId;

    @FilterableId
    @NotNull(message = "ACH destination account ID is required")
    private UUID achDestinationAccountId;

    @NotBlank(message = "ACH source account number is required")
    @Size(max = 50, message = "ACH source account number cannot exceed 50 characters")
    private String achSourceAccountNumber;

    @NotBlank(message = "ACH destination account number is required")
    @Size(max = 50, message = "ACH destination account number cannot exceed 50 characters")
    private String achDestinationAccountNumber;

    @Size(max = 200, message = "ACH source account name cannot exceed 200 characters")
    private String achSourceAccountName;

    @Size(max = 200, message = "ACH destination account name cannot exceed 200 characters")
    private String achDestinationAccountName;

    @NotBlank(message = "ACH routing number is required")
    @Size(max = 20, message = "ACH routing number cannot exceed 20 characters")
    private String achRoutingNumber;

    @Size(max = 10, message = "ACH transaction code cannot exceed 10 characters")
    private String achTransactionCode;

    @Size(max = 200, message = "ACH purpose cannot exceed 200 characters")
    private String achPurpose;

    @Size(max = 500, message = "ACH notes cannot exceed 500 characters")
    private String achNotes;

    @ValidDateTime
    private LocalDateTime achTimestamp;

    @Size(max = 100, message = "ACH processed by cannot exceed 100 characters")
    private String achProcessedBy;

    @ValidAmount
    private BigDecimal achFeeAmount;

    @ValidCurrencyCode
    private String achFeeCurrency;

    @ValidDate
    private LocalDate achScheduledDate;

    @ValidDate
    private LocalDate achExecutionDate;

    @Size(max = 20, message = "ACH batch number cannot exceed 20 characters")
    private String achBatchNumber;

    @Size(max = 20, message = "ACH trace number cannot exceed 20 characters")
    private String achTraceNumber;

    @Size(max = 10, message = "ACH entry class code cannot exceed 10 characters")
    private String achEntryClassCode;

    @ValidDate
    private LocalDate achSettlementDate;

    @Size(max = 10, message = "ACH return code cannot exceed 10 characters")
    private String achReturnCode;

    @Size(max = 200, message = "ACH return reason cannot exceed 200 characters")
    private String achReturnReason;
}