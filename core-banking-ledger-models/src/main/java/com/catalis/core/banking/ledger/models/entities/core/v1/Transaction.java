package com.catalis.core.banking.ledger.models.entities.core.v1;

import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
import com.catalis.core.banking.ledger.models.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
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

    @Column("account_space_id")
    private Long accountSpaceId;

    @Column("transaction_category_id")
    private Long transactionCategoryId;

    @Column("branch_office_code")
    private String branchOfficeCode;

    @Column("nif_initiating_party")
    private String nifInitiatingParty;

    // Geotag fields
    @Column("latitude")
    private Double latitude;

    @Column("longitude")
    private Double longitude;

    @Column("location_name")
    private String locationName;

    @Column("country")
    private String country;

    @Column("city")
    private String city;

    @Column("postal_code")
    private String postalCode;

    // Relation fields
    @Column("related_transaction_id")
    private Long relatedTransactionId;

    @Column("relation_type")
    private String relationType;

    @Column("request_id")
    private String requestId;

    @Column("batch_id")
    private String batchId;

    @Column("booking_date")
    private LocalDateTime bookingDate;

    @Version
    @Column("row_version")
    private Long rowVersion;

    // Compliance fields
    @Column("aml_risk_score")
    private Integer amlRiskScore;

    @Column("aml_screening_result")
    private String amlScreeningResult;

    @Column("aml_large_txn_flag")
    private Boolean amlLargeTxnFlag;

    @Column("sca_method")
    private String scaMethod;

    @Column("sca_result")
    private String scaResult;

    @Column("instant_flag")
    private Boolean instantFlag;

    @Column("confirmation_of_payee_result")
    private String confirmationOfPayeeResult;
}
