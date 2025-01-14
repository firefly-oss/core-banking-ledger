package com.catalis.core.banking.ledger.interfaces.dtos.card.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineCardDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionLineCardId;

    private Long transactionId;
    private String cardAuthCode;
    private String cardMerchantCategoryCode;
    private String cardMerchantName;
    private String cardPosEntryMode;
    private String cardTransactionReference;
    private String cardTerminalId;
    private String cardHolderCountry;
    private Boolean cardPresentFlag;
    private LocalDateTime cardTransactionTimestamp;
    private Boolean cardFraudFlag;
    private BigDecimal cardCurrencyConversionRate;
    private BigDecimal cardFeeAmount;
    private String cardFeeCurrency;
    private String cardInstallmentPlan;
    private String cardMerchantCif;
}
