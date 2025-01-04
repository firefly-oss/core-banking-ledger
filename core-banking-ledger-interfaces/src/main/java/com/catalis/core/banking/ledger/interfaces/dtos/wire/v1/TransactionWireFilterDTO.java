package com.catalis.core.banking.ledger.interfaces.dtos.wire.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionFilterDTO;
import com.catalis.core.banking.ledger.interfaces.enums.wire.v1.WireTransferPriorityEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransactionWireFilterDTO extends TransactionFilterDTO {
    // Amount related
    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    // Wire specific identifiers
    private String wireTransferReference;
    private List<String> originSwiftCodes;
    private List<String> destinationSwiftCodes;
    private String originAccountNumber;
    private String destinationAccountNumber;

    // Status and type filters
    private WireTransferPriorityEnum priority;
    private Boolean cancelled;
    private String wireReceptionStatus;

    // Party information
    private String beneficiaryName;
    private String instructingParty;
    private String beneficiaryAddress;

    // Exchange rate filters
    private BigDecimal minExchangeRate;
    private BigDecimal maxExchangeRate;

    // Fee filters
    private BigDecimal minFeeAmount;
    private BigDecimal maxFeeAmount;
    private List<String> feeCurrencies;
}