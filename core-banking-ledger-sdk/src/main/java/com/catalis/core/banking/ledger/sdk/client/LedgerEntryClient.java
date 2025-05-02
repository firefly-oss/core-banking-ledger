package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerEntryDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Client for interacting with the Ledger Entry API endpoints.
 */
public class LedgerEntryClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/ledger-entries";

    /**
     * Constructs a new LedgerEntryClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public LedgerEntryClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves a ledger entry by its ID.
     *
     * @param ledgerEntryId The ID of the ledger entry to retrieve.
     * @return A Mono emitting the ledger entry.
     */
    public Mono<LedgerEntryDTO> getLedgerEntry(Long ledgerEntryId) {
        return get("/" + ledgerEntryId, LedgerEntryDTO.class);
    }

    /**
     * Creates a new ledger entry.
     *
     * @param ledgerEntry The ledger entry to create.
     * @return A Mono emitting the created ledger entry.
     */
    public Mono<LedgerEntryDTO> createLedgerEntry(LedgerEntryDTO ledgerEntry) {
        return post("", ledgerEntry, LedgerEntryDTO.class);
    }

    /**
     * Retrieves a paginated list of ledger entries.
     *
     * @param paginationRequest The pagination parameters.
     * @return A Mono emitting a paginated response of ledger entries.
     */
    public Mono<PaginationResponse<LedgerEntryDTO>> listLedgerEntries(PaginationRequest paginationRequest) {
        return getPaginated("", paginationRequest, new ParameterizedTypeReference<PaginationResponse<LedgerEntryDTO>>() {});
    }

    /**
     * Retrieves a paginated list of ledger entries filtered by transaction ID or ledger account ID.
     *
     * @param transactionId     The transaction ID to filter by (optional).
     * @param ledgerAccountId   The ledger account ID to filter by (optional).
     * @param paginationRequest The pagination parameters.
     * @return A Mono emitting a paginated response of filtered ledger entries.
     */
    public Mono<PaginationResponse<LedgerEntryDTO>> listLedgerEntries(Long transactionId, Long ledgerAccountId, PaginationRequest paginationRequest) {
        Map<String, Object> queryParams = new HashMap<>();
        
        if (transactionId != null) {
            queryParams.put("transactionId", transactionId);
        }
        
        if (ledgerAccountId != null) {
            queryParams.put("ledgerAccountId", ledgerAccountId);
        }
        
        // Add pagination parameters
        queryParams.put("page", paginationRequest.getPage());
        queryParams.put("size", paginationRequest.getSize());
        
        if (paginationRequest.getSortBy() != null) {
            queryParams.put("sortBy", paginationRequest.getSortBy());
        }
        
        if (paginationRequest.getSortDirection() != null) {
            queryParams.put("sortDirection", paginationRequest.getSortDirection().name());
        }
        
        return get("", queryParams, new ParameterizedTypeReference<PaginationResponse<LedgerEntryDTO>>() {});
    }
}
