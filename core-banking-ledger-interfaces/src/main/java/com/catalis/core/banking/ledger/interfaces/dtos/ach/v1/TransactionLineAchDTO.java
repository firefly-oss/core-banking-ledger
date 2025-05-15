package com.catalis.core.banking.ledger.interfaces.dtos.ach.v1;

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
 * DTO representing detailed information about an ACH (Automated Clearing House) transaction.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineAchDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionLineAchId;

    @FilterableId
    private Long transactionId;

    private String achReference;
    
    @FilterableId
    private Long achSourceAccountId;
    
    @FilterableId
    private Long achDestinationAccountId;
    
    private String achSourceAccountNumber;
    private String achDestinationAccountNumber;
    private String achSourceAccountName;
    private String achDestinationAccountName;
    private String achRoutingNumber;
    private String achTransactionCode;
    private String achPurpose;
    private String achNotes;
    private LocalDateTime achTimestamp;
    private String achProcessedBy;
    private BigDecimal achFeeAmount;
    private String achFeeCurrency;
    private LocalDate achScheduledDate;
    private LocalDate achExecutionDate;
    private String achBatchNumber;
    private String achTraceNumber;
    private String achEntryClassCode;
    private LocalDate achSettlementDate;
    private String achReturnCode;
    private String achReturnReason;
}