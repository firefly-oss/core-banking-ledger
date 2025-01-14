package com.catalis.core.banking.ledger.models.entities.ledger.v1;

import com.catalis.core.banking.ledger.interfaces.enums.ledger.v1.LedgerDebitCreditIndicatorEnum;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("ledger_entry")
public class LedgerEntry extends BaseEntity {

    @Id
    @Column("ledger_entry_id")
    private Long ledgerEntryId;

    @Column("transaction_id")
    private Long transactionId;

    @Column("ledger_account_id")
    private Long ledgerAccountId;

    @Column("debit_credit_indicator")
    private LedgerDebitCreditIndicatorEnum debitCreditIndicator;

    @Column("amount")
    private BigDecimal amount;

    @Column("currency")
    private String currency;

    @Column("posting_date")
    private LocalDateTime postingDate;

    @Column("exchange_rate")
    private BigDecimal exchangeRate;

    @Column("cost_center_id")
    private Long costCenterId;

    @Column("notes")
    private String notes;
}