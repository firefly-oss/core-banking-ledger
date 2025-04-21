package com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1;

import com.catalis.core.banking.ledger.interfaces.dtos.BaseDTO;
import com.catalis.core.banking.ledger.interfaces.enums.ledger.v1.LedgerAccountTypeEnum;
import com.catalis.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LedgerAccountDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long ledgerAccountId;

    private String accountCode;
    private String accountName;
    private LedgerAccountTypeEnum accountType;

    @FilterableId
    private Long parentAccountId;

    private Boolean isActive;
}
