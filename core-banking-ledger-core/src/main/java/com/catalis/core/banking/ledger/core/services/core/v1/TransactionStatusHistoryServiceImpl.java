package com.catalis.core.banking.ledger.core.services.core.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.ledger.core.mappers.core.v1.TransactionStatusHistoryMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionStatusHistoryDTO;
import com.catalis.core.banking.ledger.models.entities.core.v1.TransactionStatusHistory;
import com.catalis.core.banking.ledger.models.repositories.core.v1.TransactionStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionStatusHistoryServiceImpl implements TransactionStatusHistoryService {

    @Autowired
    private TransactionStatusHistoryRepository repository;

    @Autowired
    private TransactionStatusHistoryMapper mapper;

    @Override
    public Mono<PaginationResponse<TransactionStatusHistoryDTO>> listStatusHistory(Long transactionId, PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                pageable -> repository.findByTransactionId(transactionId, pageable),
                () -> repository.countByTransactionId(transactionId)
        );
    }

    @Override
    public Mono<TransactionStatusHistoryDTO> createStatusHistory(Long transactionId, TransactionStatusHistoryDTO historyDTO) {
        historyDTO.setTransactionId(transactionId);
        TransactionStatusHistory entity = mapper.toEntity(historyDTO);
        return repository.save(entity).map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionStatusHistoryDTO> getStatusHistory(Long transactionId, Long historyId) {
        return repository.findById(historyId)
                .filter(entity -> entity.getTransactionId().equals(transactionId))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionStatusHistoryDTO> updateStatusHistory(Long transactionId, Long historyId, TransactionStatusHistoryDTO historyDTO) {
        return repository.findById(historyId)
                .filter(entity -> entity.getTransactionId().equals(transactionId))
                .flatMap(existingEntity -> {
                    TransactionStatusHistory updatedEntity = mapper.toEntity(historyDTO);
                    updatedEntity.setTransactionStatusHistoryId(existingEntity.getTransactionStatusHistoryId());
                    updatedEntity.setTransactionId(existingEntity.getTransactionId());
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteStatusHistory(Long transactionId, Long historyId) {
        return repository.findById(historyId)
                .filter(entity -> entity.getTransactionId().equals(transactionId))
                .flatMap(repository::delete);
    }
}