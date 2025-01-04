package com.catalis.core.banking.ledger.core.services.card.v1;

import com.catalis.core.banking.ledger.core.mappers.card.v1.TransactionLineCardMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.card.v1.TransactionLineCardDTO;
import com.catalis.core.banking.ledger.models.entities.card.v1.TransactionLineCard;
import com.catalis.core.banking.ledger.models.repositories.card.v1.TransactionLineCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TransactionLineCardUpdateService {

    @Autowired
    private TransactionLineCardRepository repository;

    @Autowired
    private TransactionLineCardMapper mapper;

    /**
     * Updates the details of an existing TransactionLineCard entity based on the provided DTO
     * and returns the updated entity as a DTO.
     *
     * @param transactionLineCardId The ID of the TransactionLineCard entity to be updated.
     * @param transactionLineCardDTO The data transfer object containing the updated details for the TransactionLineCard.
     * @return A Mono containing the updated TransactionLineCardDTO.
     *         If the given ID does not correspond to an existing entity, an error is emitted.
     */
    public Mono<TransactionLineCardDTO> updateTransactionLineCard(Long transactionLineCardId,
                                                                  TransactionLineCardDTO transactionLineCardDTO) {
        return repository.findById(transactionLineCardId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("TransactionLineCard not found")))
                .flatMap(existingEntity -> {
                    existingEntity.setTransactionId(transactionLineCardDTO.getTransactionId());
                    existingEntity.setCardAuthCode(transactionLineCardDTO.getCardAuthCode());
                    existingEntity.setCardMerchantCategoryCode(transactionLineCardDTO.getCardMerchantCategoryCode());
                    existingEntity.setCardMerchantName(transactionLineCardDTO.getCardMerchantName());
                    existingEntity.setCardPosEntryMode(transactionLineCardDTO.getCardPosEntryMode());
                    existingEntity.setCardTransactionReference(transactionLineCardDTO.getCardTransactionReference());
                    existingEntity.setCardTerminalId(transactionLineCardDTO.getCardTerminalId());
                    existingEntity.setCardHolderCountry(transactionLineCardDTO.getCardHolderCountry());
                    existingEntity.setCardPresentFlag(transactionLineCardDTO.getCardPresentFlag());
                    existingEntity.setCardTransactionTimestamp(transactionLineCardDTO.getCardTransactionTimestamp());
                    existingEntity.setCardFraudFlag(transactionLineCardDTO.getCardFraudFlag());
                    existingEntity.setCardCurrencyConversionRate(transactionLineCardDTO.getCardCurrencyConversionRate());
                    existingEntity.setCardFeeAmount(transactionLineCardDTO.getCardFeeAmount());
                    existingEntity.setCardFeeCurrency(transactionLineCardDTO.getCardFeeCurrency());
                    existingEntity.setCardInstallmentPlan(transactionLineCardDTO.getCardInstallmentPlan());
                    return repository.save(existingEntity);
                })
                .map(mapper::toDTO);
    }

}
