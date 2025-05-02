package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.category.v1.TransactionCategoryDTO;
import com.catalis.core.banking.ledger.interfaces.enums.category.v1.CategoryTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TransactionCategoryClient class.
 */
class TransactionCategoryClientTest {

    private WebClient webClientMock;
    private TransactionCategoryClient client;
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;
    private WebClient.RequestBodyUriSpec requestBodyUriSpecMock;
    private WebClient.RequestBodySpec requestBodySpecMock;
    private WebClient.ResponseSpec responseSpecMock;

    private TransactionCategoryDTO categoryDTO;
    private final Long categoryId = 1L;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        webClientMock = mock(WebClient.class);
        requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        requestBodySpecMock = mock(WebClient.RequestBodySpec.class);
        responseSpecMock = mock(WebClient.ResponseSpec.class);

        // Create client with mocked WebClient
        client = new TransactionCategoryClient(webClientMock);

        // Initialize test data
        categoryDTO = new TransactionCategoryDTO();
        categoryDTO.setTransactionCategoryId(categoryId);
        categoryDTO.setCategoryName("Groceries");
        categoryDTO.setCategoryDescription("Food and household items");
        categoryDTO.setCategoryType(CategoryTypeEnum.EXPENSE);
        categoryDTO.setParentCategoryId(null);
        categoryDTO.setSpanishTaxCode("TAX123");
    }

    @Test
    void listCategories_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();
        paginationRequest.setPage(0);
        paginationRequest.setSize(10);
        PaginationResponse<TransactionCategoryDTO> paginationResponse = new PaginationResponse<>();

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(paginationResponse));

        // Act & Assert
        StepVerifier.create(client.listCategories(paginationRequest))
                .expectNext(paginationResponse)
                .verifyComplete();

        // Verify that the WebClient was called with the correct URI
        // Note: We can't verify the exact URI since it's built with UriComponentsBuilder
        // and includes query parameters for pagination
        verify(requestHeadersUriSpecMock).uri(contains("/api/v1/transaction-categories"));
    }

    @Test
    void createCategory_Success() {
        // Arrange
        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.body(any())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(TransactionCategoryDTO.class)).thenReturn(Mono.just(categoryDTO));

        // Act & Assert
        StepVerifier.create(client.createCategory(categoryDTO))
                .expectNext(categoryDTO)
                .verifyComplete();

        // Verify that the WebClient was called with the correct URI
        verify(requestBodyUriSpecMock).uri("/api/v1/transaction-categories");
    }

    @Test
    void getCategory_Success() {
        // Arrange
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(TransactionCategoryDTO.class)).thenReturn(Mono.just(categoryDTO));

        // Act & Assert
        StepVerifier.create(client.getCategory(categoryId))
                .expectNext(categoryDTO)
                .verifyComplete();

        // Verify that the WebClient was called with the correct URI
        ArgumentCaptor<String> uriCaptor = ArgumentCaptor.forClass(String.class);
        verify(requestHeadersUriSpecMock).uri(uriCaptor.capture());
        assertEquals("/api/v1/transaction-categories/" + categoryId, uriCaptor.getValue());
    }

    @Test
    void updateCategory_Success() {
        // Arrange
        when(webClientMock.put()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.body(any())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(TransactionCategoryDTO.class)).thenReturn(Mono.just(categoryDTO));

        // Act & Assert
        StepVerifier.create(client.updateCategory(categoryId, categoryDTO))
                .expectNext(categoryDTO)
                .verifyComplete();

        // Verify that the WebClient was called with the correct URI
        ArgumentCaptor<String> uriCaptor = ArgumentCaptor.forClass(String.class);
        verify(requestBodyUriSpecMock).uri(uriCaptor.capture());
        assertEquals("/api/v1/transaction-categories/" + categoryId, uriCaptor.getValue());
    }

    @Test
    void deleteCategory_Success() {
        // Arrange
        when(webClientMock.delete()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.accept(any(MediaType.class))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(client.deleteCategory(categoryId))
                .verifyComplete();

        // Verify that the WebClient was called with the correct URI
        ArgumentCaptor<String> uriCaptor = ArgumentCaptor.forClass(String.class);
        verify(requestHeadersUriSpecMock).uri(uriCaptor.capture());
        assertEquals("/api/v1/transaction-categories/" + categoryId, uriCaptor.getValue());
    }
}
