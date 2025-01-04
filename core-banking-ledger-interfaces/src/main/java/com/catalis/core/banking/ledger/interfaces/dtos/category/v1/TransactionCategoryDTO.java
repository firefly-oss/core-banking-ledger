package com.catalis.core.banking.ledger.interfaces.dtos.category.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.ledger.interfaces.enums.category.v1.CategoryTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TransactionCategoryDTO extends BaseDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long transactionCategoryId;

    private Long parentCategoryId;
    private String categoryName;
    private String categoryDescription;
    private CategoryTypeEnum categoryType;
}
