package com.firefly.core.banking.ledger.interfaces.dtos.leg.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO representing a transaction leg in double-entry accounting.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionLegDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionLegId;

    @FilterableId
    private Long transactionId;

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

    private String legType;  // "DEBIT" or "CREDIT"
    private BigDecimal amount;
    private String currency;
    private String description;
    private LocalDateTime valueDate;
    private LocalDateTime bookingDate;
}
