package com.catalis.core.banking.ledger.interfaces.dtos.core.v1;

import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFilterDTO {
    // Time range filters
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    // Amount range filters
    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    // Basic filters
    private List<String> currencies;
    private List<TransactionTypeEnum> types;
    private List<TransactionStatusEnum> statuses;
    private List<Long> categoryIds;
    private List<Long> accountIds;

    // Reference and description filters
    private String referenceNumber;
    private String description;
    private String initiatingParty;

    // Additional metadata filters
    private Boolean includeReversed;
    private Boolean onlyFailed;
    private Boolean onlyPending;
}
