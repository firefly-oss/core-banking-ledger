package com.catalis.core.banking.ledger.core.services.ledger.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerAccountDTO;
import reactor.core.publisher.Mono;

public interface LedgerAccountService {

    /**
     * Retrieve a paginated list of ledger accounts.
     */
    Mono<PaginationResponse<LedgerAccountDTO>> listLedgerAccounts(PaginationRequest paginationRequest);

    /**
     * Filter ledger accounts using a FilterRequest.
     */
    Mono<PaginationResponse<LedgerAccountDTO>> filterLedgerAccounts(FilterRequest<LedgerAccountDTO> filterRequest);

    /**
     * Create a new ledger account record.
     */
    Mono<LedgerAccountDTO> createLedgerAccount(LedgerAccountDTO dto);

    /**
     * Retrieve a specific ledger account by its ID.
     */
    Mono<LedgerAccountDTO> getLedgerAccount(Long ledgerAccountId);

    /**
     * Update an existing ledger account by its ID.
     */
    Mono<LedgerAccountDTO> updateLedgerAccount(Long ledgerAccountId, LedgerAccountDTO dto);

    /**
     * Delete a ledger account by its ID.
     */
    Mono<Void> deleteLedgerAccount(Long ledgerAccountId);
}
