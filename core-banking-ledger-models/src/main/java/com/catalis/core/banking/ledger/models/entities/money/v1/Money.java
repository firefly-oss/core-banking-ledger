package com.catalis.core.banking.ledger.models.entities.money.v1;

import com.catalis.core.banking.ledger.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * Entity representing a money value with amount and currency.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("money")
public class Money extends BaseEntity {
    @Id
    @Column("money_id")
    private Long moneyId;

    @Column("amount")
    private BigDecimal amount;

    @Column("currency")
    private String currency;
}
