package com.catalis.core.banking.ledger.core.services.core.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import reactor.core.publisher.Mono;

public interface TransactionService {

    /**
     * Create a new transaction record.
     */
    Mono<TransactionDTO> createTransaction(TransactionDTO transactionDTO);

    /**
     * Filter transactions based on various criteria in FilterRequest.
     */
    Mono<PaginationResponse<TransactionDTO>> filterTransactions(FilterRequest<TransactionDTO> filterRequest);

    /**
     * Find transactions by account space ID with pagination.
     * 
     * @param accountSpaceId The ID of the account space
     * @param paginationRequest The pagination parameters
     * @return A paginated list of transactions for the specified account space
     */
    Mono<PaginationResponse<TransactionDTO>> findTransactionsByAccountSpaceId(Long accountSpaceId, PaginationRequest paginationRequest);

    /**
     * Retrieve a specific transaction by its unique ID.
     */
    Mono<TransactionDTO> getTransaction(Long transactionId);

    /**
     * Update an existing transaction by its unique ID.
     */
    Mono<TransactionDTO> updateTransaction(Long transactionId, TransactionDTO transactionDTO);

    /**
     * Delete a transaction by its unique ID.
     */
    Mono<Void> deleteTransaction(Long transactionId);

    /**
     * Find transactions by country with pagination.
     * 
     * @param country The country to search for
     * @param paginationRequest The pagination parameters
     * @return A paginated list of transactions for the specified country
     */
    Mono<PaginationResponse<TransactionDTO>> findTransactionsByCountry(String country, PaginationRequest paginationRequest);

    /**
     * Find transactions by city with pagination.
     * 
     * @param city The city to search for
     * @param paginationRequest The pagination parameters
     * @return A paginated list of transactions for the specified city
     */
    Mono<PaginationResponse<TransactionDTO>> findTransactionsByCity(String city, PaginationRequest paginationRequest);

    /**
     * Find transactions by postal code with pagination.
     * 
     * @param postalCode The postal code to search for
     * @param paginationRequest The pagination parameters
     * @return A paginated list of transactions for the specified postal code
     */
    Mono<PaginationResponse<TransactionDTO>> findTransactionsByPostalCode(String postalCode, PaginationRequest paginationRequest);

    /**
     * Find transactions by location name with pagination.
     * 
     * @param locationName The location name to search for (partial, case-insensitive)
     * @param paginationRequest The pagination parameters
     * @return A paginated list of transactions for the specified location name
     */
    Mono<PaginationResponse<TransactionDTO>> findTransactionsByLocationName(String locationName, PaginationRequest paginationRequest);

    /**
     * Find transactions within a specified radius of a point with pagination.
     * 
     * @param latitude The latitude of the center point
     * @param longitude The longitude of the center point
     * @param radiusInKm The radius in kilometers
     * @param paginationRequest The pagination parameters
     * @return A paginated list of transactions within the specified radius
     */
    Mono<PaginationResponse<TransactionDTO>> findTransactionsWithinRadius(Double latitude, Double longitude, Double radiusInKm, PaginationRequest paginationRequest);
}
