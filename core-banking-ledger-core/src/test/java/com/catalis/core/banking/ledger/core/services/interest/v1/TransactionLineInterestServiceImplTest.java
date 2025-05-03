package com.catalis.core.banking.ledger.core.services.interest.v1;

import com.catalis.core.banking.ledger.core.mappers.interest.v1.TransactionLineInterestMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.interest.v1.TransactionLineInterestDTO;
import com.catalis.core.banking.ledger.models.entities.interest.v1.TransactionLineInterest;
import com.catalis.core.banking.ledger.models.repositories.interest.v1.TransactionLineInterestRepository;
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
public class TransactionLineInterestServiceImplTest {

    @Mock
    private TransactionLineInterestRepository repository;

    @Mock
    private TransactionLineInterestMapper mapper;

    @InjectMocks
    private TransactionLineInterestServiceImpl service;

    private TransactionLineInterestDTO interestDTO;
    private TransactionLineInterest interestEntity;
    private final Long transactionId = 1L;
    private final Long interestId = 2L;

    @BeforeEach
    void setUp() {
        // Initialize test data
        interestDTO = new TransactionLineInterestDTO();
        interestDTO.setTransactionLineInterestId(interestId);
        interestDTO.setTransactionId(transactionId);
        interestDTO.setInterestNetAmount(BigDecimal.valueOf(5.25));
        interestDTO.setInterestRatePercentage(BigDecimal.valueOf(0.05));
        interestDTO.setInterestType("SAVINGS_INTEREST");
        interestDTO.setInterestTimestamp(LocalDateTime.now());
        interestDTO.setInterestAccrualStartDate(LocalDateTime.now().minusMonths(1).toLocalDate());
        interestDTO.setInterestAccrualEndDate(LocalDateTime.now().toLocalDate());

        interestEntity = new TransactionLineInterest();
        interestEntity.setTransactionLineInterestId(interestId);
        interestEntity.setTransactionId(transactionId);
        interestEntity.setInterestNetAmount(BigDecimal.valueOf(5.25));
        interestEntity.setInterestRatePercentage(BigDecimal.valueOf(0.05));
        interestEntity.setInterestType("SAVINGS_INTEREST");
        interestEntity.setInterestTimestamp(LocalDateTime.now());
        interestEntity.setInterestAccrualStartDate(LocalDateTime.now().minusMonths(1).toLocalDate());
        interestEntity.setInterestAccrualEndDate(LocalDateTime.now().toLocalDate());
    }

    @Test
    void getInterestLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(interestEntity));
        when(mapper.toDTO(any(TransactionLineInterest.class))).thenReturn(interestDTO);

        // Act & Assert
        StepVerifier.create(service.getInterestLine(transactionId))
                .expectNext(interestDTO)
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper).toDTO(interestEntity);
    }

    @Test
    void getInterestLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getInterestLine(transactionId))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Transaction Line Interest not found"))
                .verify();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper, never()).toDTO(any(TransactionLineInterest.class));
    }

    @Test
    void createInterestLine_Success() {
        // Arrange
        when(mapper.toEntity(any(TransactionLineInterestDTO.class))).thenReturn(interestEntity);
        when(repository.save(any(TransactionLineInterest.class))).thenReturn(Mono.just(interestEntity));
        when(mapper.toDTO(any(TransactionLineInterest.class))).thenReturn(interestDTO);

        // Act & Assert
        StepVerifier.create(service.createInterestLine(transactionId, interestDTO))
                .expectNext(interestDTO)
                .verifyComplete();

        verify(mapper).toEntity(interestDTO);
        verify(repository).save(interestEntity);
        verify(mapper).toDTO(interestEntity);
    }

    @Test
    void createInterestLine_Error() {
        // Arrange
        when(mapper.toEntity(any(TransactionLineInterestDTO.class))).thenReturn(interestEntity);
        when(repository.save(any(TransactionLineInterest.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act & Assert
        StepVerifier.create(service.createInterestLine(transactionId, interestDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Failed to create Transaction Line Interest"))
                .verify();

        verify(mapper).toEntity(interestDTO);
        verify(repository).save(interestEntity);
        verify(mapper, never()).toDTO(any(TransactionLineInterest.class));
    }

    @Test
    void updateInterestLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(interestEntity));
        when(mapper.toEntity(any(TransactionLineInterestDTO.class))).thenReturn(interestEntity);
        when(repository.save(any(TransactionLineInterest.class))).thenReturn(Mono.just(interestEntity));
        when(mapper.toDTO(any(TransactionLineInterest.class))).thenReturn(interestDTO);

        // Act & Assert
        StepVerifier.create(service.updateInterestLine(transactionId, interestDTO))
                .expectNext(interestDTO)
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper).toEntity(interestDTO);
        verify(repository).save(interestEntity);
        verify(mapper).toDTO(interestEntity);
    }

    @Test
    void updateInterestLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateInterestLine(transactionId, interestDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Failed to update Transaction Line Interest"))
                .verify();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper, never()).toEntity(any(TransactionLineInterestDTO.class));
        verify(repository, never()).save(any(TransactionLineInterest.class));
        verify(mapper, never()).toDTO(any(TransactionLineInterest.class));
    }

    @Test
    void deleteInterestLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(interestEntity));
        when(repository.delete(interestEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteInterestLine(transactionId))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(repository).delete(interestEntity);
    }

    @Test
    void deleteInterestLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteInterestLine(transactionId))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(repository, never()).delete(any(TransactionLineInterest.class));
    }
}
