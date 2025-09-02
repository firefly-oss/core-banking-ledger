package com.firefly.core.banking.ledger.interfaces.dtos.directdebit.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitProcessingStatusEnum;
import com.firefly.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitSequenceTypeEnum;
import com.firefly.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitSpanishSchemeEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.firefly.annotations.ValidDate;
import com.firefly.annotations.ValidDateTime;
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
public class TransactionLineDirectDebitDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID transactionLineDirectDebitId;

    @FilterableId
    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @FilterableId
    @NotBlank(message = "Direct debit mandate ID is required")
    @Size(max = 50, message = "Direct debit mandate ID cannot exceed 50 characters")
    private String directDebitMandateId;

    @FilterableId
    @NotBlank(message = "Direct debit creditor ID is required")
    @Size(max = 50, message = "Direct debit creditor ID cannot exceed 50 characters")
    private String directDebitCreditorId;

    @Size(max = 100, message = "Direct debit reference cannot exceed 100 characters")
    private String directDebitReference;

    @NotNull(message = "Direct debit sequence type is required")
    private DirectDebitSequenceTypeEnum directDebitSequenceType;

    @NotNull(message = "Direct debit due date is required")
    @ValidDate
    private LocalDate directDebitDueDate;

    @Size(max = 50, message = "Direct debit payment method cannot exceed 50 characters")
    private String directDebitPaymentMethod;

    @Size(max = 200, message = "Direct debit debtor name cannot exceed 200 characters")
    private String directDebitDebtorName;

    @Size(max = 500, message = "Direct debit debtor address cannot exceed 500 characters")
    private String directDebitDebtorAddress;

    @Size(max = 100, message = "Direct debit debtor contact cannot exceed 100 characters")
    private String directDebitDebtorContact;

    @NotNull(message = "Direct debit processing status is required")
    private DirectDebitProcessingStatusEnum directDebitProcessingStatus;

    @ValidDateTime
    private LocalDateTime directDebitAuthorizationDate;

    @ValidDateTime
    private LocalDateTime directDebitRevocationDate;

    @NotNull(message = "Direct debit Spanish scheme is required")
    private DirectDebitSpanishSchemeEnum directDebitSpanishScheme;

}