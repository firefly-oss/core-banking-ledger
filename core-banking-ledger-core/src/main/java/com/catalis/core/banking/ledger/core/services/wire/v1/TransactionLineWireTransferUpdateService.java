package com.catalis.core.banking.ledger.core.services.wire.v1;

import com.catalis.core.banking.ledger.core.mappers.wire.v1.TransactionLineWireTransferMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.wire.v1.TransactionLineWireTransferDTO;
import com.catalis.core.banking.ledger.models.repositories.wire.v1.TransactionLineWireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineWireTransferUpdateService {

    @Autowired
    private TransactionLineWireRepository repository;

    @Autowired
    private TransactionLineWireTransferMapper mapper;

    /**
     * Updates the details of an existing TransactionLineWireTransfer entity using the values provided in the DTO.
     * The method retrieves the entity by its ID, updates its properties with values from the DTO,
     * saves the updated entity to the database, and maps it back to a DTO for response.
     *
     * @param transactionLineWireTransferId the unique identifier of the TransactionLineWireTransfer to be updated
     * @param dto the data transfer object containing the updated details for the TransactionLineWireTransfer
     * @return a Mono emitting the updated TransactionLineWireTransferDTO if the operation is successful, or an error if not
     */
    public Mono<TransactionLineWireTransferDTO> updateTransactionLineWireTransfer(
            Long transactionLineWireTransferId,
            TransactionLineWireTransferDTO dto) {
        return repository.findById(transactionLineWireTransferId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "TransactionLineWireTransfer with ID " + transactionLineWireTransferId + " not found")))
                .flatMap(existingEntity -> {
                    // Update entity fields with values from the DTO
                    existingEntity.setTransactionId(dto.getTransactionId());
                    existingEntity.setWireTransferReference(dto.getWireTransferReference());
                    existingEntity.setWireOriginSwiftBic(dto.getWireOriginSwiftBic());
                    existingEntity.setWireDestinationSwiftBic(dto.getWireDestinationSwiftBic());
                    existingEntity.setWireOriginAccountNumber(dto.getWireOriginAccountNumber());
                    existingEntity.setWireDestinationAccountNumber(dto.getWireDestinationAccountNumber());
                    existingEntity.setWireTransferPurpose(dto.getWireTransferPurpose());
                    existingEntity.setWireTransferPriority(dto.getWireTransferPriority());
                    existingEntity.setWireExchangeRate(dto.getWireExchangeRate());
                    existingEntity.setWireFeeAmount(dto.getWireFeeAmount());
                    existingEntity.setWireFeeCurrency(dto.getWireFeeCurrency());
                    existingEntity.setWireInstructingParty(dto.getWireInstructingParty());
                    existingEntity.setWireBeneficiaryName(dto.getWireBeneficiaryName());
                    existingEntity.setWireBeneficiaryAddress(dto.getWireBeneficiaryAddress());
                    existingEntity.setWireProcessingDate(dto.getWireProcessingDate());
                    existingEntity.setWireTransactionNotes(dto.getWireTransactionNotes());
                    existingEntity.setWireReceptionStatus(dto.getWireReceptionStatus());
                    existingEntity.setWireDeclineReason(dto.getWireDeclineReason());
                    existingEntity.setWireCancelledFlag(dto.getWireCancelledFlag());

                    return repository.save(existingEntity);
                })
                .map(mapper::toDTO) // Map the updated entity back to a DTO
                .onErrorResume(e -> {
                    // Catch and propagate meaningful error messages
                    return Mono.error(new RuntimeException(
                            "Error occurred while updating TransactionLineWireTransfer", e));
                });
    }

}