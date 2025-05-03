package com.catalis.core.banking.ledger.core.services.attachment.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.attachment.v1.TransactionAttachmentMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.attachment.v1.TransactionAttachmentDTO;
import com.catalis.core.banking.ledger.models.entities.attachment.v1.TransactionAttachment;
import com.catalis.core.banking.ledger.models.repositories.attachment.v1.TransactionAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementation of the TransactionAttachmentService interface.
 */
@Service
@Transactional
public class TransactionAttachmentServiceImpl implements TransactionAttachmentService {

    @Autowired
    private TransactionAttachmentRepository repository;

    @Autowired
    private TransactionAttachmentMapper mapper;

    @Override
    public Mono<TransactionAttachmentDTO> createAttachment(Long transactionId, TransactionAttachmentDTO attachmentDTO) {
        attachmentDTO.setTransactionId(transactionId);
        TransactionAttachment entity = mapper.toEntity(attachmentDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionAttachmentDTO> getAttachment(Long transactionId, Long attachmentId) {
        return repository.findById(attachmentId)
                .filter(entity -> entity.getTransactionId().equals(transactionId))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<TransactionAttachmentDTO>> listAttachments(Long transactionId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByTransactionId(transactionId, pageable),
                () -> repository.countByTransactionId(transactionId)
        );
    }

    @Override
    public Mono<PaginationResponse<TransactionAttachmentDTO>> listAttachmentsByType(
            Long transactionId,
            String attachmentType,
            PaginationRequest paginationRequest
    ) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByTransactionIdAndAttachmentType(transactionId, attachmentType, pageable),
                () -> repository.countByTransactionIdAndAttachmentType(transactionId, attachmentType)
        );
    }
}
