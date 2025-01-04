package com.catalis.core.banking.ledger.core.services.directdebit.v1;

import com.catalis.core.banking.ledger.core.mappers.directdebit.v1.TransactionLineDirectDebitMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionLineDirectDebitDTO;
import com.catalis.core.banking.ledger.models.repositories.directdebit.v1.TransactionLineDirectDebitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineDirectDebitUpdateService {


    @Autowired
    private TransactionLineDirectDebitRepository repository;

    @Autowired
    private TransactionLineDirectDebitMapper mapper;

    /**
     * Updates an existing TransactionLineDirectDebit record with the provided data.
     * If the record with the given ID does not exist, an error is returned.
     *
     * @param transactionLineDirectDebitId The ID of the TransactionLineDirectDebit to update.
     * @param transactionLineDirectDebitDTO The details of the updated TransactionLineDirectDebit.
     * @return A Mono emitting the updated TransactionLineDirectDebitDTO.
     *         Emits an error if the record is not found or if an exception occurs.
     */
    public Mono<TransactionLineDirectDebitDTO> updateTransactionLineDirectDebit(
            Long transactionLineDirectDebitId, TransactionLineDirectDebitDTO transactionLineDirectDebitDTO) {
        return repository.findById(transactionLineDirectDebitId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "TransactionLineDirectDebit with ID " + transactionLineDirectDebitId + " not found")))
                .flatMap(existingEntity -> {
                    existingEntity.setTransactionId(transactionLineDirectDebitDTO.getTransactionId());
                    existingEntity.setDirectDebitMandateId(transactionLineDirectDebitDTO.getDirectDebitMandateId());
                    existingEntity.setDirectDebitCreditorId(transactionLineDirectDebitDTO.getDirectDebitCreditorId());
                    existingEntity.setDirectDebitReference(transactionLineDirectDebitDTO.getDirectDebitReference());
                    existingEntity.setDirectDebitSequenceType(transactionLineDirectDebitDTO.getDirectDebitSequenceType());
                    existingEntity.setDirectDebitDueDate(transactionLineDirectDebitDTO.getDirectDebitDueDate());
                    existingEntity.setDirectDebitPaymentMethod(transactionLineDirectDebitDTO.getDirectDebitPaymentMethod());
                    existingEntity.setDirectDebitDebtorName(transactionLineDirectDebitDTO.getDirectDebitDebtorName());
                    existingEntity.setDirectDebitDebtorAddress(transactionLineDirectDebitDTO.getDirectDebitDebtorAddress());
                    existingEntity.setDirectDebitDebtorContact(transactionLineDirectDebitDTO.getDirectDebitDebtorContact());
                    existingEntity.setDirectDebitProcessingStatus(transactionLineDirectDebitDTO.getDirectDebitProcessingStatus());
                    existingEntity.setDirectDebitAuthorizationDate(transactionLineDirectDebitDTO.getDirectDebitAuthorizationDate());
                    existingEntity.setDirectDebitRevocationDate(transactionLineDirectDebitDTO.getDirectDebitRevocationDate());

                    return repository.save(existingEntity);
                })
                .map(updatedEntity -> mapper.toDTO(updatedEntity)) // Map updated entity to the DTO
                .onErrorResume(e -> {
                    // Handle exceptions during the process
                    return Mono.error(new RuntimeException(
                            "Error occurred while updating TransactionLineDirectDebit", e));
                });
    }

}
