package com.firefly.core.banking.ledger.models.entities.withdrawal.v1;

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

import java.util.UUID;
/**
 * Entity representing detailed information about a withdrawal transaction.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("transaction_line_withdrawal")
public class TransactionLineWithdrawal extends BaseEntity {
    @Id
    @Column("transaction_line_withdrawal_id")
    private UUID transactionLineWithdrawalId;

    @Column("transaction_id")
    private UUID transactionId;

    @Column("withdrawal_method")
    private String withdrawalMethod;

    @Column("withdrawal_reference")
    private String withdrawalReference;

    @Column("withdrawal_location")
    private String withdrawalLocation;

    @Column("withdrawal_notes")
    private String withdrawalNotes;

    @Column("withdrawal_confirmation_code")
    private String withdrawalConfirmationCode;

    @Column("withdrawal_receipt_number")
    private String withdrawalReceiptNumber;

    @Column("withdrawal_atm_id")
    private String withdrawalAtmId;

    @Column("withdrawal_branch_id")
    private String withdrawalBranchId;

    @Column("withdrawal_timestamp")
    private LocalDateTime withdrawalTimestamp;

    @Column("withdrawal_processed_by")
    private String withdrawalProcessedBy;

    @Column("withdrawal_authorization_code")
    private String withdrawalAuthorizationCode;

    @Column("withdrawal_daily_limit_check")
    private Boolean withdrawalDailyLimitCheck;

    @Column("withdrawal_daily_amount_used")
    private BigDecimal withdrawalDailyAmountUsed;

    @Column("withdrawal_daily_limit")
    private BigDecimal withdrawalDailyLimit;

    @Column("withdrawal_spanish_tax_code")
    private String withdrawalSpanishTaxCode;
}
