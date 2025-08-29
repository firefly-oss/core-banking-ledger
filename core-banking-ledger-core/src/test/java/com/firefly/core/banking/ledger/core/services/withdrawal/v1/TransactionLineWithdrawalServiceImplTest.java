package com.firefly.core.banking.ledger.core.services.withdrawal.v1;

import com.firefly.core.banking.ledger.core.mappers.withdrawal.v1.TransactionLineWithdrawalMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.withdrawal.v1.TransactionLineWithdrawalDTO;
import com.firefly.core.banking.ledger.models.entities.withdrawal.v1.TransactionLineWithdrawal;
import com.firefly.core.banking.ledger.models.repositories.withdrawal.v1.TransactionLineWithdrawalRepository;
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
public class TransactionLineWithdrawalServiceImplTest {

    @Mock
    private TransactionLineWithdrawalRepository repository;

    @Mock
    private TransactionLineWithdrawalMapper mapper;

    @InjectMocks
    private TransactionLineWithdrawalServiceImpl service;

    private TransactionLineWithdrawalDTO withdrawalDTO;
    private TransactionLineWithdrawal withdrawalEntity;
    private final Long transactionId = 1L;
    private final Long withdrawalId = 2L;

    @BeforeEach
    void setUp() {
        // Initialize test data
        withdrawalDTO = new TransactionLineWithdrawalDTO();
        withdrawalDTO.setTransactionLineWithdrawalId(withdrawalId);
        withdrawalDTO.setTransactionId(transactionId);
        withdrawalDTO.setWithdrawalDailyAmountUsed(BigDecimal.valueOf(50.00));
        withdrawalDTO.setWithdrawalTimestamp(LocalDateTime.now());
        withdrawalDTO.setWithdrawalMethod("ATM");
        withdrawalDTO.setWithdrawalLocation("Main Street ATM");
        withdrawalDTO.setWithdrawalReference("WD123456");

        withdrawalEntity = new TransactionLineWithdrawal();
        withdrawalEntity.setTransactionLineWithdrawalId(withdrawalId);
        withdrawalEntity.setTransactionId(transactionId);
        withdrawalEntity.setWithdrawalDailyAmountUsed(BigDecimal.valueOf(50.00));
        withdrawalEntity.setWithdrawalTimestamp(LocalDateTime.now());
        withdrawalEntity.setWithdrawalMethod("ATM");
        withdrawalEntity.setWithdrawalLocation("Main Street ATM");
        withdrawalEntity.setWithdrawalReference("WD123456");
    }

    @Test
    void getWithdrawalLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(withdrawalEntity));
        when(mapper.toDTO(any(TransactionLineWithdrawal.class))).thenReturn(withdrawalDTO);

        // Act & Assert
        StepVerifier.create(service.getWithdrawalLine(transactionId))
                .expectNext(withdrawalDTO)
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper).toDTO(withdrawalEntity);
    }

    @Test
    void getWithdrawalLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getWithdrawalLine(transactionId))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Transaction Line Withdrawal not found"))
                .verify();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper, never()).toDTO(any(TransactionLineWithdrawal.class));
    }

    @Test
    void createWithdrawalLine_Success() {
        // Arrange
        when(mapper.toEntity(any(TransactionLineWithdrawalDTO.class))).thenReturn(withdrawalEntity);
        when(repository.save(any(TransactionLineWithdrawal.class))).thenReturn(Mono.just(withdrawalEntity));
        when(mapper.toDTO(any(TransactionLineWithdrawal.class))).thenReturn(withdrawalDTO);

        // Act & Assert
        StepVerifier.create(service.createWithdrawalLine(transactionId, withdrawalDTO))
                .expectNext(withdrawalDTO)
                .verifyComplete();

        verify(mapper).toEntity(withdrawalDTO);
        verify(repository).save(withdrawalEntity);
        verify(mapper).toDTO(withdrawalEntity);
    }

    @Test
    void createWithdrawalLine_Error() {
        // Arrange
        when(mapper.toEntity(any(TransactionLineWithdrawalDTO.class))).thenReturn(withdrawalEntity);
        when(repository.save(any(TransactionLineWithdrawal.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act & Assert
        StepVerifier.create(service.createWithdrawalLine(transactionId, withdrawalDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Failed to create Transaction Line Withdrawal"))
                .verify();

        verify(mapper).toEntity(withdrawalDTO);
        verify(repository).save(withdrawalEntity);
        verify(mapper, never()).toDTO(any(TransactionLineWithdrawal.class));
    }

    @Test
    void updateWithdrawalLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(withdrawalEntity));
        when(mapper.toEntity(any(TransactionLineWithdrawalDTO.class))).thenReturn(withdrawalEntity);
        when(repository.save(any(TransactionLineWithdrawal.class))).thenReturn(Mono.just(withdrawalEntity));
        when(mapper.toDTO(any(TransactionLineWithdrawal.class))).thenReturn(withdrawalDTO);

        // Act & Assert
        StepVerifier.create(service.updateWithdrawalLine(transactionId, withdrawalDTO))
                .expectNext(withdrawalDTO)
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper).toEntity(withdrawalDTO);
        verify(repository).save(withdrawalEntity);
        verify(mapper).toDTO(withdrawalEntity);
    }

    @Test
    void updateWithdrawalLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateWithdrawalLine(transactionId, withdrawalDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Failed to update Transaction Line Withdrawal"))
                .verify();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper, never()).toEntity(any(TransactionLineWithdrawalDTO.class));
        verify(repository, never()).save(any(TransactionLineWithdrawal.class));
        verify(mapper, never()).toDTO(any(TransactionLineWithdrawal.class));
    }

    @Test
    void deleteWithdrawalLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(withdrawalEntity));
        when(repository.delete(withdrawalEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteWithdrawalLine(transactionId))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(repository).delete(withdrawalEntity);
    }

    @Test
    void deleteWithdrawalLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteWithdrawalLine(transactionId))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(repository, never()).delete(any(TransactionLineWithdrawal.class));
    }
}
