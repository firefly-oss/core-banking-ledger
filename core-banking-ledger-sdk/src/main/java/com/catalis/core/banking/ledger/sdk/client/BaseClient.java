package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

/**
 * Base abstract class for all API clients in the Core Banking Ledger SDK.
 * <p>
 * This class provides common functionality for making HTTP requests to the Core Banking Ledger API,
 * including methods for GET, POST, PUT, and DELETE operations with support for query parameters,
 * request bodies, and parameterized response types.
 */
public abstract class BaseClient {

    /**
     * The WebClient instance used for making HTTP requests.
     */
    @Getter
    private final WebClient webClient;

    /**
     * The base path for API endpoints handled by this client.
     */
    @Getter
    private final String basePath;

    /**
     * Constructs a new BaseClient with the specified WebClient and base path.
     *
     * @param webClient The WebClient instance to use for making HTTP requests.
     * @param basePath  The base path for API endpoints handled by this client.
     */
    protected BaseClient(WebClient webClient, String basePath) {
        this.webClient = webClient;
        this.basePath = basePath;
    }

    /**
     * Performs a GET request to retrieve a resource.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param responseType  The class of the response type.
     * @param <T>           The type of the response.
     * @return A Mono that emits the response body.
     */
    protected <T> Mono<T> get(String path, Class<T> responseType) {
        return get(path, Collections.emptyMap(), responseType);
    }

