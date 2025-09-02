package com.firefly.core.banking.ledger.interfaces.dtos.statement.v1;

import com.firefly.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.firefly.annotations.ValidAmount;
import com.firefly.annotations.ValidCurrencyCode;
import com.firefly.annotations.ValidDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.UUID;
/**
 * DTO for an individual entry in a statement.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class StatementEntryDTO extends BaseDTO {
    /**
     * The transaction ID associated with this entry.
     */
    @FilterableId
    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    /**
     * The date of the transaction.
     */
    @NotNull(message = "Transaction date is required")
    @ValidDateTime
    private LocalDateTime transactionDate;

    /**
     * The value date of the transaction.
     */
    @NotNull(message = "Value date is required")
    @ValidDateTime
    private LocalDateTime valueDate;

    /**
     * The booking date of the transaction.
     */
    @ValidDateTime
    private LocalDateTime bookingDate;

    /**
     * The type of the transaction.
     */
    @NotNull(message = "Transaction type is required")
    private TransactionTypeEnum transactionType;

    /**
     * The status of the transaction.
     */
    @NotNull(message = "Transaction status is required")
    private TransactionStatusEnum transactionStatus;

    /**
     * The amount of the transaction.
     */
    @NotNull(message = "Amount is required")
    @ValidAmount
    private BigDecimal amount;

    /**
     * The currency of the transaction.
     */
    @NotBlank(message = "Currency is required")
    @ValidCurrencyCode
    private String currency;
    
    /**
     * The description of the transaction.
     */
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    /**
     * The initiating party of the transaction.
     */
    @Size(max = 100, message = "Initiating party cannot exceed 100 characters")
    private String initiatingParty;

    /**
     * The external reference of the transaction.
     */
    @Size(max = 100, message = "External reference cannot exceed 100 characters")
    private String externalReference;

    /**
     * The running balance after this transaction.
     */
    @NotNull(message = "Running balance is required")
    @ValidAmount
    private BigDecimal runningBalance;

    /**
     * The transaction category ID.
     */
    @FilterableId
    private UUID transactionCategoryId;

    /**
     * The transaction category name.
     */
    @Size(max = 100, message = "Transaction category name cannot exceed 100 characters")
    private String transactionCategoryName;
}
