package com.catalis.core.banking.ledger.core.services.ledger.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerEntryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LedgerEntryService {

    /**
     * List all ledger entries that match the provided transactionId and ledgerAccountId.
     */
    Mono<PaginationResponse<LedgerEntryDTO>> listLedgerEntries(Long transactionId, Long ledgerAccountId, PaginationRequest paginationRequest);

    /**
     * Create a new ledger entry record.
     */
    Mono<LedgerEntryDTO> createLedgerEntry(LedgerEntryDTO dto);

    /**
     * Retrieve a specific ledger entry by its unique ID.
     */
    Mono<LedgerEntryDTO> getLedgerEntry(Long ledgerEntryId);

    /**
     * Update an existing ledger entry.
     */
    Mono<LedgerEntryDTO> updateLedgerEntry(Long ledgerEntryId, LedgerEntryDTO dto);

    /**
     * Delete a ledger entry by its unique ID.
     */
    Mono<Void> deleteLedgerEntry(Long ledgerEntryId);
}
