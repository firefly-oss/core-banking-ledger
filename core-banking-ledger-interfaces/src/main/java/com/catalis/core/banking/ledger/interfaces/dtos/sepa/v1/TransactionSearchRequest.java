package com.catalis.core.banking.ledger.interfaces.dtos.sepa.v1;

import com.catalis.common.core.queries.PaginationRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionSearchRequest {

    @Valid
    private TransactionSepaFilterDTO transactionFilter;

    @Valid
    private PaginationRequest pagination;

}
