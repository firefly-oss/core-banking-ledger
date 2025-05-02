package com.catalis.common.core.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response object for paginated data.
 *
 * @param <T> The type of the items in the page.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> {

    /**
     * The items in the current page.
     */
    private List<T> content;

    /**
     * The total number of items.
     */
    private long totalElements;

    /**
     * The total number of pages.
     */
    private int totalPages;

    /**
     * The current page number (0-based).
     */
    private int page;

    /**
     * The page size.
     */
    private int size;

    /**
     * Whether the current page is the first page.
     */
    private boolean first;

    /**
     * Whether the current page is the last page.
     */
    private boolean last;

    /**
     * Whether the current page is empty.
     */
    private boolean empty;
}
