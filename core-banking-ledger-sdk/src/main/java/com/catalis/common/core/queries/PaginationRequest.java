package com.catalis.common.core.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for pagination.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

    /**
     * The page number (0-based).
     */
    private int page = 0;

    /**
     * The page size.
     */
    private int size = 10;

    /**
     * The field to sort by.
     */
    private String sortBy;

    /**
     * The sort direction.
     */
    private SortDirection sortDirection;

    /**
     * Enumeration for sort directions.
     */
    public enum SortDirection {
        ASC,
        DESC
    }
}
