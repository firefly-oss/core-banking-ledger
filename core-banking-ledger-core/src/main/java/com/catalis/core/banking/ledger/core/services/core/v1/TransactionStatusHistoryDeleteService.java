package com.catalis.core.banking.ledger.core.services.core.v1;

import com.catalis.core.banking.ledger.models.repositories.core.v1.TransactionStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionStatusHistoryDeleteService {

    @Autowired
    private TransactionStatusHistoryRepository repository;

    /**
     * Deletes a status history entry identified by the provided unique identifier.
     *
     * @param statusHistoryId the unique identifier of the status history entry to delete
     * @return a {@code Mono<Void>} that completes when the deletion is successful, or emits an error
     *         if the entry does not exist or an issue occurs during the deletion process
     */
    public Mono<Void> deleteStatusHistoryEntry(Long statusHistoryId) {
        return repository.findById(statusHistoryId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Status history entry not found")))
                .flatMap(statusHistory -> repository.delete(statusHistory));
    }

}