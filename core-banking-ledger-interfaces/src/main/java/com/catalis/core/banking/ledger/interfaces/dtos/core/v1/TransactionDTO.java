package com.catalis.core.banking.ledger.interfaces.dtos.core.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
import com.catalis.core.utils.annotations.FilterableId;
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

    @FilterableId
    private Long accountId;

    @FilterableId
    private Long accountSpaceId;

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
