package com.catalis.core.banking.ledger.interfaces.dtos.core.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
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
    private Long accountId;
    private Long transactionCategoryId;
}
