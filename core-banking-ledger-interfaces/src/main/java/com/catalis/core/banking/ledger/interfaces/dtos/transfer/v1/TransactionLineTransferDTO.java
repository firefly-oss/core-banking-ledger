package com.catalis.core.banking.ledger.interfaces.dtos.transfer.v1;

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
 * DTO representing detailed information about an internal transfer transaction.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineTransferDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionLineTransferId;

    @FilterableId
    private Long transactionId;

    private String transferReference;
    
    @FilterableId
    private Long transferSourceAccountId;
    
    @FilterableId
    private Long transferDestinationAccountId;
    
    private String transferSourceAccountNumber;
    private String transferDestinationAccountNumber;
    private String transferSourceAccountName;
    private String transferDestinationAccountName;
    private String transferPurpose;
    private String transferNotes;
    private LocalDateTime transferTimestamp;
    private String transferProcessedBy;
    private BigDecimal transferFeeAmount;
    private String transferFeeCurrency;
    private LocalDate transferScheduledDate;
    private LocalDate transferExecutionDate;
    private String transferSpanishTaxCode;
}
