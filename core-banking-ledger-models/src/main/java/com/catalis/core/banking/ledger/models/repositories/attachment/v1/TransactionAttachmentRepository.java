package com.catalis.core.banking.ledger.models.repositories.attachment.v1;

import com.catalis.core.banking.ledger.interfaces.enums.attachment.v1.AttachmentTypeEnum;
import com.catalis.core.banking.ledger.models.entities.attachment.v1.TransactionAttachment;
import com.catalis.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository interface for transaction attachments.
 */
public interface TransactionAttachmentRepository extends BaseRepository<TransactionAttachment, Long> {
    /**
     * Find all attachments for a specific transaction.
     */
    Flux<TransactionAttachment> findByTransactionId(Long transactionId, Pageable pageable);

    /**
     * Count all attachments for a specific transaction.
     */
    Mono<Long> countByTransactionId(Long transactionId);

    /**
     * Find all attachments of a specific type for a transaction.
     */
    Flux<TransactionAttachment> findByTransactionIdAndAttachmentType(Long transactionId, AttachmentTypeEnum attachmentType, Pageable pageable);

    /**
     * Count all attachments of a specific type for a transaction.
     */
    Mono<Long> countByTransactionIdAndAttachmentType(Long transactionId, AttachmentTypeEnum attachmentType);
}
