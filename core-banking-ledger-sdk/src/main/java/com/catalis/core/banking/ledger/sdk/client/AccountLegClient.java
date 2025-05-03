package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.leg.v1.TransactionLegDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Client for interacting with the Account Leg API endpoints.
 * <p>
 * This client provides methods for retrieving transaction legs associated with a specific account.
 */
public class AccountLegClient extends BaseClient {

    /**
     * The base path format for account leg API endpoints.
     */
    private static final String BASE_PATH_FORMAT = "/api/v1/accounts/%s/legs";

    /**
     * Constructs a new AccountLegClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for making HTTP requests.
     */
    public AccountLegClient(WebClient webClient) {
        super(webClient, "");  // Base path will be set dynamically for each account
    }

    /**
     * Lists transaction legs for a specific account with pagination.
     *
     * @param accountId         The ID of the account.
     * @param paginationRequest The pagination parameters.
     * @return A Mono that emits a paginated response of transaction legs.
     */
    public Mono<PaginationResponse<TransactionLegDTO>> listAccountLegs(
            Long accountId, PaginationRequest paginationRequest) {
        
        String path = String.format(BASE_PATH_FORMAT, accountId);
        
        Map<String, String> queryParams = new HashMap<>();
        if (paginationRequest != null) {
            if (paginationRequest.getPage() != null) {
                queryParams.put("page", paginationRequest.getPage().toString());
            }
            if (paginationRequest.getSize() != null) {
                queryParams.put("size", paginationRequest.getSize().toString());
            }
            if (paginationRequest.getSort() != null && !paginationRequest.getSort().isEmpty()) {
                queryParams.put("sort", paginationRequest.getSort());
                queryParams.put("direction", paginationRequest.getDirection());
            }
        }
        
        return get(path, queryParams, new ParameterizedTypeReference<PaginationResponse<TransactionLegDTO>>() {});
    }

    /**
     * Lists transaction legs for a specific account within a date range with pagination.
     *
     * @param accountId         The ID of the account.
     * @param startDate         The start date of the range.
     * @param endDate           The end date of the range.
     * @param paginationRequest The pagination parameters.
     * @return A Mono that emits a paginated response of transaction legs.
     */
    public Mono<PaginationResponse<TransactionLegDTO>> listAccountLegsByDateRange(
            Long accountId, LocalDateTime startDate, LocalDateTime endDate, PaginationRequest paginationRequest) {
        
        String path = String.format(BASE_PATH_FORMAT, accountId) + "/date-range";
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("startDate", startDate.format(DateTimeFormatter.ISO_DATE_TIME));
        queryParams.put("endDate", endDate.format(DateTimeFormatter.ISO_DATE_TIME));
        
        if (paginationRequest != null) {
            if (paginationRequest.getPage() != null) {
                queryParams.put("page", paginationRequest.getPage().toString());
            }
            if (paginationRequest.getSize() != null) {
                queryParams.put("size", paginationRequest.getSize().toString());
            }
            if (paginationRequest.getSort() != null && !paginationRequest.getSort().isEmpty()) {
                queryParams.put("sort", paginationRequest.getSort());
                queryParams.put("direction", paginationRequest.getDirection());
            }
        }
        
        return get(path, queryParams, new ParameterizedTypeReference<PaginationResponse<TransactionLegDTO>>() {});
    }
}