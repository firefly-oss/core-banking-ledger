package com.catalis.core.banking.ledger.interfaces.dtos.sepa.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionFilterDTO;
import com.catalis.core.banking.ledger.interfaces.enums.sepa.v1.SepaTransactionStatusEnum;
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
public class TransactionSepaFilterDTO extends TransactionFilterDTO {
    private String endToEndId;
    private List<String> creditorIds;
    private List<String> debtorIds;
    private List<SepaTransactionStatusEnum> sepaStatuses;
    private String originIban;
    private String destinationIban;
    private String originBic;
    private String destinationBic;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
}