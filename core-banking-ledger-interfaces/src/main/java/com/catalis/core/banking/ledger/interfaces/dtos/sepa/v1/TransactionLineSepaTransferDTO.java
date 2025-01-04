package com.catalis.core.banking.ledger.interfaces.dtos.sepa.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.ledger.interfaces.enums.sepa.v1.SepaTransactionStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineSepaTransferDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionLineSepaId;

    private Long transactionId;
    private String sepaEndToEndId;
    private String sepaRemittanceInfo;
    private String sepaOriginIban;
    private String sepaOriginBic;
    private String sepaDestinationIban;
    private String sepaDestinationBic;
    private SepaTransactionStatusEnum sepaTransactionStatus;
    private String sepaCreditorId;
    private String sepaDebtorId;
    private String sepaInitiatingAgentBic;
    private String sepaIntermediaryBic;
    private String sepaTransactionPurpose;
    private LocalDate sepaRequestedExecutionDate;
    private BigDecimal sepaExchangeRate;
    private BigDecimal sepaFeeAmount;
    private String sepaFeeCurrency;
    private String sepaRecipientName;
    private String sepaRecipientAddress;
    private LocalDateTime sepaProcessingDate;
    private String sepaNotes;
}
