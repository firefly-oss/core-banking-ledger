package com.firefly.core.banking.ledger.models.entities.sepa.v1;

import com.firefly.core.banking.ledger.interfaces.enums.sepa.v1.SepaSpanishSchemeEnum;
import com.firefly.core.banking.ledger.interfaces.enums.sepa.v1.SepaTransactionStatusEnum;
import com.firefly.core.banking.ledger.models.entities.BaseEntity;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("transaction_line_sepa_transfer")
public class TransactionLineSepaTransfer extends BaseEntity {
    @Id
    @Column("transaction_line_sepa_id")
    private Long transactionLineSepaId;

    @Column("transaction_id")
    private Long transactionId;

    @Column("sepa_end_to_end_id")
    private String sepaEndToEndId;

    @Column("sepa_remittance_info")
    private String sepaRemittanceInfo;

    @Column("sepa_origin_iban")
    private String sepaOriginIban;

    @Column("sepa_origin_bic")
    private String sepaOriginBic;

    @Column("sepa_destination_iban")
    private String sepaDestinationIban;

    @Column("sepa_destination_bic")
    private String sepaDestinationBic;

    @Column("sepa_transaction_status")
    private SepaTransactionStatusEnum sepaTransactionStatus;

    @Column("sepa_creditor_id")
    private String sepaCreditorId;

    @Column("sepa_debtor_id")
    private String sepaDebtorId;

    @Column("sepa_initiating_agent_bic")
    private String sepaInitiatingAgentBic;

    @Column("sepa_intermediary_bic")
    private String sepaIntermediaryBic;

    @Column("sepa_transaction_purpose")
    private String sepaTransactionPurpose;

    @Column("sepa_requested_execution_date")
    private LocalDate sepaRequestedExecutionDate;

    @Column("sepa_exchange_rate")
    private BigDecimal sepaExchangeRate;

    @Column("sepa_fee_amount")
    private BigDecimal sepaFeeAmount;

    @Column("sepa_fee_currency")
    private String sepaFeeCurrency;

    @Column("sepa_recipient_name")
    private String sepaRecipientName;

    @Column("sepa_recipient_address")
    private String sepaRecipientAddress;

    @Column("sepa_processing_date")
    private LocalDateTime sepaProcessingDate;

    @Column("sepa_notes")
    private String sepaNotes;

    @Column("sepa_payment_scheme")
    private SepaSpanishSchemeEnum sepaPaymentScheme;
}

