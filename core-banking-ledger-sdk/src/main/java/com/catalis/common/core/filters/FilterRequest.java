package com.catalis.common.core.filters;

import com.catalis.common.core.queries.PaginationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request for filtering data with pagination.
 * <p>
 * This class is used to specify filter criteria along with pagination parameters.
 *
 * @param <T> The type of the filter criteria.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest<T> {

    /**
     * The filter criteria.
     */
    private T filter;

    /**
     * The pagination parameters.
     */
    private PaginationRequest pagination;

    /**
     * Creates a new FilterRequest with the specified filter criteria and pagination parameters.
     *
     * @param filter     The filter criteria.
     * @param pagination The pagination parameters.
     * @param <T>        The type of the filter criteria.
     * @return A new FilterRequest instance.
     */
    public static <T> FilterRequest<T> of(T filter, PaginationRequest pagination) {
        return new FilterRequest<>(filter, pagination);
    }
}