package com.firefly.core.banking.ledger.interfaces.dtos.core.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionId;

    private String externalReference;
    private LocalDateTime transactionDate;
    private LocalDateTime valueDate;
    private TransactionTypeEnum transactionType;
    private TransactionStatusEnum transactionStatus;
    private BigDecimal totalAmount;
    private String currency;
    private String description;
    private String initiatingParty;

    /**
     * Reference to account ID in external account microservice.
     */
    @FilterableId
    private Long accountId;

    /**
     * Reference to account space ID in external account microservice.
     */
    @FilterableId
    private Long accountSpaceId;

    /**
     * Reference to category ID in external master data microservice.
     */
    @FilterableId
    private Long transactionCategoryId;

    private String branchOfficeCode;
    private String nifInitiatingParty;

    // Geotag fields
    private Double latitude;
    private Double longitude;
    private String locationName;
    private String country;
    private String city;
    private String postalCode;

    // Relation fields
    @FilterableId
    private Long relatedTransactionId;
    private String relationType;
    private String requestId;
    private String batchId;
    private LocalDateTime bookingDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long rowVersion;

    // Compliance fields
    private Integer amlRiskScore;
    private String amlScreeningResult;
    private Boolean amlLargeTxnFlag;
    private String scaMethod;
    private String scaResult;
    private Boolean instantFlag;
    private String confirmationOfPayeeResult;
}
