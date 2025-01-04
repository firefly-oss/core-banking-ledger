package com.catalis.core.banking.ledger.core.services.core.v1;

import com.catalis.core.banking.ledger.core.mappers.core.v1.TransactionStatusHistoryMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionStatusHistoryDTO;
import com.catalis.core.banking.ledger.models.repositories.core.v1.TransactionStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionStatusHistoryCreateService {

    @Autowired
    private TransactionStatusHistoryRepository repository;

    @Autowired
    private TransactionStatusHistoryMapper mapper;

    /**
     * Adds a status history entry for a specific transaction.
     * This method retrieves the transaction by its ID, maps the provided status history DTO
     * to its corresponding entity, associates it with the transaction, saves it to the repository,
     * and then returns the saved entry as a DTO.
     *
     * @param transactionId the unique identifier of the transaction for which the status history
     *                      entry is being added
     * @param statusHistoryDTO the data transfer object containing the details of the status
     *                         history entry to be added
     * @return a {@code Mono} emitting the newly added {@code TransactionStatusHistoryDTO}
     *         if the transaction exists and the entry is successfully added, or an error
     *         if the transaction is not found or an issue occurs during the process
     */
    public Mono<TransactionStatusHistoryDTO> addStatusHistoryEntry(
            Long transactionId,
            TransactionStatusHistoryDTO statusHistoryDTO) {
        return repository.findById(transactionId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Transaction not found")))
                .flatMap(transaction -> {
                    var statusHistory = mapper.toEntity(statusHistoryDTO);
                    statusHistory.setTransactionId(transactionId);
                    return repository.save(statusHistory)
                            .map(mapper::toDTO);
                });
    }
}
