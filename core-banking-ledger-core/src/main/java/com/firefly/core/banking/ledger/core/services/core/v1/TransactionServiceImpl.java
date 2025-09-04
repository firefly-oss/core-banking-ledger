/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.core.banking.ledger.core.services.core.v1;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.filters.FilterUtils;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.banking.ledger.core.mappers.core.v1.TransactionMapper;
import com.firefly.core.banking.ledger.core.mappers.core.v1.TransactionStatusHistoryMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.firefly.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
import com.firefly.core.banking.ledger.models.entities.core.v1.Transaction;
import com.firefly.core.banking.ledger.models.entities.core.v1.TransactionStatusHistory;
import com.firefly.core.banking.ledger.models.repositories.core.v1.TransactionRepository;
import com.firefly.core.banking.ledger.models.repositories.core.v1.TransactionStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.UUID;
/**
 * Implementation of the TransactionService interface.
 * Provides basic CRUD operations for transaction data persistence
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

                .map(mapper::toDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TransactionDTO> getTransaction(UUID transactionId) {
        return repository.findById(transactionId)
                .map(mapper::toDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TransactionDTO> updateTransaction(UUID transactionId, TransactionDTO transactionDTO) {
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
;
                })
                .map(mapper::toDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> deleteTransaction(UUID transactionId) {
        return repository.findById(transactionId)
                .flatMap(repository::delete);
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
    public Flux<TransactionDTO> getTransactionsByAccountId(UUID accountId) {
        return repository.findByAccountId(accountId)
                .map(mapper::toDTO);
    }

    @Override
    public Flux<TransactionDTO> getTransactionsByAccountSpaceId(UUID accountSpaceId) {
        return repository.findByAccountSpaceId(accountSpaceId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionDTO> updateTransactionStatus(UUID transactionId, TransactionStatusEnum newStatus, String reason) {
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