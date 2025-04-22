package com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.ledger.interfaces.enums.ledger.v1.LedgerDebitCreditIndicatorEnum;
import com.catalis.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LedgerEntryDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long ledgerEntryId;

    @FilterableId
    private Long transactionId;

    @FilterableId
    private Long ledgerAccountId;

    private LedgerDebitCreditIndicatorEnum debitCreditIndicator;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime postingDate;
    private BigDecimal exchangeRate;
    private Long costCenterId;
    private String notes;
}
