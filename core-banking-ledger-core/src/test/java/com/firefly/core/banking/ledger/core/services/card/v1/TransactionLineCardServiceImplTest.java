package com.firefly.core.banking.ledger.core.services.card.v1;

import com.firefly.core.banking.ledger.core.mappers.card.v1.TransactionLineCardMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.card.v1.TransactionLineCardDTO;
import com.firefly.core.banking.ledger.models.entities.card.v1.TransactionLineCard;
import com.firefly.core.banking.ledger.models.repositories.card.v1.TransactionLineCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionLineCardServiceImplTest {

    @Mock
    private TransactionLineCardRepository repository;

    @Mock
    private TransactionLineCardMapper mapper;

    @InjectMocks
    private TransactionLineCardServiceImpl service;

    private TransactionLineCardDTO cardDTO;
    private TransactionLineCard cardEntity;
    private final Long transactionId = 1L;
    private final Long cardLineId = 2L;

    @BeforeEach
    void setUp() {
        // Initialize test data
        cardDTO = new TransactionLineCardDTO();
        cardDTO.setTransactionLineCardId(cardLineId);
        cardDTO.setTransactionId(transactionId);
        cardDTO.setCardAuthCode("AUTH123");
        cardDTO.setCardMerchantName("Test Merchant");
        cardDTO.setCardTerminalId("TERM123");
        cardDTO.setCardTransactionTimestamp(LocalDateTime.now());
        cardDTO.setCardPresentFlag(true);
        cardDTO.setCardFraudFlag(false);
        cardDTO.setCardFeeAmount(new BigDecimal("5.00"));
        cardDTO.setCardFeeCurrency("EUR");

        cardEntity = new TransactionLineCard();
        cardEntity.setTransactionLineCardId(cardLineId);
        cardEntity.setTransactionId(transactionId);
        cardEntity.setCardAuthCode("AUTH123");
        cardEntity.setCardMerchantName("Test Merchant");
        cardEntity.setCardTerminalId("TERM123");
        cardEntity.setCardTransactionTimestamp(LocalDateTime.now());
        cardEntity.setCardPresentFlag(true);
        cardEntity.setCardFraudFlag(false);
        cardEntity.setCardFeeAmount(new BigDecimal("5.00"));
        cardEntity.setCardFeeCurrency("EUR");
    }

    @Test
    void getCardLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(cardEntity));
        when(mapper.toDTO(any(TransactionLineCard.class))).thenReturn(cardDTO);

        // Act & Assert
        StepVerifier.create(service.getCardLine(transactionId))
                .expectNext(cardDTO)
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper).toDTO(cardEntity);
    }

    @Test
    void getCardLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getCardLine(transactionId))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Transaction Line Card not found"))
                .verify();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper, never()).toDTO(any(TransactionLineCard.class));
    }

    @Test
    void createCardLine_Success() {
        // Arrange
        when(mapper.toEntity(any(TransactionLineCardDTO.class))).thenReturn(cardEntity);
        when(repository.save(any(TransactionLineCard.class))).thenReturn(Mono.just(cardEntity));
        when(mapper.toDTO(any(TransactionLineCard.class))).thenReturn(cardDTO);

        // Act & Assert
        StepVerifier.create(service.createCardLine(transactionId, cardDTO))
                .expectNext(cardDTO)
                .verifyComplete();

        verify(mapper).toEntity(cardDTO);
        verify(repository).save(cardEntity);
        verify(mapper).toDTO(cardEntity);
    }

    @Test
    void createCardLine_Error() {
        // Arrange
        when(mapper.toEntity(any(TransactionLineCardDTO.class))).thenReturn(cardEntity);
        when(repository.save(any(TransactionLineCard.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act & Assert
        StepVerifier.create(service.createCardLine(transactionId, cardDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Failed to create Transaction Line Card"))
                .verify();

        verify(mapper).toEntity(cardDTO);
        verify(repository).save(cardEntity);
        verify(mapper, never()).toDTO(any(TransactionLineCard.class));
    }

    @Test
    void updateCardLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(cardEntity));
        when(mapper.toEntity(any(TransactionLineCardDTO.class))).thenReturn(cardEntity);
        when(repository.save(any(TransactionLineCard.class))).thenReturn(Mono.just(cardEntity));
        when(mapper.toDTO(any(TransactionLineCard.class))).thenReturn(cardDTO);

        // Act & Assert
        StepVerifier.create(service.updateCardLine(transactionId, cardDTO))
                .expectNext(cardDTO)
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper).toEntity(cardDTO);
        verify(repository).save(cardEntity);
        verify(mapper).toDTO(cardEntity);
    }

    @Test
    void updateCardLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateCardLine(transactionId, cardDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Transaction Line Card not found"))
                .verify();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper, never()).toEntity(any(TransactionLineCardDTO.class));
        verify(repository, never()).save(any(TransactionLineCard.class));
        verify(mapper, never()).toDTO(any(TransactionLineCard.class));
    }

    @Test
    void updateCardLine_Error() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(cardEntity));
        when(mapper.toEntity(any(TransactionLineCardDTO.class))).thenReturn(cardEntity);
        when(repository.save(any(TransactionLineCard.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act & Assert
        StepVerifier.create(service.updateCardLine(transactionId, cardDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Failed to update Transaction Line Card"))
                .verify();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper).toEntity(cardDTO);
        verify(repository).save(cardEntity);
        verify(mapper, never()).toDTO(any(TransactionLineCard.class));
    }

    @Test
    void deleteCardLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(cardEntity));
        when(repository.delete(cardEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteCardLine(transactionId))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(repository).delete(cardEntity);
    }

    @Test
    void deleteCardLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteCardLine(transactionId))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Transaction Line Card not found"))
                .verify();

        verify(repository).findByTransactionId(transactionId);
        verify(repository, never()).delete(any(TransactionLineCard.class));
    }
}