package com.catalis.core.banking.ledger.interfaces.dtos.directdebit.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionFilterDTO;
import com.catalis.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitProcessingStatusEnum;
import com.catalis.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitSequenceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransactionDirectDebitFilterDTO extends TransactionFilterDTO {
    // Amount related filters
    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    // Direct Debit specific identifiers
    private String mandateId;
    private String creditorId;
    private String debtorName;
    private String debtorAddress;

    // Status and type filters
    private DirectDebitProcessingStatusEnum processingStatus;
    private DirectDebitSequenceTypeEnum sequenceType;
    private String paymentMethod;

    // Date filters
    private LocalDate dueDateStart;
    private LocalDate dueDateEnd;
    private LocalDate authorizationDateStart;
    private LocalDate authorizationDateEnd;

    // Revocation filters
    private Boolean isRevoked;
}