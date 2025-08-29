package com.firefly.core.banking.ledger.models.entities.fee.v1;

import com.firefly.core.banking.ledger.models.entities.BaseEntity;
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
 * Entity representing detailed information about a fee transaction.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("transaction_line_fee")
public class TransactionLineFee extends BaseEntity {
    @Id
    @Column("transaction_line_fee_id")
    private Long transactionLineFeeId;

    @Column("transaction_id")
    private Long transactionId;

    @Column("fee_type")
    private String feeType;

    @Column("fee_description")
    private String feeDescription;

    @Column("fee_reference")
    private String feeReference;

    @Column("fee_related_transaction_id")
    private Long feeRelatedTransactionId;

    @Column("fee_related_service")
    private String feeRelatedService;

    @Column("fee_calculation_method")
    private String feeCalculationMethod;

    @Column("fee_calculation_base")
    private BigDecimal feeCalculationBase;

    @Column("fee_rate_percentage")
    private BigDecimal feeRatePercentage;

    @Column("fee_fixed_amount")
    private BigDecimal feeFixedAmount;

    @Column("fee_currency")
    private String feeCurrency;

    @Column("fee_waived")
    private Boolean feeWaived;

    @Column("fee_waiver_reason")
    private String feeWaiverReason;

    @Column("fee_waiver_authorized_by")
    private String feeWaiverAuthorizedBy;

    @Column("fee_timestamp")
    private LocalDateTime feeTimestamp;

    @Column("fee_processed_by")
    private String feeProcessedBy;

    @Column("fee_spanish_tax_code")
    private String feeSpanishTaxCode;
}
