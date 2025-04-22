package com.catalis.core.banking.ledger.core.services.standingorder.v1;

import com.catalis.core.banking.ledger.core.mappers.standingorder.v1.TransactionLineStandingOrderMapper;
import com.catalis.core.banking.ledger.interfaces.dtos.standingorder.v1.TransactionLineStandingOrderDTO;
import com.catalis.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderFrequencyEnum;
import com.catalis.core.banking.ledger.interfaces.enums.standingorder.v1.StandingOrderStatusEnum;
import com.catalis.core.banking.ledger.models.entities.standingorder.v1.TransactionLineStandingOrder;
import com.catalis.core.banking.ledger.models.repositories.standingorder.v1.TransactionLineStandingOrderRepository;
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
public class TransactionLineStandingOrderServiceImplTest {

    @Mock
    private TransactionLineStandingOrderRepository repository;

    @Mock
    private TransactionLineStandingOrderMapper mapper;

    @InjectMocks
    private TransactionLineStandingOrderServiceImpl service;

    private TransactionLineStandingOrderDTO standingOrderDTO;
    private TransactionLineStandingOrder standingOrderEntity;
    private final Long transactionId = 1L;
    private final Long standingOrderId = 2L;

    @BeforeEach
    void setUp() {
        // Initialize test data
        standingOrderDTO = new TransactionLineStandingOrderDTO();
        standingOrderDTO.setTransactionLineStandingOrderId(standingOrderId);
        standingOrderDTO.setTransactionId(transactionId);
        standingOrderDTO.setStandingOrderId("SO123456");
        standingOrderDTO.setStandingOrderFrequency(StandingOrderFrequencyEnum.MONTHLY);
        standingOrderDTO.setStandingOrderStartDate(LocalDate.now());
        standingOrderDTO.setStandingOrderEndDate(LocalDate.now().plusYears(1));
        standingOrderDTO.setStandingOrderNextExecutionDate(LocalDate.now().plusMonths(1));
        standingOrderDTO.setStandingOrderReference("Monthly Rent");
        standingOrderDTO.setStandingOrderRecipientName("Landlord Inc.");
        standingOrderDTO.setStandingOrderRecipientIban("ES9121000418450200051332");
        standingOrderDTO.setStandingOrderRecipientBic("CAIXESBBXXX");
        standingOrderDTO.setStandingOrderPurpose("Rent payment");
        standingOrderDTO.setStandingOrderStatus(StandingOrderStatusEnum.ACTIVE);
        standingOrderDTO.setStandingOrderNotes("Apartment 3B");
        standingOrderDTO.setStandingOrderLastExecutionDate(LocalDate.now().minusMonths(1));
        standingOrderDTO.setStandingOrderTotalExecutions(12);
        standingOrderDTO.setStandingOrderCreatedBy("user123");
        standingOrderDTO.setStandingOrderUpdatedBy("user123");
        standingOrderDTO.setStandingOrderCreationTimestamp(LocalDateTime.now().minusMonths(12));
        standingOrderDTO.setStandingOrderUpdateTimestamp(LocalDateTime.now());
        standingOrderDTO.setStandingOrderSpanishTaxFlag(false);

        standingOrderEntity = new TransactionLineStandingOrder();
        standingOrderEntity.setTransactionLineStandingOrderId(standingOrderId);
        standingOrderEntity.setTransactionId(transactionId);
        standingOrderEntity.setStandingOrderId("SO123456");
        standingOrderEntity.setStandingOrderFrequency(StandingOrderFrequencyEnum.MONTHLY);
        standingOrderEntity.setStandingOrderStartDate(LocalDate.now());
        standingOrderEntity.setStandingOrderEndDate(LocalDate.now().plusYears(1));
        standingOrderEntity.setStandingOrderNextExecutionDate(LocalDate.now().plusMonths(1));
        standingOrderEntity.setStandingOrderReference("Monthly Rent");
        standingOrderEntity.setStandingOrderRecipientName("Landlord Inc.");
        standingOrderEntity.setStandingOrderRecipientIban("ES9121000418450200051332");
        standingOrderEntity.setStandingOrderRecipientBic("CAIXESBBXXX");
        standingOrderEntity.setStandingOrderPurpose("Rent payment");
        standingOrderEntity.setStandingOrderStatus(StandingOrderStatusEnum.ACTIVE);
        standingOrderEntity.setStandingOrderNotes("Apartment 3B");
        standingOrderEntity.setStandingOrderLastExecutionDate(LocalDate.now().minusMonths(1));
        standingOrderEntity.setStandingOrderTotalExecutions(12);
        standingOrderEntity.setStandingOrderCreatedBy("user123");
        standingOrderEntity.setStandingOrderUpdatedBy("user123");
        standingOrderEntity.setStandingOrderCreationTimestamp(LocalDateTime.now().minusMonths(12));
        standingOrderEntity.setStandingOrderUpdateTimestamp(LocalDateTime.now());
        standingOrderEntity.setStandingOrderSpanishTaxFlag(false);
    }

