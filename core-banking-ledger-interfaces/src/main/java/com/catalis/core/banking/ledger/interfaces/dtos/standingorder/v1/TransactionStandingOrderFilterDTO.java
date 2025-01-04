package com.catalis.core.banking.ledger.interfaces.dtos.standingorder.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionFilterDTO;
import com.catalis.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderFrequencyEnum;
import com.catalis.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TransactionStandingOrderFilterDTO extends TransactionFilterDTO {
    // Amount related filters
    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    // Standing Order specific identifiers
    private String standingOrderId;
    private List<String> recipientIbans;
    private List<String> recipientBics;
    private String recipientName;

    // Status and type filters
    private StandingOrderStatusEnum status;
    private StandingOrderFrequencyEnum frequency;

    // Date filters
    private LocalDate startDateFrom;
    private LocalDate startDateTo;
    private LocalDate endDateFrom;
    private LocalDate endDateTo;
    private LocalDate nextExecutionDateFrom;
    private LocalDate nextExecutionDateTo;

    // Additional filters
    private Boolean isSuspended;
    private Integer minTotalExecutions;
    private Integer maxTotalExecutions;
    private String createdBy;
    private String updatedBy;
}
