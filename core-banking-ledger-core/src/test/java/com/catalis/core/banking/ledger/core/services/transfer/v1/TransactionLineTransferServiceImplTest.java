package com.catalis.core.banking.ledger.core.services.transfer.v1;

import com.catalis.core.banking.ledger.core.mappers.transfer.v1.TransactionLineTransferMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.transfer.v1.TransactionLineTransferDTO;
import com.catalis.core.banking.ledger.models.entities.transfer.v1.TransactionLineTransfer;
import com.catalis.core.banking.ledger.models.repositories.transfer.v1.TransactionLineTransferRepository;
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
public class TransactionLineTransferServiceImplTest {

    @Mock
    private TransactionLineTransferRepository repository;

    @Mock
    private TransactionLineTransferMapper mapper;

    @InjectMocks
    private TransactionLineTransferServiceImpl service;

    private TransactionLineTransferDTO transferDTO;
    private TransactionLineTransfer transferEntity;
    private final Long transactionId = 1L;
    private final Long transferId = 2L;

    @BeforeEach
    void setUp() {
        // Initialize test data
        transferDTO = new TransactionLineTransferDTO();
        transferDTO.setTransactionLineTransferId(transferId);
        transferDTO.setTransactionId(transactionId);
        transferDTO.setTransferSourceAccountId(101L);
        transferDTO.setTransferDestinationAccountId(102L);
        transferDTO.setTransferFeeAmount(BigDecimal.valueOf(100.00));
        transferDTO.setTransferTimestamp(LocalDateTime.now());
        transferDTO.setTransferPurpose("Test Transfer");
        transferDTO.setTransferReference("REF123456");

        transferEntity = new TransactionLineTransfer();
        transferEntity.setTransactionLineTransferId(transferId);
        transferEntity.setTransactionId(transactionId);
        transferEntity.setTransferSourceAccountId(101L);
        transferEntity.setTransferDestinationAccountId(102L);
        transferEntity.setTransferFeeAmount(BigDecimal.valueOf(100.00));
        transferEntity.setTransferTimestamp(LocalDateTime.now());
        transferEntity.setTransferPurpose("Test Transfer");
        transferEntity.setTransferReference("REF123456");
    }

    @Test
    void getTransferLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(transferEntity));
        when(mapper.toDTO(any(TransactionLineTransfer.class))).thenReturn(transferDTO);

        // Act & Assert
        StepVerifier.create(service.getTransferLine(transactionId))
                .expectNext(transferDTO)
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper).toDTO(transferEntity);
    }

    @Test
    void getTransferLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getTransferLine(transactionId))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Transaction Line Transfer not found"))
                .verify();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper, never()).toDTO(any(TransactionLineTransfer.class));
    }

    @Test
    void createTransferLine_Success() {
        // Arrange
        when(mapper.toEntity(any(TransactionLineTransferDTO.class))).thenReturn(transferEntity);
        when(repository.save(any(TransactionLineTransfer.class))).thenReturn(Mono.just(transferEntity));
        when(mapper.toDTO(any(TransactionLineTransfer.class))).thenReturn(transferDTO);

        // Act & Assert
        StepVerifier.create(service.createTransferLine(transactionId, transferDTO))
                .expectNext(transferDTO)
                .verifyComplete();

        verify(mapper).toEntity(transferDTO);
        verify(repository).save(transferEntity);
        verify(mapper).toDTO(transferEntity);
    }

    @Test
    void createTransferLine_Error() {
        // Arrange
        when(mapper.toEntity(any(TransactionLineTransferDTO.class))).thenReturn(transferEntity);
        when(repository.save(any(TransactionLineTransfer.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act & Assert
        StepVerifier.create(service.createTransferLine(transactionId, transferDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Failed to create Transaction Line Transfer"))
                .verify();

        verify(mapper).toEntity(transferDTO);
        verify(repository).save(transferEntity);
        verify(mapper, never()).toDTO(any(TransactionLineTransfer.class));
    }

    @Test
    void updateTransferLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(transferEntity));
        when(mapper.toEntity(any(TransactionLineTransferDTO.class))).thenReturn(transferEntity);
        when(repository.save(any(TransactionLineTransfer.class))).thenReturn(Mono.just(transferEntity));
        when(mapper.toDTO(any(TransactionLineTransfer.class))).thenReturn(transferDTO);

        // Act & Assert
        StepVerifier.create(service.updateTransferLine(transactionId, transferDTO))
                .expectNext(transferDTO)
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper).toEntity(transferDTO);
        verify(repository).save(transferEntity);
        verify(mapper).toDTO(transferEntity);
    }

    @Test
    void updateTransferLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateTransferLine(transactionId, transferDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Failed to update Transaction Line Transfer"))
                .verify();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper, never()).toEntity(any(TransactionLineTransferDTO.class));
        verify(repository, never()).save(any(TransactionLineTransfer.class));
        verify(mapper, never()).toDTO(any(TransactionLineTransfer.class));
    }

    @Test
    void deleteTransferLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(transferEntity));
        when(repository.delete(transferEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteTransferLine(transactionId))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(repository).delete(transferEntity);
    }

    @Test
    void deleteTransferLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteTransferLine(transactionId))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(repository, never()).delete(any(TransactionLineTransfer.class));
    }
}
