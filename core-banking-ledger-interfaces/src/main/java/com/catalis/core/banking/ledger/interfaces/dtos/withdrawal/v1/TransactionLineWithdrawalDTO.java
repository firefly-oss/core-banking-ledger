package com.catalis.core.banking.ledger.interfaces.dtos.withdrawal.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO representing detailed information about a withdrawal transaction.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineWithdrawalDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionLineWithdrawalId;

    @FilterableId
    private Long transactionId;

    private String withdrawalMethod;
    private String withdrawalReference;
    private String withdrawalLocation;
    private String withdrawalNotes;
    private String withdrawalConfirmationCode;
    private String withdrawalReceiptNumber;
    private String withdrawalAtmId;
    private String withdrawalBranchId;
    private LocalDateTime withdrawalTimestamp;
    private String withdrawalProcessedBy;
    private String withdrawalAuthorizationCode;
    private Boolean withdrawalDailyLimitCheck;
    private BigDecimal withdrawalDailyAmountUsed;
    private BigDecimal withdrawalDailyLimit;
    private String withdrawalSpanishTaxCode;
}
