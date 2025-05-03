package com.catalis.core.banking.ledger.sdk.client;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.interfaces.dtos.attachment.v1.TransactionAttachmentDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Client for interacting with the Transaction Attachment API endpoints.
 * <p>
 * This client provides methods for creating, retrieving, and listing attachments
 * associated with a specific transaction.
 */
public class TransactionAttachmentClient extends BaseClient {

    /**
     * The base path format for transaction attachment API endpoints.
     */
    private static final String BASE_PATH_FORMAT = "/api/v1/transactions/%s/attachments";

    /**
     * Constructs a new TransactionAttachmentClient with the specified WebClient.
     *
     * @param webClient The WebClient instance to use for making HTTP requests.
     */
    public TransactionAttachmentClient(WebClient webClient) {
        super(webClient, "");  // Base path will be set dynamically for each transaction
    }

    /**
     * Creates a new attachment for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param attachmentDTO The attachment data to create.
     * @return A Mono that emits the created attachment.
     */
    public Mono<TransactionAttachmentDTO> createAttachment(Long transactionId, TransactionAttachmentDTO attachmentDTO) {
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        return post(path, attachmentDTO, TransactionAttachmentDTO.class);
    }

    /**
     * Retrieves a specific attachment by its ID.
     *
     * @param transactionId The ID of the transaction.
     * @param attachmentId  The ID of the attachment.
     * @return A Mono that emits the attachment.
     */
    public Mono<TransactionAttachmentDTO> getAttachment(Long transactionId, Long attachmentId) {
        String path = String.format(BASE_PATH_FORMAT, transactionId) + "/" + attachmentId;
        return get(path, TransactionAttachmentDTO.class);
    }

    /**
     * Lists attachments for a specific transaction with pagination.
     *
     * @param transactionId     The ID of the transaction.
     * @param paginationRequest The pagination parameters.
     * @return A Mono that emits a paginated response of attachments.
     */
    public Mono<PaginationResponse<TransactionAttachmentDTO>> listAttachments(
            Long transactionId, PaginationRequest paginationRequest) {
        
        String path = String.format(BASE_PATH_FORMAT, transactionId);
        
        Map<String, String> queryParams = new HashMap<>();
        if (paginationRequest != null) {
            if (paginationRequest.getPage() != null) {
                queryParams.put("page", paginationRequest.getPage().toString());
            }
            if (paginationRequest.getSize() != null) {
                queryParams.put("size", paginationRequest.getSize().toString());
            }
            if (paginationRequest.getSort() != null && !paginationRequest.getSort().isEmpty()) {
                queryParams.put("sort", paginationRequest.getSort());
                queryParams.put("direction", paginationRequest.getDirection());
            }
        }
        
        return get(path, queryParams, new ParameterizedTypeReference<PaginationResponse<TransactionAttachmentDTO>>() {});
    }

    /**
     * Lists attachments of a specific type for a transaction with pagination.
     *
     * @param transactionId     The ID of the transaction.
     * @param attachmentType    The type of attachments to retrieve.
     * @param paginationRequest The pagination parameters.
     * @return A Mono that emits a paginated response of attachments.
     */
    public Mono<PaginationResponse<TransactionAttachmentDTO>> listAttachmentsByType(
            Long transactionId, String attachmentType, PaginationRequest paginationRequest) {
        
        String path = String.format(BASE_PATH_FORMAT, transactionId) + "/type/" + attachmentType;
        
        Map<String, String> queryParams = new HashMap<>();
        if (paginationRequest != null) {
            if (paginationRequest.getPage() != null) {
                queryParams.put("page", paginationRequest.getPage().toString());
            }
            if (paginationRequest.getSize() != null) {
                queryParams.put("size", paginationRequest.getSize().toString());
            }
            if (paginationRequest.getSort() != null && !paginationRequest.getSort().isEmpty()) {
                queryParams.put("sort", paginationRequest.getSort());
                queryParams.put("direction", paginationRequest.getDirection());
            }
        }
        
        return get(path, queryParams, new ParameterizedTypeReference<PaginationResponse<TransactionAttachmentDTO>>() {});
    }
}