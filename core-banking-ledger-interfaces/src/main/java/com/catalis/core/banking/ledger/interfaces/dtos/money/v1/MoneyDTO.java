package com.catalis.core.banking.ledger.interfaces.dtos.money.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * DTO representing a money value with amount and currency.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MoneyDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long moneyId;

    private BigDecimal amount;
    private String currency;
}
