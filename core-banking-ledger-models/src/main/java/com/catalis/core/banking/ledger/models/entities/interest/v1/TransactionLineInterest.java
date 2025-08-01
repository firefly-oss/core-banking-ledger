package com.catalis.core.banking.ledger.models.entities.interest.v1;

import com.catalis.core.banking.ledger.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing detailed information about an interest transaction.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("transaction_line_interest")
public class TransactionLineInterest extends BaseEntity {
    @Id
    @Column("transaction_line_interest_id")
    private Long transactionLineInterestId;

    @Column("transaction_id")
    private Long transactionId;

    @Column("interest_type")
    private String interestType;

    @Column("interest_description")
    private String interestDescription;

    @Column("interest_reference")
    private String interestReference;

    @Column("interest_related_account_id")
    private Long interestRelatedAccountId;

    @Column("interest_calculation_method")
    private String interestCalculationMethod;

    @Column("interest_calculation_base")
    private BigDecimal interestCalculationBase;

    @Column("interest_rate_percentage")
    private BigDecimal interestRatePercentage;

    @Column("interest_accrual_start_date")
    private LocalDate interestAccrualStartDate;

    @Column("interest_accrual_end_date")
    private LocalDate interestAccrualEndDate;

    @Column("interest_days_calculated")
    private Integer interestDaysCalculated;

    @Column("interest_currency")
    private String interestCurrency;

    @Column("interest_tax_withheld_amount")
    private BigDecimal interestTaxWithheldAmount;

    @Column("interest_tax_withheld_rate")
    private BigDecimal interestTaxWithheldRate;

    @Column("interest_gross_amount")
    private BigDecimal interestGrossAmount;

    @Column("interest_net_amount")
    private BigDecimal interestNetAmount;

    @Column("interest_timestamp")
    private LocalDateTime interestTimestamp;

    @Column("interest_processed_by")
    private String interestProcessedBy;

    @Column("interest_spanish_tax_code")
    private String interestSpanishTaxCode;
}
