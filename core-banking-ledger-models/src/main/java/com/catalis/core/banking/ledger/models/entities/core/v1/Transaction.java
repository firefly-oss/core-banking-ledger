package com.catalis.core.banking.ledger.models.entities.core.v1;

import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
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
@Table("transaction")
public class Transaction extends BaseEntity {
    @Id
    @Column("transaction_id")
    private Long transactionId;

    @Column("external_reference")
    private String externalReference;

    @Column("transaction_date")
    private LocalDateTime transactionDate;

    @Column("value_date")
    private LocalDateTime valueDate;

    @Column("transaction_type")
    private TransactionTypeEnum transactionType;

    @Column("transaction_status")
    private TransactionStatusEnum transactionStatus;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("currency")
    private String currency;

    @Column("description")
    private String description;

    @Column("initiating_party")
    private String initiatingParty;

    @Column("account_id")
    private Long accountId;

    @Column("transaction_category_id")
    private Long transactionCategoryId;
}