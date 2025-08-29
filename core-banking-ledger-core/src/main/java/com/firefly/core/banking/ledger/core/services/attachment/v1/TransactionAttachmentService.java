package com.firefly.core.banking.ledger.core.services.attachment.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.ledger.interfaces.dtos.attachment.v1.TransactionAttachmentDTO;
import com.firefly.core.banking.ledger.interfaces.enums.attachment.v1.AttachmentTypeEnum;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing transaction attachments.
 */
public interface TransactionAttachmentService {
    /**
     * Create a new transaction attachment.
     *
     * @param transactionId The ID of the transaction to which the attachment belongs.
     * @param attachmentDTO The attachment data.
     * @return The created attachment.
     */
    Mono<TransactionAttachmentDTO> createAttachment(Long transactionId, TransactionAttachmentDTO attachmentDTO);

    /**
     * Get a specific transaction attachment by ID.
     *
     * @param transactionId The ID of the transaction to which the attachment belongs.
     * @param attachmentId The ID of the attachment.
     * @return The attachment.
     */
    Mono<TransactionAttachmentDTO> getAttachment(Long transactionId, Long attachmentId);

    /**
     * List all attachments for a specific transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of attachments.
     */
    Mono<PaginationResponse<TransactionAttachmentDTO>> listAttachments(Long transactionId, PaginationRequest paginationRequest);

    /**
     * List all attachments of a specific type for a transaction.
     *
     * @param transactionId The ID of the transaction.
     * @param attachmentType The type of attachment.
     * @param paginationRequest Pagination parameters.
     * @return A paginated list of attachments.
     */
    Mono<PaginationResponse<TransactionAttachmentDTO>> listAttachmentsByType(
            Long transactionId,
            AttachmentTypeEnum attachmentType,
            PaginationRequest paginationRequest
    );
}
