package com.catalis.core.banking.ledger.interfaces.dtos.wire.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.ledger.interfaces.enums.wire.v1.WireTransferPriorityEnum;
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
public class TransactionLineWireTransferDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionLineWireTransferId;

    private Long transactionId;
    private String wireTransferReference;
    private String wireOriginSwiftBic;
    private String wireDestinationSwiftBic;
    private String wireOriginAccountNumber;
    private String wireDestinationAccountNumber;
    private String wireTransferPurpose;
    private WireTransferPriorityEnum wireTransferPriority;
    private BigDecimal wireExchangeRate;
    private BigDecimal wireFeeAmount;
    private String wireFeeCurrency;
    private String wireInstructingParty;
    private String wireBeneficiaryName;
    private String wireBeneficiaryAddress;
    private LocalDateTime wireProcessingDate;
    private String wireTransactionNotes;
    private String wireReceptionStatus;
    private String wireDeclineReason;
    private Boolean wireCancelledFlag;
}
