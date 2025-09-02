package com.firefly.core.banking.ledger.models.repositories.attachment.v1;

import com.firefly.core.banking.ledger.interfaces.enums.attachment.v1.AttachmentTypeEnum;
import com.firefly.core.banking.ledger.models.entities.attachment.v1.TransactionAttachment;
import com.firefly.core.banking.ledger.models.repositories.BaseRepository;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;
/**
 * Repository interface for transaction attachments.
 */
public interface TransactionAttachmentRepository extends BaseRepository<TransactionAttachment, UUID> {
    /**
     * Find all attachments for a specific transaction.
     */
    Flux<TransactionAttachment> findByTransactionId(UUID transactionId, Pageable pageable);

    /**
     * Count all attachments for a specific transaction.
     */
    Mono<Long> countByTransactionId(UUID transactionId);

    /**
     * Find all attachments of a specific type for a transaction.
     */
    Flux<TransactionAttachment> findByTransactionIdAndAttachmentType(UUID transactionId, AttachmentTypeEnum attachmentType, Pageable pageable);

    /**
     * Count all attachments of a specific type for a transaction.
     */
    Mono<Long> countByTransactionIdAndAttachmentType(UUID transactionId, AttachmentTypeEnum attachmentType);
}
