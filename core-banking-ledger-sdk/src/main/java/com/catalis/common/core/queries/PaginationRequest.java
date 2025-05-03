package com.catalis.common.core.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request for pagination.
 * <p>
 * This class is used to specify pagination parameters such as page number, page size,
 * sort field, and sort direction.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

    /**
     * The page number (0-based).
     */
    private Integer page;

    /**
     * The page size.
     */
    private Integer size;

    /**
     * The field to sort by.
     */
    private String sort;

    /**
     * The sort direction (ASC or DESC).
     */
    private String direction;

    /**
     * Creates a new PaginationRequest with the specified parameters.
     *
     * @param page      The page number (0-based).
     * @param size      The page size.
     * @param sort      The field to sort by.
     * @param direction The sort direction (ASC or DESC).
     * @return A new PaginationRequest instance.
     */
    public static PaginationRequest of(int page, int size, String sort, String direction) {
        return new PaginationRequest(page, size, sort, direction);
    }

    /**
     * Creates a new PaginationRequest with the specified page and size, without sorting.
     *
     * @param page The page number (0-based).
     * @param size The page size.
     * @return A new PaginationRequest instance.
     */
    public static PaginationRequest of(int page, int size) {
        return new PaginationRequest(page, size, null, null);
    }

    /**
     * Gets the page number.
     *
     * @return The page number.
     */
    public Integer getPage() {
        return page;
    }

    /**
     * Gets the page size.
     *
     * @return The page size.
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Gets the sort field.
     *
     * @return The sort field.
     */
    public String getSort() {
        return sort;
    }

    /**
     * Gets the sort direction.
     *
     * @return The sort direction.
     */
    public String getDirection() {
        return direction;
    }
}