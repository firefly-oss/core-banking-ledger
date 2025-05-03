package com.catalis.core.banking.ledger.core.services.core.v1;

import com.catalis.core.banking.ledger.core.mappers.core.v1.TransactionMapper;
import com.catalis.core.banking.ledger.core.mappers.core.v1.TransactionStatusHistoryMapper;
import com.catalis.core.banking.ledger.core.services.event.v1.EventOutboxService;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
import com.catalis.core.banking.ledger.models.entities.core.v1.Transaction;
import com.catalis.core.banking.ledger.models.entities.core.v1.TransactionStatusHistory;
import com.catalis.core.banking.ledger.models.repositories.core.v1.TransactionRepository;
import com.catalis.core.banking.ledger.models.repositories.core.v1.TransactionStatusHistoryRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest_NewMethods {

    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionStatusHistoryRepository statusHistoryRepository;

    @Mock
    private TransactionMapper mapper;

    @Mock
    private TransactionStatusHistoryMapper statusHistoryMapper;

    @Mock
    private EventOutboxService eventOutboxService;

    @InjectMocks
    private TransactionServiceImpl service;

    private TransactionDTO transactionDTO;
    private Transaction transaction;
    private TransactionStatusHistory statusHistory;
    private final Long transactionId = 1L;
    private final String externalReference = "TX-2023-12345";
    private final String reason = "Test reason";

    @BeforeEach
    void setUp() {
        // Initialize test data
        transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transactionId);
        transactionDTO.setExternalReference(externalReference);
        transactionDTO.setTotalAmount(new BigDecimal("100.00"));
        transactionDTO.setDescription("Test Transaction");
        transactionDTO.setTransactionDate(LocalDateTime.now());
        transactionDTO.setValueDate(LocalDateTime.now());
        transactionDTO.setBookingDate(LocalDateTime.now());
        transactionDTO.setTransactionType(TransactionTypeEnum.TRANSFER);
        transactionDTO.setTransactionStatus(TransactionStatusEnum.POSTED);
        transactionDTO.setCurrency("EUR");
        transactionDTO.setAccountId(100L);

        transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setExternalReference(externalReference);
        transaction.setTotalAmount(new BigDecimal("100.00"));
        transaction.setDescription("Test Transaction");
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setValueDate(LocalDateTime.now());
        transaction.setBookingDate(LocalDateTime.now());
        transaction.setTransactionType(TransactionTypeEnum.TRANSFER);
        transaction.setTransactionStatus(TransactionStatusEnum.POSTED);
        transaction.setCurrency("EUR");
        transaction.setAccountId(100L);

        statusHistory = new TransactionStatusHistory();
        statusHistory.setTransactionStatusHistoryId(1L);
        statusHistory.setTransactionId(transactionId);
        statusHistory.setStatusCode(TransactionStatusEnum.POSTED);
        statusHistory.setStatusStartDatetime(LocalDateTime.now());
        statusHistory.setReason(reason);
        statusHistory.setRegulatedReportingFlag(false);
    }

    @Test
    void updateTransactionStatus_Success() {
        // Arrange
        TransactionStatusEnum newStatus = TransactionStatusEnum.POSTED;
        transaction.setTransactionStatus(TransactionStatusEnum.PENDING); // Current status

        when(repository.findById(transactionId)).thenReturn(Mono.just(transaction));
        when(repository.save(any(Transaction.class))).thenReturn(Mono.just(transaction));
        when(statusHistoryRepository.save(any(TransactionStatusHistory.class))).thenReturn(Mono.just(statusHistory));
        when(eventOutboxService.publishEvent(
                eq("TRANSACTION"),
                eq(transactionId.toString()),
                eq("TRANSACTION_STATUS_CHANGED"),
                any(TransactionDTO.class)
        )).thenReturn(Mono.empty());
        when(mapper.toDTO(any(Transaction.class))).thenReturn(transactionDTO);

        // Act & Assert
        StepVerifier.create(service.updateTransactionStatus(transactionId, newStatus, reason))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(repository).findById(transactionId);
        verify(repository).save(transaction);
        verify(statusHistoryRepository).save(any(TransactionStatusHistory.class));
        verify(eventOutboxService).publishEvent(
                eq("TRANSACTION"),
                eq(transactionId.toString()),
                eq("TRANSACTION_STATUS_CHANGED"),
                any(TransactionDTO.class)
        );
        verify(mapper, times(2)).toDTO(transaction);
    }

    @Test
    void updateTransactionStatus_SameStatus_NoChange() {
        // Arrange
        TransactionStatusEnum currentStatus = TransactionStatusEnum.POSTED;
        transaction.setTransactionStatus(currentStatus);

        when(repository.findById(transactionId)).thenReturn(Mono.just(transaction));
        when(mapper.toDTO(any(Transaction.class))).thenReturn(transactionDTO);

        // Act & Assert
        StepVerifier.create(service.updateTransactionStatus(transactionId, currentStatus, reason))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(repository).findById(transactionId);
        verify(repository, never()).save(any(Transaction.class));
        verify(statusHistoryRepository, never()).save(any(TransactionStatusHistory.class));
        verify(eventOutboxService, never()).publishEvent(
                anyString(),
                anyString(),
                anyString(),
                any(TransactionDTO.class)
        );
        verify(mapper).toDTO(transaction);
    }

    @Test
    void updateTransactionStatus_NotFound() {
        // Arrange
        TransactionStatusEnum newStatus = TransactionStatusEnum.POSTED;
        when(repository.findById(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateTransactionStatus(transactionId, newStatus, reason))
                .verifyComplete();

        verify(repository).findById(transactionId);
        verify(repository, never()).save(any(Transaction.class));
        verify(statusHistoryRepository, never()).save(any(TransactionStatusHistory.class));
        verify(eventOutboxService, never()).publishEvent(
                anyString(),
                anyString(),
                anyString(),
                any(TransactionDTO.class)
        );
        verify(mapper, never()).toDTO(any(Transaction.class));
    }

    @Test
    void createReversalTransaction_Success() {
        // Arrange
        Transaction reversalTransaction = new Transaction();
        reversalTransaction.setTransactionId(2L);
        reversalTransaction.setTotalAmount(transaction.getTotalAmount().negate());
        reversalTransaction.setTransactionStatus(TransactionStatusEnum.PENDING);
        reversalTransaction.setRelatedTransactionId(transactionId);
        reversalTransaction.setRelationType("REVERSAL");

        TransactionDTO reversalDTO = new TransactionDTO();
        reversalDTO.setTransactionId(2L);
        reversalDTO.setTotalAmount(transactionDTO.getTotalAmount().negate());
        reversalDTO.setTransactionStatus(TransactionStatusEnum.PENDING);
        reversalDTO.setRelatedTransactionId(transactionId);
        reversalDTO.setRelationType("REVERSAL");

        when(repository.findById(transactionId)).thenReturn(Mono.just(transaction));
        when(repository.save(any(Transaction.class))).thenReturn(Mono.just(reversalTransaction));
        when(statusHistoryRepository.save(any(TransactionStatusHistory.class))).thenReturn(Mono.just(statusHistory));
        when(eventOutboxService.publishEvent(
                eq("TRANSACTION"),
                anyString(),
                eq("REVERSAL_TRANSACTION_CREATED"),
                any(TransactionDTO.class)
        )).thenReturn(Mono.empty());
        when(mapper.toDTO(reversalTransaction)).thenReturn(reversalDTO);

        // Act & Assert
        StepVerifier.create(service.createReversalTransaction(transactionId, reason))
                .expectNext(reversalDTO)
                .verifyComplete();

        verify(repository).findById(transactionId);
        verify(repository).save(any(Transaction.class));
        verify(statusHistoryRepository).save(any(TransactionStatusHistory.class));
        verify(eventOutboxService).publishEvent(
                eq("TRANSACTION"),
                anyString(),
                eq("REVERSAL_TRANSACTION_CREATED"),
                any(TransactionDTO.class)
        );
        verify(mapper, times(2)).toDTO(reversalTransaction);
    }

    @Test
    void createReversalTransaction_OriginalNotFound() {
        // Arrange
        when(repository.findById(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.createReversalTransaction(transactionId, reason))
                .verifyComplete();

        verify(repository).findById(transactionId);
        verify(repository, never()).save(any(Transaction.class));
        verify(statusHistoryRepository, never()).save(any(TransactionStatusHistory.class));
        verify(eventOutboxService, never()).publishEvent(
                anyString(),
                anyString(),
                anyString(),
                any(TransactionDTO.class)
        );
        verify(mapper, never()).toDTO(any(Transaction.class));
    }

    @Test
    void findByExternalReference_Success() {
        // Arrange
        when(repository.findByExternalReference(externalReference)).thenReturn(Mono.just(transaction));
        when(mapper.toDTO(transaction)).thenReturn(transactionDTO);

        // Act & Assert
        StepVerifier.create(service.findByExternalReference(externalReference))
                .expectNext(transactionDTO)
                .verifyComplete();

        verify(repository).findByExternalReference(externalReference);
        verify(mapper).toDTO(transaction);
    }

    @Test
    void findByExternalReference_NotFound() {
        // Arrange
        when(repository.findByExternalReference(externalReference)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.findByExternalReference(externalReference))
                .verifyComplete();

        verify(repository).findByExternalReference(externalReference);
        verify(mapper, never()).toDTO(any(Transaction.class));
    }
}
