package com.catalis.common.core.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a paginated response.
 * <p>
 * This class is used to return paginated data along with pagination metadata
 * such as page number, page size, and total elements.
 *
 * @param <T> The type of elements in the content list.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> {

    /**
     * The content of the page.
     */
    private List<T> content;

    /**
     * The page number (0-based).
     */
    private int page;

    /**
     * The page size.
     */
    private int size;

    /**
     * The total number of elements.
     */
    private long totalElements;

    /**
     * Gets the total number of pages.
     *
     * @return The total number of pages.
     */
    public int getTotalPages() {
        return size == 0 ? 0 : (int) Math.ceil((double) totalElements / (double) size);
    }

    /**
     * Checks if this is the first page.
     *
     * @return true if this is the first page, false otherwise.
     */
    public boolean isFirst() {
        return page == 0;
    }

    /**
     * Checks if this is the last page.
     *
     * @return true if this is the last page, false otherwise.
     */
    public boolean isLast() {
        return page >= getTotalPages() - 1;
    }

    /**
     * Checks if there is a next page.
     *
     * @return true if there is a next page, false otherwise.
     */
    public boolean hasNext() {
        return !isLast();
    }

    /**
     * Checks if there is a previous page.
     *
     * @return true if there is a previous page, false otherwise.
     */
    public boolean hasPrevious() {
        return !isFirst();
    }
}