package com.catalis.common.core.filters;

import com.catalis.common.core.queries.PaginationRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Request object for filtering data with pagination.
 *
 * @param <T> The type of the entity to filter.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FilterRequest<T> extends PaginationRequest {

    /**
     * The filters to apply.
     */
    private Map<String, Object> filters = new HashMap<>();

    /**
     * Creates a new FilterRequest with the specified pagination parameters.
     *
     * @param page           The page number.
     * @param size           The page size.
     * @param sortBy         The field to sort by.
     * @param sortDirection  The sort direction.
     * @param filters        The filters to apply.
     */
    public FilterRequest(int page, int size, String sortBy, SortDirection sortDirection, Map<String, Object> filters) {
        super(page, size, sortBy, sortDirection);
        this.filters = filters != null ? filters : new HashMap<>();
    }

    /**
     * Creates a new FilterRequest with default pagination parameters.
     *
     * @param filters The filters to apply.
     */
    public FilterRequest(Map<String, Object> filters) {
        super();
        this.filters = filters != null ? filters : new HashMap<>();
    }

    /**
     * Adds a filter.
     *
     * @param key   The filter key.
     * @param value The filter value.
     * @return This FilterRequest instance for method chaining.
     */
    public FilterRequest<T> addFilter(String key, Object value) {
        if (filters == null) {
            filters = new HashMap<>();
        }
        filters.put(key, value);
        return this;
    }

    /**
     * Creates a new FilterRequest builder.
     *
     * @param <T> The type of the entity to filter.
     * @return A new FilterRequestBuilder instance.
     */
    public static <T> FilterRequestBuilder<T> builder() {
        return new FilterRequestBuilder<>();
    }

    /**
     * Builder for FilterRequest.
     *
     * @param <T> The type of the entity to filter.
     */
    public static class FilterRequestBuilder<T> {
        private int page = 0;
        private int size = 10;
        private String sortBy;
        private SortDirection sortDirection;
        private Map<String, Object> filters = new HashMap<>();

        /**
         * Sets the page number.
         *
         * @param page The page number.
         * @return This builder instance.
         */
        public FilterRequestBuilder<T> page(int page) {
            this.page = page;
            return this;
        }

        /**
         * Sets the page size.
         *
         * @param size The page size.
         * @return This builder instance.
         */
        public FilterRequestBuilder<T> size(int size) {
            this.size = size;
            return this;
        }

        /**
         * Sets the field to sort by.
         *
         * @param sortBy The field to sort by.
         * @return This builder instance.
         */
        public FilterRequestBuilder<T> sortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        /**
         * Sets the sort direction.
         *
         * @param sortDirection The sort direction.
         * @return This builder instance.
         */
        public FilterRequestBuilder<T> sortDirection(SortDirection sortDirection) {
            this.sortDirection = sortDirection;
            return this;
        }

        /**
         * Sets the filters.
         *
         * @param filters The filters.
         * @return This builder instance.
         */
        public FilterRequestBuilder<T> filters(Map<String, Object> filters) {
            this.filters = filters;
            return this;
        }

        /**
         * Adds a filter.
         *
         * @param key   The filter key.
         * @param value The filter value.
         * @return This builder instance.
         */
        public FilterRequestBuilder<T> filter(String key, Object value) {
            if (this.filters == null) {
                this.filters = new HashMap<>();
            }
            this.filters.put(key, value);
            return this;
        }

        /**
         * Builds a new FilterRequest instance.
         *
         * @return A new FilterRequest instance.
         */
        public FilterRequest<T> build() {
            return new FilterRequest<>(page, size, sortBy, sortDirection, filters);
        }
    }
}
