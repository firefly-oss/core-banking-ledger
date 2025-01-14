package com.catalis.core.banking.ledger.core.services.core.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.filters.FilterUtils;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.core.mappers.core.v1.TransactionMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.models.entities.core.v1.Transaction;
import com.catalis.core.banking.ledger.models.repositories.core.v1.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private TransactionMapper mapper;

    @Override
    public Mono<TransactionDTO> createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = mapper.toEntity(transactionDTO);
        return repository.save(transaction)
                .map(mapper::toDTO);
    }

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
    public Mono<TransactionDTO> getTransaction(Long transactionId) {
        return repository.findById(transactionId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TransactionDTO> updateTransaction(Long transactionId, TransactionDTO transactionDTO) {
        return repository.findById(transactionId)
                .flatMap(existingTransaction -> {
                    Transaction updatedTransaction = mapper.toEntity(transactionDTO);
                    updatedTransaction.setTransactionId(existingTransaction.getTransactionId());
                    return repository.save(updatedTransaction);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteTransaction(Long transactionId) {
        return repository.findById(transactionId)
                .flatMap(repository::delete);
    }
}