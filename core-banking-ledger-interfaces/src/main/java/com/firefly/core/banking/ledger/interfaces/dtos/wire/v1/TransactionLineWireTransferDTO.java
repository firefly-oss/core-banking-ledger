package com.firefly.core.banking.ledger.interfaces.dtos.wire.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.ledger.interfaces.enums.wire.v1.WireTransferPriorityEnum;
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
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineWireTransferDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID transactionLineWireTransferId;

    @FilterableId
    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @Size(max = 100, message = "Wire transfer reference cannot exceed 100 characters")
    private String wireTransferReference;

    @NotBlank(message = "Wire origin SWIFT BIC is required")
    @Size(max = 11, message = "Wire origin SWIFT BIC cannot exceed 11 characters")
    private String wireOriginSwiftBic;

    @NotBlank(message = "Wire destination SWIFT BIC is required")
    @Size(max = 11, message = "Wire destination SWIFT BIC cannot exceed 11 characters")
    private String wireDestinationSwiftBic;

    @NotBlank(message = "Wire origin account number is required")
    @Size(max = 50, message = "Wire origin account number cannot exceed 50 characters")
    private String wireOriginAccountNumber;

    @NotBlank(message = "Wire destination account number is required")
    @Size(max = 50, message = "Wire destination account number cannot exceed 50 characters")
    private String wireDestinationAccountNumber;

    @Size(max = 200, message = "Wire transfer purpose cannot exceed 200 characters")
    private String wireTransferPurpose;

    @NotNull(message = "Wire transfer priority is required")
    private WireTransferPriorityEnum wireTransferPriority;

    @DecimalMin(value = "0.0", message = "Wire exchange rate cannot be negative")
    private BigDecimal wireExchangeRate;

    @ValidAmount
    private BigDecimal wireFeeAmount;

    @ValidCurrencyCode
    private String wireFeeCurrency;
    @Size(max = 200, message = "Wire instructing party cannot exceed 200 characters")
    private String wireInstructingParty;

    @Size(max = 200, message = "Wire beneficiary name cannot exceed 200 characters")
    private String wireBeneficiaryName;

    @Size(max = 500, message = "Wire beneficiary address cannot exceed 500 characters")
    private String wireBeneficiaryAddress;

    @ValidDateTime
    private LocalDateTime wireProcessingDate;

    @Size(max = 500, message = "Wire transaction notes cannot exceed 500 characters")
    private String wireTransactionNotes;

    @Size(max = 50, message = "Wire reception status cannot exceed 50 characters")
    private String wireReceptionStatus;

    @Size(max = 200, message = "Wire decline reason cannot exceed 200 characters")
    private String wireDeclineReason;

    private Boolean wireCancelledFlag;

    @Size(max = 20, message = "Bank of Spain reg code cannot exceed 20 characters")
    private String bankOfSpainRegCode;
}
