package com.catalis.core.banking.ledger.core.services.core.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.core.mappers.core.v1.TransactionMapper;
import com.catalis.core.banking.ledger.core.mappers.core.v1.TransactionStatusHistoryMapper;
import com.catalis.core.banking.ledger.core.services.event.v1.EventOutboxService;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.catalis.core.banking.ledger.models.entities.core.v1.Transaction;
import com.catalis.core.banking.ledger.models.entities.core.v1.TransactionStatusHistory;
import com.catalis.core.banking.ledger.models.repositories.core.v1.TransactionRepository;
import com.catalis.core.banking.ledger.models.repositories.core.v1.TransactionStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementation of the TransactionService interface.
 * Provides functionality for managing financial transactions including CRUD operations
 * and various search and filtering capabilities.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private TransactionStatusHistoryRepository statusHistoryRepository;

    @Autowired
    private TransactionMapper mapper;

    @Autowired
    private TransactionStatusHistoryMapper statusHistoryMapper;

    @Autowired
    private EventOutboxService eventOutboxService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TransactionDTO> createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = mapper.toEntity(transactionDTO);
        return repository.save(transaction)
                .flatMap(savedTransaction -> {
                    // Create initial status history record
                    TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                    statusHistory.setTransactionId(savedTransaction.getTransactionId());
                    statusHistory.setStatusCode(savedTransaction.getTransactionStatus());
                    statusHistory.setStatusStartDatetime(LocalDateTime.now());
                    statusHistory.setReason("Initial transaction creation");
                    statusHistory.setRegulatedReportingFlag(false);

                    return statusHistoryRepository.save(statusHistory)
                            .then(Mono.just(savedTransaction));
                })
                .flatMap(savedTransaction -> {
                    // Publish transaction created event
                    return eventOutboxService.publishEvent(
                            "TRANSACTION",
                            savedTransaction.getTransactionId().toString(),
                            "TRANSACTION_CREATED",
                            mapper.toDTO(savedTransaction)
                    ).then(Mono.just(savedTransaction));
                })
                .map(mapper::toDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TransactionDTO> getTransaction(Long transactionId) {
        return repository.findById(transactionId)
                .map(mapper::toDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TransactionDTO> updateTransaction(Long transactionId, TransactionDTO transactionDTO) {
        return repository.findById(transactionId)
                .flatMap(existingTransaction -> {
                    TransactionStatusEnum oldStatus = existingTransaction.getTransactionStatus();
                    Transaction updatedTransaction = mapper.toEntity(transactionDTO);
                    updatedTransaction.setTransactionId(existingTransaction.getTransactionId());

                    return repository.save(updatedTransaction)
                            .flatMap(savedTransaction -> {
                                // If status has changed, create a status history record
                                if (!oldStatus.equals(savedTransaction.getTransactionStatus())) {
                                    TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                                    statusHistory.setTransactionId(savedTransaction.getTransactionId());
                                    statusHistory.setStatusCode(savedTransaction.getTransactionStatus());
                                    statusHistory.setStatusStartDatetime(LocalDateTime.now());
                                    statusHistory.setReason("Status updated via API");
                                    statusHistory.setRegulatedReportingFlag(false);

                                    return statusHistoryRepository.save(statusHistory)
                                            .then(Mono.just(savedTransaction));
                                }
                                return Mono.just(savedTransaction);
                            })
                            .flatMap(savedTransaction -> {
                                // Publish transaction updated event
                                return eventOutboxService.publishEvent(
                                        "TRANSACTION",
                                        savedTransaction.getTransactionId().toString(),
                                        "TRANSACTION_UPDATED",
                                        mapper.toDTO(savedTransaction)
                                ).then(Mono.just(savedTransaction));
                            });
                })
                .map(mapper::toDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> deleteTransaction(Long transactionId) {
        return repository.findById(transactionId)
                .flatMap(transaction -> {
                    // Publish transaction deleted event
                    return eventOutboxService.publishEvent(
                            "TRANSACTION",
                            transaction.getTransactionId().toString(),
                            "TRANSACTION_DELETED",
                            mapper.toDTO(transaction)
                    ).then(repository.delete(transaction));
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<PaginationResponse<TransactionDTO>> filterTransactions(FilterRequest<TransactionDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        Transaction.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<TransactionDTO> updateTransactionStatus(Long transactionId, TransactionStatusEnum newStatus, String reason) {
        return repository.findById(transactionId)
                .flatMap(transaction -> {
                    TransactionStatusEnum oldStatus = transaction.getTransactionStatus();

                    // Only update if status is different
                    if (oldStatus.equals(newStatus)) {
                        return Mono.just(transaction);
                    }

                    // Update transaction status
                    transaction.setTransactionStatus(newStatus);

                    return repository.save(transaction)
                            .flatMap(savedTransaction -> {
                                // Create status history record
                                TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                                statusHistory.setTransactionId(savedTransaction.getTransactionId());
                                statusHistory.setStatusCode(newStatus);
                                statusHistory.setStatusStartDatetime(LocalDateTime.now());
                                statusHistory.setReason(reason);
                                statusHistory.setRegulatedReportingFlag(false);

                                return statusHistoryRepository.save(statusHistory)
                                        .then(Mono.just(savedTransaction));
                            })
                            .flatMap(savedTransaction -> {
                                // Publish transaction status changed event
                                return eventOutboxService.publishEvent(
                                        "TRANSACTION",
                                        savedTransaction.getTransactionId().toString(),
                                        "TRANSACTION_STATUS_CHANGED",
                                        mapper.toDTO(savedTransaction)
                                ).then(Mono.just(savedTransaction));
                            });
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionDTO> createReversalTransaction(Long originalTransactionId, String reason) {
        return repository.findById(originalTransactionId)
                .flatMap(originalTransaction -> {
                    // Create reversal transaction
                    Transaction reversalTransaction = new Transaction();
                    reversalTransaction.setTransactionType(originalTransaction.getTransactionType());
                    reversalTransaction.setTransactionStatus(TransactionStatusEnum.PENDING);
                    reversalTransaction.setTotalAmount(originalTransaction.getTotalAmount().negate());
                    reversalTransaction.setCurrency(originalTransaction.getCurrency());
                    reversalTransaction.setDescription("Reversal: " + originalTransaction.getDescription());
                    reversalTransaction.setInitiatingParty(originalTransaction.getInitiatingParty());
                    reversalTransaction.setAccountId(originalTransaction.getAccountId());
                    reversalTransaction.setAccountSpaceId(originalTransaction.getAccountSpaceId());
                    reversalTransaction.setTransactionCategoryId(originalTransaction.getTransactionCategoryId());
                    reversalTransaction.setTransactionDate(LocalDateTime.now());
                    reversalTransaction.setValueDate(LocalDateTime.now());
                    reversalTransaction.setRelatedTransactionId(originalTransactionId);
                    reversalTransaction.setRelationType("REVERSAL");
                    reversalTransaction.setRequestId("REVERSAL-" + originalTransaction.getTransactionId());

                    return repository.save(reversalTransaction)
                            .flatMap(savedReversal -> {
                                // Create status history record
                                TransactionStatusHistory statusHistory = new TransactionStatusHistory();
                                statusHistory.setTransactionId(savedReversal.getTransactionId());
                                statusHistory.setStatusCode(savedReversal.getTransactionStatus());
                                statusHistory.setStatusStartDatetime(LocalDateTime.now());
                                statusHistory.setReason(reason);
                                statusHistory.setRegulatedReportingFlag(false);

                                return statusHistoryRepository.save(statusHistory)
                                        .then(Mono.just(savedReversal));
                            })
                            .flatMap(savedReversal -> {
                                // Publish reversal transaction created event
                                return eventOutboxService.publishEvent(
                                        "TRANSACTION",
                                        savedReversal.getTransactionId().toString(),
                                        "REVERSAL_TRANSACTION_CREATED",
                                        mapper.toDTO(savedReversal)
                                ).then(Mono.just(savedReversal));
                            });
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionDTO> findByExternalReference(String externalReference) {
        return repository.findByExternalReference(externalReference)
                .map(mapper::toDTO);
    }
}