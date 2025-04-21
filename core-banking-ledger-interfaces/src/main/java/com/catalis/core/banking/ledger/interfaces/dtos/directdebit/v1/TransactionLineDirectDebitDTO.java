package com.catalis.core.banking.ledger.interfaces.dtos.directdebit.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitProcessingStatusEnum;
import com.catalis.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitSequenceTypeEnum;
import com.catalis.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitSpanishSchemeEnum;
import com.catalis.core.utils.annotations.FilterableId;
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
public class TransactionLineDirectDebitDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionLineDirectDebitId;

    @FilterableId
    private Long transactionId;

    @FilterableId
    private String directDebitMandateId;

    @FilterableId
    private String directDebitCreditorId;

    private String directDebitReference;
    private DirectDebitSequenceTypeEnum directDebitSequenceType;
    private LocalDate directDebitDueDate;
    private String directDebitPaymentMethod;
    private String directDebitDebtorName;
    private String directDebitDebtorAddress;
    private String directDebitDebtorContact;
    private DirectDebitProcessingStatusEnum directDebitProcessingStatus;
    private LocalDateTime directDebitAuthorizationDate;
    private LocalDateTime directDebitRevocationDate;
    private DirectDebitSpanishSchemeEnum directDebitSpanishScheme;

}