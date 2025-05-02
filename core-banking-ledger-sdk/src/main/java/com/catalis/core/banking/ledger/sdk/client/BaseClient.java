package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Base client class providing common functionality for all API clients.
 * Handles common HTTP operations and provides utility methods for working with the WebClient.
 */
public abstract class BaseClient {

    protected final WebClient webClient;
    protected final String basePath;

    /**
     * Constructs a new BaseClient with the specified WebClient and base path.
     *
     * @param webClient The WebClient instance to use for API requests.
     * @param basePath  The base path for API endpoints.
     */
    protected BaseClient(WebClient webClient, String basePath) {
        this.webClient = webClient;
        this.basePath = basePath;
    }

    /**
     * Performs a GET request to the specified path.
     *
     * @param path          The path to send the request to.
     * @param responseType  The type of the response.
     * @param <T>           The type parameter for the response.
     * @return A Mono emitting the response.
     */
    protected <T> Mono<T> get(String path, Class<T> responseType) {
        return webClient.get()
                .uri(basePath + path)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a GET request to the specified path with a parameterized response type.
     *
     * @param path          The path to send the request to.
     * @param responseType  The parameterized type reference for the response.
     * @param <T>           The type parameter for the response.
     * @return A Mono emitting the response.
     */
    protected <T> Mono<T> get(String path, ParameterizedTypeReference<T> responseType) {
        return webClient.get()
                .uri(basePath + path)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a GET request to the specified path with query parameters.
     *
     * @param path          The path to send the request to.
     * @param queryParams   The query parameters to include in the request.
     * @param responseType  The type of the response.
     * @param <T>           The type parameter for the response.
     * @return A Mono emitting the response.
     */
    protected <T> Mono<T> get(String path, Map<String, Object> queryParams, Class<T> responseType) {
        String uri = buildUriWithQueryParams(path, queryParams);
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a GET request to the specified path with query parameters and a parameterized response type.
     *
     * @param path          The path to send the request to.
     * @param queryParams   The query parameters to include in the request.
     * @param responseType  The parameterized type reference for the response.
     * @param <T>           The type parameter for the response.
     * @return A Mono emitting the response.
     */
    protected <T> Mono<T> get(String path, Map<String, Object> queryParams, ParameterizedTypeReference<T> responseType) {
        String uri = buildUriWithQueryParams(path, queryParams);
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a POST request to the specified path with a request body.
     *
     * @param path          The path to send the request to.
     * @param requestBody   The request body to include in the request.
     * @param responseType  The type of the response.
     * @param <T>           The type parameter for the response.
     * @param <R>           The type parameter for the request body.
     * @return A Mono emitting the response.
     */
    protected <T, R> Mono<T> post(String path, R requestBody, Class<T> responseType) {
        return webClient.post()
                .uri(basePath + path)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a POST request to the specified path with a request body and a parameterized response type.
     *
     * @param path          The path to send the request to.
     * @param requestBody   The request body to include in the request.
     * @param responseType  The parameterized type reference for the response.
     * @param <T>           The type parameter for the response.
     * @param <R>           The type parameter for the request body.
     * @return A Mono emitting the response.
     */
    protected <T, R> Mono<T> post(String path, R requestBody, ParameterizedTypeReference<T> responseType) {
        return webClient.post()
                .uri(basePath + path)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a POST request to the specified path with a request body and query parameters.
     *
     * @param path          The path to send the request to.
     * @param requestBody   The request body to include in the request.
     * @param queryParams   The query parameters to include in the request.
     * @param responseType  The type of the response.
     * @param <T>           The type parameter for the response.
     * @param <R>           The type parameter for the request body.
     * @return A Mono emitting the response.
     */
    protected <T, R> Mono<T> post(String path, R requestBody, Map<String, Object> queryParams, Class<T> responseType) {
        String uri = buildUriWithQueryParams(path, queryParams);
        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a PUT request to the specified path with a request body.
     *
     * @param path          The path to send the request to.
     * @param requestBody   The request body to include in the request.
     * @param responseType  The type of the response.
     * @param <T>           The type parameter for the response.
     * @param <R>           The type parameter for the request body.
     * @return A Mono emitting the response.
     */
    protected <T, R> Mono<T> put(String path, R requestBody, Class<T> responseType) {
        return webClient.put()
                .uri(basePath + path)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a DELETE request to the specified path.
     *
     * @param path          The path to send the request to.
     * @param responseType  The type of the response.
     * @param <T>           The type parameter for the response.
     * @return A Mono emitting the response.
     */
    protected <T> Mono<T> delete(String path, Class<T> responseType) {
        return webClient.delete()
                .uri(basePath + path)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a GET request to retrieve a paginated list of resources.
     *
     * @param path              The path to send the request to.
     * @param paginationRequest The pagination request parameters.
     * @param responseType      The parameterized type reference for the response.
     * @param <T>               The type parameter for the response items.
     * @return A Mono emitting the paginated response.
     */
    protected <T> Mono<PaginationResponse<T>> getPaginated(String path, PaginationRequest paginationRequest, 
                                                          ParameterizedTypeReference<PaginationResponse<T>> responseType) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", String.valueOf(paginationRequest.getPage()));
        queryParams.add("size", String.valueOf(paginationRequest.getSize()));
        
        if (paginationRequest.getSortBy() != null) {
            queryParams.add("sortBy", paginationRequest.getSortBy());
        }
        
        if (paginationRequest.getSortDirection() != null) {
            queryParams.add("sortDirection", paginationRequest.getSortDirection().name());
        }
        
        String uri = UriComponentsBuilder.fromPath(basePath + path)
                .queryParams(queryParams)
                .build()
                .toUriString();
        
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a GET request to filter resources.
     *
     * @param path          The path to send the request to.
     * @param filterRequest The filter request parameters.
     * @param responseType  The parameterized type reference for the response.
     * @param <T>           The type parameter for the response items.
     * @return A Mono emitting the filtered response.
     */
    protected <T> Mono<PaginationResponse<T>> getFiltered(String path, FilterRequest<T> filterRequest, 
                                                         ParameterizedTypeReference<PaginationResponse<T>> responseType) {
        // Convert the filter request to query parameters
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        
        // Add pagination parameters
        queryParams.add("page", String.valueOf(filterRequest.getPage()));
        queryParams.add("size", String.valueOf(filterRequest.getSize()));
        
        if (filterRequest.getSortBy() != null) {
            queryParams.add("sortBy", filterRequest.getSortBy());
        }
        
        if (filterRequest.getSortDirection() != null) {
            queryParams.add("sortDirection", filterRequest.getSortDirection().name());
        }
        
        // Add filter parameters if available
        if (filterRequest.getFilters() != null) {
            filterRequest.getFilters().forEach((key, value) -> {
                if (value != null) {
                    queryParams.add("filter_" + key, value.toString());
                }
            });
        }
        
        String uri = UriComponentsBuilder.fromPath(basePath + path)
                .queryParams(queryParams)
                .build()
                .toUriString();
        
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Builds a URI with query parameters.
     *
     * @param path        The base path.
     * @param queryParams The query parameters to include.
     * @return The URI string with query parameters.
     */
    private String buildUriWithQueryParams(String path, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(basePath + path);
        
        if (queryParams != null) {
            queryParams.forEach((key, value) -> {
                if (value != null) {
                    builder.queryParam(key, value);
                }
            });
        }
        
        return builder.build().toUriString();
    }
}
