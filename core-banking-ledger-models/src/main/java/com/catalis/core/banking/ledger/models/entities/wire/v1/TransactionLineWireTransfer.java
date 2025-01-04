package com.catalis.core.banking.ledger.models.entities.wire.v1;

import com.catalis.core.banking.ledger.interfaces.enums.wire.v1.WireTransferPriorityEnum;
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
@Table("transaction_line_wire_transfer")
public class TransactionLineWireTransfer extends BaseEntity {
    @Id
    @Column("transaction_line_wire_transfer_id")
    private Long transactionLineWireTransferId;

    @Column("transaction_id")
    private Long transactionId;

    @Column("wire_transfer_reference")
    private String wireTransferReference;

    @Column("wire_origin_swift_bic")
    private String wireOriginSwiftBic;

    @Column("wire_destination_swift_bic")
    private String wireDestinationSwiftBic;

    @Column("wire_origin_account_number")
    private String wireOriginAccountNumber;

    @Column("wire_destination_account_number")
    private String wireDestinationAccountNumber;

    @Column("wire_transfer_purpose")
    private String wireTransferPurpose;

    @Column("wire_transfer_priority")
    private WireTransferPriorityEnum wireTransferPriority;

    @Column("wire_exchange_rate")
    private BigDecimal wireExchangeRate;

    @Column("wire_fee_amount")
    private BigDecimal wireFeeAmount;

    @Column("wire_fee_currency")
    private String wireFeeCurrency;

    @Column("wire_instructing_party")
    private String wireInstructingParty;

    @Column("wire_beneficiary_name")
    private String wireBeneficiaryName;

    @Column("wire_beneficiary_address")
    private String wireBeneficiaryAddress;

    @Column("wire_processing_date")
    private LocalDateTime wireProcessingDate;

    @Column("wire_transaction_notes")
    private String wireTransactionNotes;

    @Column("wire_reception_status")
    private String wireReceptionStatus;

    @Column("wire_decline_reason")
    private String wireDeclineReason;

    @Column("wire_cancelled_flag")
    private Boolean wireCancelledFlag;
}
