package com.firefly.core.banking.ledger.interfaces.dtos.sepa.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.ledger.interfaces.enums.sepa.v1.SepaSpanishSchemeEnum;
import com.firefly.core.banking.ledger.interfaces.enums.sepa.v1.SepaTransactionStatusEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.firefly.annotations.ValidAmount;
import com.firefly.annotations.ValidBic;
import com.firefly.annotations.ValidCurrencyCode;
import com.firefly.annotations.ValidDate;
import com.firefly.annotations.ValidDateTime;
import com.firefly.annotations.ValidIban;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.UUID;
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLineSepaTransferDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID transactionLineSepaId;

    @FilterableId
    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @FilterableId
    @NotBlank(message = "SEPA end-to-end ID is required")
    @Size(max = 35, message = "SEPA end-to-end ID cannot exceed 35 characters")
    private String sepaEndToEndId;

    @Size(max = 140, message = "SEPA remittance info cannot exceed 140 characters")
    private String sepaRemittanceInfo;

    @NotBlank(message = "SEPA origin IBAN is required")
    @ValidIban
    private String sepaOriginIban;

    @ValidBic
    private String sepaOriginBic;

    @NotBlank(message = "SEPA destination IBAN is required")
    @ValidIban
    private String sepaDestinationIban;

    @ValidBic
    private String sepaDestinationBic;

    @NotNull(message = "SEPA transaction status is required")
    private SepaTransactionStatusEnum sepaTransactionStatus;

    @FilterableId
    @Size(max = 35, message = "SEPA creditor ID cannot exceed 35 characters")
    private String sepaCreditorId;

    @FilterableId
    @Size(max = 35, message = "SEPA debtor ID cannot exceed 35 characters")
    private String sepaDebtorId;

    @ValidBic
    private String sepaInitiatingAgentBic;

    @ValidBic
    private String sepaIntermediaryBic;

    @Size(max = 4, message = "SEPA transaction purpose cannot exceed 4 characters")
    private String sepaTransactionPurpose;

    @ValidDate
    private LocalDate sepaRequestedExecutionDate;

    @DecimalMin(value = "0.0", message = "SEPA exchange rate cannot be negative")
    private BigDecimal sepaExchangeRate;

    @ValidAmount
    private BigDecimal sepaFeeAmount;

    @ValidCurrencyCode
    private String sepaFeeCurrency;

    @Size(max = 70, message = "SEPA recipient name cannot exceed 70 characters")
    private String sepaRecipientName;

    @Size(max = 140, message = "SEPA recipient address cannot exceed 140 characters")
    private String sepaRecipientAddress;

    @ValidDateTime
    private LocalDateTime sepaProcessingDate;

    @Size(max = 500, message = "SEPA notes cannot exceed 500 characters")
    private String sepaNotes;

    @NotNull(message = "SEPA payment scheme is required")
    private SepaSpanishSchemeEnum sepaPaymentScheme;
}