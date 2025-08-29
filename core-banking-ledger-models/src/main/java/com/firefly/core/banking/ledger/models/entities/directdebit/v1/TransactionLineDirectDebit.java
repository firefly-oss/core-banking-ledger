package com.firefly.core.banking.ledger.models.entities.directdebit.v1;

import com.firefly.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitProcessingStatusEnum;
import com.firefly.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitSequenceTypeEnum;
import com.firefly.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitSpanishSchemeEnum;
import com.firefly.core.banking.ledger.models.entities.BaseEntity;
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
@Table("transaction_line_direct_debit")
public class TransactionLineDirectDebit extends BaseEntity {
    @Id
    @Column("transaction_line_direct_debit_id")
    private Long transactionLineDirectDebitId;

    @Column("transaction_id")
    private Long transactionId;

    @Column("direct_debit_mandate_id")
    private String directDebitMandateId;

    @Column("direct_debit_creditor_id")
    private String directDebitCreditorId;

    @Column("direct_debit_reference")
    private String directDebitReference;

    @Column("direct_debit_sequence_type")
    private DirectDebitSequenceTypeEnum directDebitSequenceType;

    @Column("direct_debit_due_date")
    private LocalDate directDebitDueDate;

    @Column("direct_debit_payment_method")
    private String directDebitPaymentMethod;

    @Column("direct_debit_debtor_name")
    private String directDebitDebtorName;

    @Column("direct_debit_debtor_address")
    private String directDebitDebtorAddress;

    @Column("direct_debit_debtor_contact")
    private String directDebitDebtorContact;

    @Column("direct_debit_processing_status")
    private DirectDebitProcessingStatusEnum directDebitProcessingStatus;

    @Column("direct_debit_authorization_date")
    private LocalDateTime directDebitAuthorizationDate;

    @Column("direct_debit_revocation_date")
    private LocalDateTime directDebitRevocationDate;

    @Column("direct_debit_spanish_scheme")
    private DirectDebitSpanishSchemeEnum directDebitSpanishScheme;
}