    /**
     * Performs a GET request to retrieve a resource with query parameters.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param queryParams   The query parameters to include in the request.
     * @param responseType  The class of the response type.
     * @param <T>           The type of the response.
     * @return A Mono that emits the response body.
     */
    protected <T> Mono<T> get(String path, Map<String, String> queryParams, Class<T> responseType) {
        String uri = buildUri(path, queryParams);
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a GET request to retrieve a resource with a parameterized response type.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param responseType  The ParameterizedTypeReference for the response type.
     * @param <T>           The type of the response.
     * @return A Mono that emits the response body.
     */
    protected <T> Mono<T> get(String path, ParameterizedTypeReference<T> responseType) {
        return get(path, Collections.emptyMap(), responseType);
    }

    /**
     * Performs a GET request to retrieve a resource with query parameters and a parameterized response type.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param queryParams   The query parameters to include in the request.
     * @param responseType  The ParameterizedTypeReference for the response type.
     * @param <T>           The type of the response.
     * @return A Mono that emits the response body.
     */
    protected <T> Mono<T> get(String path, Map<String, String> queryParams, ParameterizedTypeReference<T> responseType) {
        String uri = buildUri(path, queryParams);
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a POST request to create a resource.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param body          The request body.
     * @param responseType  The class of the response type.
     * @param <T>           The type of the response.
     * @param <R>           The type of the request body.
     * @return A Mono that emits the response body.
     */
    protected <T, R> Mono<T> post(String path, R body, Class<T> responseType) {
        return post(path, Collections.emptyMap(), body, responseType);
    }

    /**
     * Performs a POST request to create a resource with query parameters.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param queryParams   The query parameters to include in the request.
     * @param body          The request body.
     * @param responseType  The class of the response type.
     * @param <T>           The type of the response.
     * @param <R>           The type of the request body.
     * @return A Mono that emits the response body.
     */
    protected <T, R> Mono<T> post(String path, Map<String, String> queryParams, R body, Class<T> responseType) {
        String uri = buildUri(path, queryParams);
        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a POST request to create a resource with a parameterized response type.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param body          The request body.
     * @param responseType  The ParameterizedTypeReference for the response type.
     * @param <T>           The type of the response.
     * @param <R>           The type of the request body.
     * @return A Mono that emits the response body.
     */
    protected <T, R> Mono<T> post(String path, R body, ParameterizedTypeReference<T> responseType) {
        return post(path, Collections.emptyMap(), body, responseType);
    }

    /**
     * Performs a POST request to create a resource with query parameters and a parameterized response type.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param queryParams   The query parameters to include in the request.
     * @param body          The request body.
     * @param responseType  The ParameterizedTypeReference for the response type.
     * @param <T>           The type of the response.
     * @param <R>           The type of the request body.
     * @return A Mono that emits the response body.
     */
    protected <T, R> Mono<T> post(String path, Map<String, String> queryParams, R body, ParameterizedTypeReference<T> responseType) {
        String uri = buildUri(path, queryParams);
        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a PUT request to update a resource.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param body          The request body.
     * @param responseType  The class of the response type.
     * @param <T>           The type of the response.
     * @param <R>           The type of the request body.
     * @return A Mono that emits the response body.
     */
    protected <T, R> Mono<T> put(String path, R body, Class<T> responseType) {
        return put(path, Collections.emptyMap(), body, responseType);
    }

    /**
     * Performs a PUT request to update a resource with query parameters.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param queryParams   The query parameters to include in the request.
     * @param body          The request body.
     * @param responseType  The class of the response type.
     * @param <T>           The type of the response.
     * @param <R>           The type of the request body.
     * @return A Mono that emits the response body.
     */
    protected <T, R> Mono<T> put(String path, Map<String, String> queryParams, R body, Class<T> responseType) {
        String uri = buildUri(path, queryParams);
        return webClient.put()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a PATCH request to partially update a resource.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param queryParams   The query parameters to include in the request.
     * @param responseType  The class of the response type.
     * @param <T>           The type of the response.
     * @return A Mono that emits the response body.
     */
    protected <T> Mono<T> patch(String path, Map<String, String> queryParams, Class<T> responseType) {
        String uri = buildUri(path, queryParams);
        return webClient.patch()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a DELETE request to delete a resource.
     *
     * @param path The path to the resource, relative to the base path.
     * @return A Mono that completes when the request is complete.
     */
    protected Mono<Void> delete(String path) {
        return delete(path, Collections.emptyMap());
    }

    /**
     * Performs a DELETE request to delete a resource with query parameters.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param queryParams   The query parameters to include in the request.
     * @return A Mono that completes when the request is complete.
     */
    protected Mono<Void> delete(String path, Map<String, String> queryParams) {
        String uri = buildUri(path, queryParams);
        return webClient.delete()
                .uri(uri)
                .retrieve()
                .bodyToMono(Void.class);
    }

    /**
     * Performs a GET request with pagination parameters.
     *
     * @param path              The path to the resource, relative to the base path.
     * @param paginationRequest The pagination parameters.
     * @param responseType      The ParameterizedTypeReference for the response type.
     * @param <T>               The type of the response items.
     * @return A Mono that emits the paginated response.
     */
    protected <T> Mono<PaginationResponse<T>> getPaginated(String path, PaginationRequest paginationRequest, 
                                                          ParameterizedTypeReference<PaginationResponse<T>> responseType) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (paginationRequest != null) {
            if (paginationRequest.getPage() != null) {
                queryParams.add("page", paginationRequest.getPage().toString());
            }
            if (paginationRequest.getSize() != null) {
                queryParams.add("size", paginationRequest.getSize().toString());
            }
            if (paginationRequest.getSort() != null && !paginationRequest.getSort().isEmpty()) {
                queryParams.add("sort", paginationRequest.getSort());
            }
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
     * Performs a POST request with a filter request body for filtered and paginated results.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param filterRequest The filter request containing filter criteria and pagination parameters.
     * @param responseType  The ParameterizedTypeReference for the response type.
     * @param <T>           The type of the response items.
     * @param <F>           The type of the filter criteria.
     * @return A Mono that emits the filtered and paginated response.
     */
    protected <T, F> Mono<PaginationResponse<T>> postFiltered(String path, FilterRequest<F> filterRequest,
                                                             ParameterizedTypeReference<PaginationResponse<T>> responseType) {
        return webClient.post()
                .uri(basePath + path)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(filterRequest)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Builds a URI from a path and query parameters.
     *
     * @param path          The path to the resource, relative to the base path.
     * @param queryParams   The query parameters to include in the URI.
     * @return The complete URI string.
     */
    private String buildUri(String path, Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(basePath + path);
        queryParams.forEach(builder::queryParam);
        return builder.build().toUriString();
    }
}