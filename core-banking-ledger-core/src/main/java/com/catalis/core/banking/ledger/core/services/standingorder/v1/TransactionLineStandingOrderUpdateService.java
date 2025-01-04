package com.catalis.core.banking.ledger.core.services.standingorder.v1;

import com.catalis.core.banking.ledger.core.mappers.standingorder.v1.TransactionLineStandingOrderMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionLineStandingOrderDTO;
import com.catalis.core.banking.ledger.models.repositories.standingorder.v1.TransactionLineStandingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineStandingOrderUpdateService {

    @Autowired
    private TransactionLineStandingOrderRepository repository;

    @Autowired
    private TransactionLineStandingOrderMapper mapper;

    /**
     * Updates an existing TransactionLineStandingOrder entity with the provided data
     * from the TransactionLineStandingOrderDTO and returns the updated DTO.
     *
     * @param transactionLineStandingOrderId the ID of the TransactionLineStandingOrder to be updated
     * @param transactionLineStandingOrderDTO the data transfer object containing updated information for the TransactionLineStandingOrder
     * @return a Mono emitting the updated TransactionLineStandingOrderDTO
     */
    public Mono<TransactionLineStandingOrderDTO> updateTransactionLineStandingOrder(
            Long transactionLineStandingOrderId,
            TransactionLineStandingOrderDTO transactionLineStandingOrderDTO) {
        return repository.findById(transactionLineStandingOrderId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "TransactionLineStandingOrder with ID " + transactionLineStandingOrderId + " not found")))
                .flatMap(existingEntity -> {
                    // Update all fields of the entity using values from the DTO
                    existingEntity.setTransactionId(transactionLineStandingOrderDTO.getTransactionId());
                    existingEntity.setStandingOrderId(transactionLineStandingOrderDTO.getStandingOrderId());
                    existingEntity.setStandingOrderFrequency(transactionLineStandingOrderDTO.getStandingOrderFrequency());
                    existingEntity.setStandingOrderStartDate(transactionLineStandingOrderDTO.getStandingOrderStartDate());
                    existingEntity.setStandingOrderEndDate(transactionLineStandingOrderDTO.getStandingOrderEndDate());
                    existingEntity.setStandingOrderNextExecutionDate(transactionLineStandingOrderDTO.getStandingOrderNextExecutionDate());
                    existingEntity.setStandingOrderReference(transactionLineStandingOrderDTO.getStandingOrderReference());
                    existingEntity.setStandingOrderRecipientName(transactionLineStandingOrderDTO.getStandingOrderRecipientName());
                    existingEntity.setStandingOrderRecipientIban(transactionLineStandingOrderDTO.getStandingOrderRecipientIban());
                    existingEntity.setStandingOrderRecipientBic(transactionLineStandingOrderDTO.getStandingOrderRecipientBic());
                    existingEntity.setStandingOrderPurpose(transactionLineStandingOrderDTO.getStandingOrderPurpose());
                    existingEntity.setStandingOrderStatus(transactionLineStandingOrderDTO.getStandingOrderStatus());
                    existingEntity.setStandingOrderNotes(transactionLineStandingOrderDTO.getStandingOrderNotes());
                    existingEntity.setStandingOrderLastExecutionDate(transactionLineStandingOrderDTO.getStandingOrderLastExecutionDate());
                    existingEntity.setStandingOrderTotalExecutions(transactionLineStandingOrderDTO.getStandingOrderTotalExecutions());
                    existingEntity.setStandingOrderCancelledDate(transactionLineStandingOrderDTO.getStandingOrderCancelledDate());
                    existingEntity.setStandingOrderSuspendedUntilDate(transactionLineStandingOrderDTO.getStandingOrderSuspendedUntilDate());
                    existingEntity.setStandingOrderCreatedBy(transactionLineStandingOrderDTO.getStandingOrderCreatedBy());
                    existingEntity.setStandingOrderUpdatedBy(transactionLineStandingOrderDTO.getStandingOrderUpdatedBy());
                    existingEntity.setStandingOrderCreationTimestamp(transactionLineStandingOrderDTO.getStandingOrderCreationTimestamp());
                    existingEntity.setStandingOrderUpdateTimestamp(transactionLineStandingOrderDTO.getStandingOrderUpdateTimestamp());

                    // Save the updated entity
                    return repository.save(existingEntity);
                })
                .map(mapper::toDTO) // Map the updated entity back to a DTO
                .onErrorResume(e -> {
                    // Handle and propagate errors gracefully
                    return Mono.error(new RuntimeException(
                            "Error occurred while updating TransactionLineStandingOrder", e));
                });
    }

}