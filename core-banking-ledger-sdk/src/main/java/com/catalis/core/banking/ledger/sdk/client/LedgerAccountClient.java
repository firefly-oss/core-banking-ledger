package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.ledger.v1.LedgerAccountDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for interacting with the Ledger Account API endpoints.
 */
public class LedgerAccountClient extends BaseClient {

    private static final String BASE_PATH = "/api/v1/ledger-accounts";

    /**
     * Constructs a new LedgerAccountClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for API requests.
     */
    public LedgerAccountClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    /**
     * Retrieves a ledger account by its ID.
     *
     * @param ledgerAccountId The ID of the ledger account to retrieve.
     * @return A Mono emitting the ledger account.
     */
    public Mono<LedgerAccountDTO> getLedgerAccount(Long ledgerAccountId) {
        return get("/" + ledgerAccountId, LedgerAccountDTO.class);
    }

    /**
     * Creates a new ledger account.
     *
     * @param ledgerAccount The ledger account to create.
     * @return A Mono emitting the created ledger account.
     */
    public Mono<LedgerAccountDTO> createLedgerAccount(LedgerAccountDTO ledgerAccount) {
        return post("", ledgerAccount, LedgerAccountDTO.class);
    }

    /**
     * Updates an existing ledger account.
     *
     * @param ledgerAccountId The ID of the ledger account to update.
     * @param ledgerAccount   The updated ledger account data.
     * @return A Mono emitting the updated ledger account.
     */
    public Mono<LedgerAccountDTO> updateLedgerAccount(Long ledgerAccountId, LedgerAccountDTO ledgerAccount) {
        return put("/" + ledgerAccountId, ledgerAccount, LedgerAccountDTO.class);
    }

    /**
     * Retrieves a paginated list of ledger accounts.
     *
     * @param paginationRequest The pagination parameters.
     * @return A Mono emitting a paginated response of ledger accounts.
     */
    public Mono<PaginationResponse<LedgerAccountDTO>> listLedgerAccounts(PaginationRequest paginationRequest) {
        return getPaginated("", paginationRequest, new ParameterizedTypeReference<PaginationResponse<LedgerAccountDTO>>() {});
    }

    /**
     * Filters ledger accounts based on the provided criteria.
     *
     * @param filterRequest The filter criteria.
     * @return A Mono emitting a paginated response of filtered ledger accounts.
     */
    public Mono<PaginationResponse<LedgerAccountDTO>> filterLedgerAccounts(FilterRequest<LedgerAccountDTO> filterRequest) {
        return getFiltered("/filter", filterRequest, new ParameterizedTypeReference<PaginationResponse<LedgerAccountDTO>>() {});
    }
}
