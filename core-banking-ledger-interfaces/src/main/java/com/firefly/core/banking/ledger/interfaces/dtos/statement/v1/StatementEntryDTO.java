package com.firefly.core.banking.ledger.interfaces.dtos.statement.v1;

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
    private Long transactionId;
    
    /**
     * The date of the transaction.
     */
    private LocalDateTime transactionDate;
    
    /**
     * The value date of the transaction.
     */
    private LocalDateTime valueDate;
    
    /**
     * The booking date of the transaction.
     */
    private LocalDateTime bookingDate;
    
    /**
     * The type of the transaction.
     */
    private TransactionTypeEnum transactionType;
    
    /**
     * The status of the transaction.
     */
    private TransactionStatusEnum transactionStatus;
    
    /**
     * The amount of the transaction.
     */
    private BigDecimal amount;
    
    /**
     * The currency of the transaction.
     */
    private String currency;
    
    /**
     * The description of the transaction.
     */
    private String description;
    
    /**
     * The initiating party of the transaction.
     */
    private String initiatingParty;
    
    /**
     * The external reference of the transaction.
     */
    private String externalReference;
    
    /**
     * The running balance after this transaction.
     */
    private BigDecimal runningBalance;
    
    /**
     * The transaction category ID.
     */
    @FilterableId
    private Long transactionCategoryId;
    
    /**
     * The transaction category name.
     */
    private String transactionCategoryName;
}
