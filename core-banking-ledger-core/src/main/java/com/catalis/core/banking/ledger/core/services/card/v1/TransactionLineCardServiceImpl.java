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
public class TransactionLineCardServiceImpl implements TransactionLineCardService {

    @Autowired
    private TransactionLineCardRepository repository;

    @Autowired
    private TransactionLineCardMapper mapper;

    @Override
    public Mono<TransactionLineCardDTO> getCardLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line Card not found")));
    }

    @Override
    public Mono<TransactionLineCardDTO> createCardLine(Long transactionId, TransactionLineCardDTO cardDTO) {
        cardDTO.setTransactionId(transactionId);
        TransactionLineCard entity = mapper.toEntity(cardDTO);
        return repository.save(entity)
                .map(mapper::toDTO)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to create Transaction Line Card", e)));
    }

    @Override
    public Mono<TransactionLineCardDTO> updateCardLine(Long transactionId, TransactionLineCardDTO cardDTO) {
        return repository.findByTransactionId(transactionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line Card not found")))
                .flatMap(existingEntity -> {
                    cardDTO.setTransactionLineCardId(existingEntity.getTransactionLineCardId());
                    cardDTO.setTransactionId(transactionId);
                    TransactionLineCard updatedEntity = mapper.toEntity(cardDTO);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO)
                .onErrorResume(e -> {
                    if (e.getMessage() != null && e.getMessage().equals("Transaction Line Card not found")) {
                        return Mono.error(e);
                    }
                    return Mono.error(new RuntimeException("Failed to update Transaction Line Card", e));
                });
    }

    @Override
    public Mono<Void> deleteCardLine(Long transactionId) {
        return repository.findByTransactionId(transactionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction Line Card not found")))
                .flatMap(repository::delete);
    }
}
