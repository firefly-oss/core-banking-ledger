package com.catalis.core.banking.ledger.core.services.core.v1;

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
public class TransactionUpdateService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private TransactionMapper mapper;

    /**
     * Updates an existing transaction with the provided details.
     *
     * @param transactionId the ID of the transaction to be updated
     * @param transactionDTO the details to update the transaction with
     * @return a {@code Mono<TransactionDTO>} containing the updated transaction details
     */
    public Mono<TransactionDTO> updateTransaction(Long transactionId, TransactionDTO transactionDTO) {
        return repository.findById(transactionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction not found with id: " + transactionId)))
                .flatMap(existingTransaction -> {
                    existingTransaction.setExternalReference(transactionDTO.getExternalReference());
                    existingTransaction.setTransactionDate(transactionDTO.getTransactionDate());
                    existingTransaction.setValueDate(transactionDTO.getValueDate());
                    existingTransaction.setTransactionType(transactionDTO.getTransactionType());
                    existingTransaction.setTransactionStatus(transactionDTO.getTransactionStatus());
                    existingTransaction.setTotalAmount(transactionDTO.getTotalAmount());
                    existingTransaction.setCurrency(transactionDTO.getCurrency());
                    existingTransaction.setDescription(transactionDTO.getDescription());
                    existingTransaction.setInitiatingParty(transactionDTO.getInitiatingParty());
                    existingTransaction.setAccountId(transactionDTO.getAccountId());
                    existingTransaction.setTransactionCategoryId(transactionDTO.getTransactionCategoryId());
                    return repository.save(existingTransaction);
                })
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error updating transaction with id: " + transactionId, e)));
    }
    
}
