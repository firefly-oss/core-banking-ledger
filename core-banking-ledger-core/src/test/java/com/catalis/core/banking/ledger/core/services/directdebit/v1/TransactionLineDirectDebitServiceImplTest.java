package com.catalis.core.banking.ledger.core.services.directdebit.v1;

import com.catalis.core.banking.ledger.core.mappers.directdebit.v1.TransactionLineDirectDebitMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.directdebit.v1.TransactionLineDirectDebitDTO;
import com.catalis.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitProcessingStatusEnum;
import com.catalis.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitSequenceTypeEnum;
import com.catalis.core.banking.ledger.interfaces.enums.directdebit.v1.DirectDebitSpanishSchemeEnum;
import com.catalis.core.banking.ledger.models.entities.directdebit.v1.TransactionLineDirectDebit;
import com.catalis.core.banking.ledger.models.repositories.directdebit.v1.TransactionLineDirectDebitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionLineDirectDebitServiceImplTest {

    @Mock
    private TransactionLineDirectDebitRepository repository;

    @Mock
    private TransactionLineDirectDebitMapper mapper;

    @InjectMocks
    private TransactionLineDirectDebitServiceImpl service;

    private TransactionLineDirectDebitDTO directDebitDTO;
    private TransactionLineDirectDebit directDebitEntity;
    private final Long transactionId = 1L;
    private final Long directDebitId = 2L;

    @BeforeEach
    void setUp() {
        // Initialize test data
        directDebitDTO = new TransactionLineDirectDebitDTO();
        directDebitDTO.setTransactionLineDirectDebitId(directDebitId);
        directDebitDTO.setTransactionId(transactionId);
        directDebitDTO.setDirectDebitMandateId("MANDATE123");
        directDebitDTO.setDirectDebitCreditorId("CREDITOR456");
        directDebitDTO.setDirectDebitReference("REF789");
        directDebitDTO.setDirectDebitSequenceType(DirectDebitSequenceTypeEnum.RCUR);
        directDebitDTO.setDirectDebitDueDate(LocalDate.now().plusDays(7));
        directDebitDTO.setDirectDebitPaymentMethod("DIRECT_DEBIT");
        directDebitDTO.setDirectDebitDebtorName("John Doe");
        directDebitDTO.setDirectDebitDebtorAddress("123 Main St");
        directDebitDTO.setDirectDebitDebtorContact("john@example.com");
        directDebitDTO.setDirectDebitProcessingStatus(DirectDebitProcessingStatusEnum.PENDING);
        directDebitDTO.setDirectDebitAuthorizationDate(LocalDateTime.now().minusDays(30));
        directDebitDTO.setDirectDebitSpanishScheme(DirectDebitSpanishSchemeEnum.CORE);

        directDebitEntity = new TransactionLineDirectDebit();
        directDebitEntity.setTransactionLineDirectDebitId(directDebitId);
        directDebitEntity.setTransactionId(transactionId);
        directDebitEntity.setDirectDebitMandateId("MANDATE123");
        directDebitEntity.setDirectDebitCreditorId("CREDITOR456");
        directDebitEntity.setDirectDebitReference("REF789");
        directDebitEntity.setDirectDebitSequenceType(DirectDebitSequenceTypeEnum.RCUR);
        directDebitEntity.setDirectDebitDueDate(LocalDate.now().plusDays(7));
        directDebitEntity.setDirectDebitPaymentMethod("DIRECT_DEBIT");
        directDebitEntity.setDirectDebitDebtorName("John Doe");
        directDebitEntity.setDirectDebitDebtorAddress("123 Main St");
        directDebitEntity.setDirectDebitDebtorContact("john@example.com");
        directDebitEntity.setDirectDebitProcessingStatus(DirectDebitProcessingStatusEnum.PENDING);
        directDebitEntity.setDirectDebitAuthorizationDate(LocalDateTime.now().minusDays(30));
        directDebitEntity.setDirectDebitSpanishScheme(DirectDebitSpanishSchemeEnum.CORE);
    }

    @Test
    void getDirectDebitLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(directDebitEntity));
        when(mapper.toDTO(any(TransactionLineDirectDebit.class))).thenReturn(directDebitDTO);

        // Act & Assert
        StepVerifier.create(service.getDirectDebitLine(transactionId))
                .expectNext(directDebitDTO)
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper).toDTO(directDebitEntity);
    }

    @Test
    void getDirectDebitLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getDirectDebitLine(transactionId))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper, never()).toDTO(any(TransactionLineDirectDebit.class));
    }

    @Test
    void createDirectDebitLine_Success() {
        // Arrange
        when(mapper.toEntity(any(TransactionLineDirectDebitDTO.class))).thenReturn(directDebitEntity);
        when(repository.save(any(TransactionLineDirectDebit.class))).thenReturn(Mono.just(directDebitEntity));
        when(mapper.toDTO(any(TransactionLineDirectDebit.class))).thenReturn(directDebitDTO);

        // Act & Assert
        StepVerifier.create(service.createDirectDebitLine(transactionId, directDebitDTO))
                .expectNext(directDebitDTO)
                .verifyComplete();

        verify(mapper).toEntity(directDebitDTO);
        verify(repository).save(directDebitEntity);
        verify(mapper).toDTO(directDebitEntity);
    }

    @Test
    void updateDirectDebitLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(directDebitEntity));
        when(mapper.toEntity(any(TransactionLineDirectDebitDTO.class))).thenReturn(directDebitEntity);
        when(repository.save(any(TransactionLineDirectDebit.class))).thenReturn(Mono.just(directDebitEntity));
        when(mapper.toDTO(any(TransactionLineDirectDebit.class))).thenReturn(directDebitDTO);

        // Act & Assert
        StepVerifier.create(service.updateDirectDebitLine(transactionId, directDebitDTO))
                .expectNext(directDebitDTO)
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper).toEntity(directDebitDTO);
        verify(repository).save(directDebitEntity);
        verify(mapper).toDTO(directDebitEntity);
    }

    @Test
    void updateDirectDebitLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateDirectDebitLine(transactionId, directDebitDTO))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper, never()).toEntity(any(TransactionLineDirectDebitDTO.class));
        verify(repository, never()).save(any(TransactionLineDirectDebit.class));
        verify(mapper, never()).toDTO(any(TransactionLineDirectDebit.class));
    }

    @Test
    void deleteDirectDebitLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(directDebitEntity));
        when(repository.delete(directDebitEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDirectDebitLine(transactionId))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(repository).delete(directDebitEntity);
    }

    @Test
    void deleteDirectDebitLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteDirectDebitLine(transactionId))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(repository, never()).delete(any(TransactionLineDirectDebit.class));
    }
}