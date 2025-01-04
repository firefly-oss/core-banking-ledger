package com.catalis.core.banking.ledger.core.services.sepa.v1;

import com.catalis.core.banking.ledger.core.mappers.sepa.v1.TransactionLineSepaTransferMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.sepa.v1.TransactionLineSepaTransferDTO;
import com.catalis.core.banking.ledger.models.repositories.sepa.v1.TransactionLineSepaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineSepaUpdateService {

    @Autowired
    private TransactionLineSepaRepository repository;

    @Autowired
    private TransactionLineSepaTransferMapper mapper;

    /**
     * Updates an existing TransactionLineSepaTransfer with the provided details.
     *
     * @param transactionLineSepaTransferId  the ID of the transaction to update
     * @param transactionLineSepaTransferDTO the updated details
     * @return a {@code Mono<TransactionLineSepaTransferDTO>} containing the updated transaction details
     */
    public Mono<TransactionLineSepaTransferDTO> updateTransactionLineSepaTransfer(
            Long transactionLineSepaTransferId,
            TransactionLineSepaTransferDTO transactionLineSepaTransferDTO) {

        return repository.findById(transactionLineSepaTransferId)
                .switchIfEmpty(Mono.error(new RuntimeException(
                        "TransactionLineSepaTransfer not found with id: " + transactionLineSepaTransferId)))
                .flatMap(existingEntity -> {
                    // Update fields on the existing entity
                    existingEntity.setTransactionId(transactionLineSepaTransferDTO.getTransactionId());
                    existingEntity.setSepaEndToEndId(transactionLineSepaTransferDTO.getSepaEndToEndId());
                    existingEntity.setSepaRemittanceInfo(transactionLineSepaTransferDTO.getSepaRemittanceInfo());
                    existingEntity.setSepaOriginIban(transactionLineSepaTransferDTO.getSepaOriginIban());
                    existingEntity.setSepaOriginBic(transactionLineSepaTransferDTO.getSepaOriginBic());
                    existingEntity.setSepaDestinationIban(transactionLineSepaTransferDTO.getSepaDestinationIban());
                    existingEntity.setSepaDestinationBic(transactionLineSepaTransferDTO.getSepaDestinationBic());
                    existingEntity.setSepaTransactionStatus(transactionLineSepaTransferDTO.getSepaTransactionStatus());
                    existingEntity.setSepaCreditorId(transactionLineSepaTransferDTO.getSepaCreditorId());
                    existingEntity.setSepaDebtorId(transactionLineSepaTransferDTO.getSepaDebtorId());
                    existingEntity.setSepaInitiatingAgentBic(transactionLineSepaTransferDTO.getSepaInitiatingAgentBic());
                    existingEntity.setSepaIntermediaryBic(transactionLineSepaTransferDTO.getSepaIntermediaryBic());
                    existingEntity.setSepaTransactionPurpose(transactionLineSepaTransferDTO.getSepaTransactionPurpose());
                    existingEntity.setSepaRequestedExecutionDate(transactionLineSepaTransferDTO.getSepaRequestedExecutionDate());
                    existingEntity.setSepaExchangeRate(transactionLineSepaTransferDTO.getSepaExchangeRate());
                    existingEntity.setSepaFeeAmount(transactionLineSepaTransferDTO.getSepaFeeAmount());
                    existingEntity.setSepaFeeCurrency(transactionLineSepaTransferDTO.getSepaFeeCurrency());
                    existingEntity.setSepaRecipientName(transactionLineSepaTransferDTO.getSepaRecipientName());
                    existingEntity.setSepaRecipientAddress(transactionLineSepaTransferDTO.getSepaRecipientAddress());
                    existingEntity.setSepaProcessingDate(transactionLineSepaTransferDTO.getSepaProcessingDate());
                    existingEntity.setSepaNotes(transactionLineSepaTransferDTO.getSepaNotes());

                    // Persist the updated entity
                    return repository.save(existingEntity);
                })
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException(
                        "Error updating TransactionLineSepaTransfer with id: " + transactionLineSepaTransferId, e)));
    }
}