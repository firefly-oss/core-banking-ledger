package com.catalis.core.banking.ledger.models.entities.leg.v1;

import com.catalis.core.banking.ledger.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a transaction leg in double-entry accounting.
 * Each transaction consists of at least two legs (debit and credit).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("transaction_leg")
public class TransactionLeg extends BaseEntity {
    @Id
    @Column("transaction_leg_id")
    private Long transactionLegId;

    @Column("transaction_id")
    private Long transactionId;

    @Column("account_id")
    private Long accountId;

    @Column("account_space_id")
    private Long accountSpaceId;

    @Column("leg_type")
    private String legType;  // "DEBIT" or "CREDIT"

    @Column("amount")
    private BigDecimal amount;

    @Column("currency")
    private String currency;

    @Column("description")
    private String description;

    @Column("value_date")
    private LocalDateTime valueDate;

    @Column("booking_date")
    private LocalDateTime bookingDate;
}
