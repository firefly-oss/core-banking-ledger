package com.catalis.core.banking.ledger.interfaces.dtos.card.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionFilterDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TransactionCardFilterDTO extends TransactionFilterDTO {
    private List<String> merchantCodes;
    private List<String> terminalIds;
    private String cardHolderCountry;
    private Boolean cardPresentOnly;
    private Boolean fraudFlaggedOnly;
    private String merchantName;
    private String posEntryMode;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
}