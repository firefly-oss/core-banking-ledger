package com.firefly.core.banking.ledger.interfaces.dtos.money.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.annotations.ValidAmount;
import com.firefly.annotations.ValidCurrencyCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

import java.util.UUID;
/**
 * DTO representing a money value with amount and currency.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MoneyDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID moneyId;

    @NotNull(message = "Amount is required")
    @ValidAmount
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @ValidCurrencyCode
    private String currency;
}
