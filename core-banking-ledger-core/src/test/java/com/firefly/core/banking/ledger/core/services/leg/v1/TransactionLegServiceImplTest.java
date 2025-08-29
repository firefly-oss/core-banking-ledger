package com.firefly.core.banking.ledger.core.services.leg.v1;

import com.firefly.common.core.queries.PaginationRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.common.core.queries.PaginationUtils;
import com.firefly.core.banking.ledger.core.mappers.leg.v1.TransactionLegMapper;
import com.firefly.core.banking.ledger.interfaces.dtos.leg.v1.TransactionLegDTO;
import com.firefly.core.banking.ledger.models.entities.leg.v1.TransactionLeg;
import com.firefly.core.banking.ledger.models.repositories.leg.v1.TransactionLegRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionLegServiceImplTest {

    @Mock
    private TransactionLegRepository repository;

    @Mock
    private TransactionLegMapper mapper;

    @InjectMocks
    private TransactionLegServiceImpl service;

    private TransactionLegDTO legDTO;
    private TransactionLeg legEntity;
    private final Long transactionId = 1L;
    private final Long legId = 1L;
    private final Long accountId = 100L;
    private final LocalDateTime startDate = LocalDateTime.now().minusDays(30);
    private final LocalDateTime endDate = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Initialize test data
        legDTO = new TransactionLegDTO();
        legDTO.setTransactionLegId(legId);
        legDTO.setTransactionId(transactionId);
        legDTO.setAccountId(accountId);
        legDTO.setLegType("DEBIT");
        legDTO.setAmount(new BigDecimal("100.00"));
        legDTO.setCurrency("EUR");
        legDTO.setDescription("Test leg");
        legDTO.setValueDate(LocalDateTime.now());
        legDTO.setBookingDate(LocalDateTime.now());

        legEntity = new TransactionLeg();
        legEntity.setTransactionLegId(legId);
        legEntity.setTransactionId(transactionId);
        legEntity.setAccountId(accountId);
        legEntity.setLegType("DEBIT");
        legEntity.setAmount(new BigDecimal("100.00"));
        legEntity.setCurrency("EUR");
        legEntity.setDescription("Test leg");
        legEntity.setValueDate(LocalDateTime.now());
        legEntity.setBookingDate(LocalDateTime.now());
    }

    @Test
    void createTransactionLeg_Success() {
        // Arrange
        when(mapper.toEntity(any(TransactionLegDTO.class))).thenReturn(legEntity);
        when(repository.save(any(TransactionLeg.class))).thenReturn(Mono.just(legEntity));
        when(mapper.toDTO(any(TransactionLeg.class))).thenReturn(legDTO);

        // Act & Assert
        StepVerifier.create(service.createTransactionLeg(transactionId, legDTO))
                .expectNext(legDTO)
                .verifyComplete();

        verify(mapper).toEntity(legDTO);
        verify(repository).save(legEntity);
        verify(mapper).toDTO(legEntity);
    }

    @Test
    void getTransactionLeg_Success() {
        // Arrange
        when(repository.findById(legId)).thenReturn(Mono.just(legEntity));
        when(mapper.toDTO(any(TransactionLeg.class))).thenReturn(legDTO);

        // Act & Assert
        StepVerifier.create(service.getTransactionLeg(transactionId, legId))
                .expectNext(legDTO)
                .verifyComplete();

        verify(repository).findById(legId);
        verify(mapper).toDTO(legEntity);
    }

    @Test
    void getTransactionLeg_WrongTransactionId_EmptyResult() {
        // Arrange
        legEntity.setTransactionId(999L); // Different transaction ID
        when(repository.findById(legId)).thenReturn(Mono.just(legEntity));

        // Act & Assert
        StepVerifier.create(service.getTransactionLeg(transactionId, legId))
                .verifyComplete(); // Empty result

        verify(repository).findById(legId);
        verify(mapper, never()).toDTO(any(TransactionLeg.class));
    }

    @Test
    void listTransactionLegs_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest(0, 10, null, null);
        PaginationResponse<TransactionLegDTO> expectedResponse = new PaginationResponse<>(
                List.of(legDTO), 0, 10, 1
        );

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = Mockito.mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(),
                    any(),
                    any()
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listTransactionLegs(transactionId, paginationRequest))
                    .expectNext(expectedResponse)
                    .verifyComplete();
        }
    }

    @Test
    void listAccountLegs_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest(0, 10, null, null);
        PaginationResponse<TransactionLegDTO> expectedResponse = new PaginationResponse<>(
                List.of(legDTO), 0, 10, 1
        );

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = Mockito.mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(),
                    any(),
                    any()
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listAccountLegs(accountId, paginationRequest))
                    .expectNext(expectedResponse)
                    .verifyComplete();
        }
    }

    @Test
    void listAccountLegsByDateRange_Success() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest(0, 10, null, null);
        PaginationResponse<TransactionLegDTO> expectedResponse = new PaginationResponse<>(
                List.of(legDTO), 0, 10, 1
        );

        try (MockedStatic<PaginationUtils> paginationUtilsMocked = Mockito.mockStatic(PaginationUtils.class)) {
            paginationUtilsMocked.when(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(),
                    any(),
                    any()
            )).thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(service.listAccountLegsByDateRange(accountId, startDate, endDate, paginationRequest))
                    .expectNext(expectedResponse)
                    .verifyComplete();
        }
    }
}
