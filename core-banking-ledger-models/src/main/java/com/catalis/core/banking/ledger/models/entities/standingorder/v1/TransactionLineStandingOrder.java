package com.catalis.core.banking.ledger.models.entities.standingorder.v1;

import com.catalis.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderFrequencyEnum;
import com.catalis.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderStatusEnum;
import com.catalis.core.banking.ledger.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("transaction_line_standing_order")
public class TransactionLineStandingOrder extends BaseEntity {
    @Id
    @Column("transaction_line_standing_order_id")
    private Long transactionLineStandingOrderId;

    @Column("transaction_id")
    private Long transactionId;

    @Column("standing_order_id")
    private String standingOrderId;

    @Column("standing_order_frequency")
    private StandingOrderFrequencyEnum standingOrderFrequency;

    @Column("standing_order_start_date")
    private LocalDate standingOrderStartDate;

    @Column("standing_order_end_date")
    private LocalDate standingOrderEndDate;

    @Column("standing_order_next_execution_date")
    private LocalDate standingOrderNextExecutionDate;

    @Column("standing_order_reference")
    private String standingOrderReference;

    @Column("standing_order_recipient_name")
    private String standingOrderRecipientName;

    @Column("standing_order_recipient_iban")
    private String standingOrderRecipientIban;

    @Column("standing_order_recipient_bic")
    private String standingOrderRecipientBic;

    @Column("standing_order_purpose")
    private String standingOrderPurpose;

    @Column("standing_order_status")
    private StandingOrderStatusEnum standingOrderStatus;

    @Column("standing_order_notes")
    private String standingOrderNotes;

    @Column("standing_order_last_execution_date")
    private LocalDate standingOrderLastExecutionDate;

    @Column("standing_order_total_executions")
    private Integer standingOrderTotalExecutions;

    @Column("standing_order_cancelled_date")
    private LocalDateTime standingOrderCancelledDate;

    @Column("standing_order_suspended_until_date")
    private LocalDate standingOrderSuspendedUntilDate;

    @Column("standing_order_created_by")
    private String standingOrderCreatedBy;

    @Column("standing_order_updated_by")
    private String standingOrderUpdatedBy;

    @Column("standing_order_creation_timestamp")
    private LocalDateTime standingOrderCreationTimestamp;

    @Column("standing_order_update_timestamp")
    private LocalDateTime standingOrderUpdateTimestamp;

    @Column("standing_order_spanish_tax_flag")
    private Boolean standingOrderSpanishTaxFlag;
}