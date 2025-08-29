package com.firefly.core.banking.ledger.models.entities.core.v1;

import com.firefly.core.banking.ledger.interfaces.enums.asset.v1.AssetTypeEnum;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
import com.firefly.core.banking.ledger.models.entities.BaseEntity;
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

    /**
     * Reference to account ID in external account microservice.
     * This is a logical reference to an account managed in a separate microservice.
     */
    @Column("account_id")
    private Long accountId;

    /**
     * Reference to account space ID in external account microservice.
     * This is a logical reference to an account space managed in a separate microservice.
     */
    @Column("account_space_id")
    private Long accountSpaceId;

    /**
     * Reference to category ID in external master data microservice.
     * This is no longer a foreign key to an internal table but a logical reference
     * to a category managed in a separate microservice.
     */
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

    // Crypto and blockchain fields
    /**
     * Type of asset (FIAT, CRYPTOCURRENCY, TOKEN_SECURITY, etc.)
     * Defaults to FIAT for backward compatibility
     */
    @Column("asset_type")
    private AssetTypeEnum assetType = AssetTypeEnum.FIAT;

    /**
     * Reference to blockchain_network table for cryptocurrency and token transactions
     * Only populated when assetType is not FIAT
     */
    @Column("blockchain_network_id")
    private Long blockchainNetworkId;

    /**
     * Transaction hash on the blockchain
     * Only populated for crypto transactions
     */
    @Column("blockchain_transaction_hash")
    private String blockchainTransactionHash;

    /**
     * Result of compliance checks specific to crypto transactions
     * E.g., "APPROVED", "REJECTED", "MANUAL_REVIEW"
     */
    @Column("crypto_compliance_check_result")
    private String cryptoComplianceCheckResult;

    /**
     * Risk score for crypto addresses involved in the transaction
     * Higher values indicate higher risk
     */
    @Column("crypto_address_risk_score")
    private Integer cryptoAddressRiskScore;

    /**
     * Source of the crypto transaction
     * E.g., "EXCHANGE", "WALLET", "SMART_CONTRACT"
     */
    @Column("crypto_transaction_source")
    private String cryptoTransactionSource;
}