    @Test
    void getStandingOrderLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(standingOrderEntity));
        when(mapper.toDTO(any(TransactionLineStandingOrder.class))).thenReturn(standingOrderDTO);

        // Act & Assert
        StepVerifier.create(service.getStandingOrderLine(transactionId))
                .expectNext(standingOrderDTO)
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper).toDTO(standingOrderEntity);
    }

    @Test
    void getStandingOrderLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getStandingOrderLine(transactionId))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper, never()).toDTO(any(TransactionLineStandingOrder.class));
    }

    @Test
    void createStandingOrderLine_Success() {
        // Arrange
        when(mapper.toEntity(any(TransactionLineStandingOrderDTO.class))).thenReturn(standingOrderEntity);
        when(repository.save(any(TransactionLineStandingOrder.class))).thenReturn(Mono.just(standingOrderEntity));
        when(mapper.toDTO(any(TransactionLineStandingOrder.class))).thenReturn(standingOrderDTO);

        // Act & Assert
        StepVerifier.create(service.createStandingOrderLine(transactionId, standingOrderDTO))
                .expectNext(standingOrderDTO)
                .verifyComplete();

        verify(mapper).toEntity(standingOrderDTO);
        verify(repository).save(standingOrderEntity);
        verify(mapper).toDTO(standingOrderEntity);
    }

    @Test
    void updateStandingOrderLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(standingOrderEntity));
        when(mapper.toEntity(any(TransactionLineStandingOrderDTO.class))).thenReturn(standingOrderEntity);
        when(repository.save(any(TransactionLineStandingOrder.class))).thenReturn(Mono.just(standingOrderEntity));
        when(mapper.toDTO(any(TransactionLineStandingOrder.class))).thenReturn(standingOrderDTO);

        // Act & Assert
        StepVerifier.create(service.updateStandingOrderLine(transactionId, standingOrderDTO))
                .expectNext(standingOrderDTO)
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper).toEntity(standingOrderDTO);
        verify(repository).save(standingOrderEntity);
        verify(mapper).toDTO(standingOrderEntity);
    }

    @Test
    void updateStandingOrderLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateStandingOrderLine(transactionId, standingOrderDTO))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(mapper, never()).toEntity(any(TransactionLineStandingOrderDTO.class));
        verify(repository, never()).save(any(TransactionLineStandingOrder.class));
        verify(mapper, never()).toDTO(any(TransactionLineStandingOrder.class));
    }

    @Test
    void deleteStandingOrderLine_Success() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.just(standingOrderEntity));
        when(repository.delete(standingOrderEntity)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteStandingOrderLine(transactionId))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(repository).delete(standingOrderEntity);
    }

    @Test
    void deleteStandingOrderLine_NotFound() {
        // Arrange
        when(repository.findByTransactionId(transactionId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteStandingOrderLine(transactionId))
                .verifyComplete();

        verify(repository).findByTransactionId(transactionId);
        verify(repository, never()).delete(any(TransactionLineStandingOrder.class));
    }
}