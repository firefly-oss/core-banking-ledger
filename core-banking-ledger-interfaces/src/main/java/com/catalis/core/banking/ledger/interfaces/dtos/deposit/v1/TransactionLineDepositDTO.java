package com.catalis.core.banking.ledger.interfaces.dtos.deposit.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO representing detailed information about a deposit transaction.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineDepositDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionLineDepositId;

    @FilterableId
    private Long transactionId;

    private String depositMethod;
    private String depositReference;
    private String depositLocation;
    private String depositNotes;
    private String depositConfirmationCode;
    private String depositReceiptNumber;
    private String depositAtmId;
    private String depositBranchId;
    private BigDecimal depositCashAmount;
    private BigDecimal depositCheckAmount;
    private String depositCheckNumber;
    private LocalDate depositCheckDate;
    private String depositCheckBank;
    private LocalDateTime depositTimestamp;
    private String depositProcessedBy;
    private String depositSpanishTaxCode;
}
