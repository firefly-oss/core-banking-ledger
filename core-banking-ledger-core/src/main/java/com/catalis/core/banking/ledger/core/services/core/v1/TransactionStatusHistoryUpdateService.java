package com.catalis.core.banking.ledger.core.services.core.v1;

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
public class TransactionStatusHistoryUpdateService {

    @Autowired
    private TransactionStatusHistoryRepository repository;

    @Autowired
    private TransactionStatusHistoryMapper mapper;

    /**
     * Updates an existing transaction status history entry with new data.
     *
     * @param statusHistoryId The ID of the existing transaction status history entry to update.
     * @param statusHistoryDTO The new data to update the transaction status history entry with.
     * @return A {@code Mono<TransactionStatusHistoryDTO>} containing the updated transaction status history entry.
     *         Emits an error if the entry with the provided ID does not exist.
     */
    public Mono<TransactionStatusHistoryDTO> updateStatusHistoryEntry(
            Long statusHistoryId,
            TransactionStatusHistoryDTO statusHistoryDTO) {
        return repository.findById(statusHistoryId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Status history entry not found")))
                .flatMap(existingStatus -> {
                    TransactionStatusHistory updatedStatus = mapper.toEntity(statusHistoryDTO);
                    updatedStatus.setTransactionStatusHistoryId(existingStatus.getTransactionStatusHistoryId());
                    updatedStatus.setTransactionId(existingStatus.getTransactionId());

                    return repository.save(updatedStatus)
                            .map(mapper::toDTO);
                });
    }

}
