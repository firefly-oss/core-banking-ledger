package com.catalis.core.banking.ledger.core.services.statement.v1;

import com.catalis.common.core.filters.FilterRequest;
import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.core.banking.ledger.core.mappers.core.v1.TransactionMapper;
import com.catalis.core.banking.ledger.core.mappers.statement.v1.StatementMapper;
import com.catalis.core.banking.ledger.core.services.core.v1.TransactionService;
import com.catalis.core.banking.ledger.core.services.event.v1.EventOutboxService;
import com.catalis.core.banking.ledger.interfaces.dtos.core.v1.TransactionDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.statement.v1.StatementDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.statement.v1.StatementMetadataDTO;
import com.catalis.core.banking.ledger.interfaces.dtos.statement.v1.StatementRequestDTO;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionStatusEnum;
import com.catalis.core.banking.ledger.interfaces.enums.core.v1.TransactionTypeEnum;
import com.catalis.core.banking.ledger.interfaces.enums.statement.v1.StatementPeriodEnum;
import com.catalis.core.banking.ledger.models.entities.statement.v1.Statement;
import com.catalis.core.banking.ledger.models.repositories.statement.v1.StatementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatementGenerationTest {

    @Mock
    private StatementRepository repository;

    @Mock
    private StatementMapper mapper;

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private EventOutboxService eventOutboxService;

    @InjectMocks
    private StatementServiceImpl service;

    private StatementRequestDTO requestDTO;
    private Long accountId;
    private Long accountSpaceId;
    private List<TransactionDTO> mockTransactions;

    @BeforeEach
    void setUp() {
        accountId = 123L;
        accountSpaceId = 456L;

        // Create a statement request
        requestDTO = new StatementRequestDTO();
        requestDTO.setPeriodType(StatementPeriodEnum.MONTHLY);
        requestDTO.setMonth(6);
        requestDTO.setYear(2023);
        requestDTO.setIncludePending(true);
        requestDTO.setIncludeDetails(true);

        // Create mock transactions
        mockTransactions = new ArrayList<>();
        
        // Transaction 1
        TransactionDTO transaction1 = new TransactionDTO();
        transaction1.setTransactionId(1001L);
        transaction1.setAccountId(accountId);
        transaction1.setTransactionDate(LocalDateTime.of(2023, 6, 5, 10, 0));
        transaction1.setValueDate(LocalDateTime.of(2023, 6, 5, 10, 0));
        transaction1.setBookingDate(LocalDateTime.of(2023, 6, 5, 10, 5));
        transaction1.setTransactionType(TransactionTypeEnum.DEPOSIT);
        transaction1.setTransactionStatus(TransactionStatusEnum.COMPLETED);
        transaction1.setTotalAmount(new BigDecimal("500.00"));
        transaction1.setCurrency("EUR");
        transaction1.setDescription("Salary deposit");
        transaction1.setInitiatingParty("Employer Inc.");
        transaction1.setExternalReference("REF-001");
        mockTransactions.add(transaction1);
        
        // Transaction 2
        TransactionDTO transaction2 = new TransactionDTO();
        transaction2.setTransactionId(1002L);
        transaction2.setAccountId(accountId);
        transaction2.setTransactionDate(LocalDateTime.of(2023, 6, 10, 14, 30));
        transaction2.setValueDate(LocalDateTime.of(2023, 6, 10, 14, 30));
        transaction2.setBookingDate(LocalDateTime.of(2023, 6, 10, 14, 35));
        transaction2.setTransactionType(TransactionTypeEnum.PAYMENT);
        transaction2.setTransactionStatus(TransactionStatusEnum.COMPLETED);
        transaction2.setTotalAmount(new BigDecimal("-100.00"));
        transaction2.setCurrency("EUR");
        transaction2.setDescription("Electricity bill payment");
        transaction2.setInitiatingParty("John Doe");
        transaction2.setExternalReference("REF-002");
        mockTransactions.add(transaction2);
        
        // Transaction 3
        TransactionDTO transaction3 = new TransactionDTO();
        transaction3.setTransactionId(1003L);
        transaction3.setAccountId(accountId);
        transaction3.setTransactionDate(LocalDateTime.of(2023, 6, 15, 9, 0));
        transaction3.setValueDate(LocalDateTime.of(2023, 6, 15, 9, 0));
        transaction3.setBookingDate(LocalDateTime.of(2023, 6, 15, 9, 5));
        transaction3.setTransactionType(TransactionTypeEnum.TRANSFER);
        transaction3.setTransactionStatus(TransactionStatusEnum.COMPLETED);
        transaction3.setTotalAmount(new BigDecimal("-50.00"));
        transaction3.setCurrency("EUR");
        transaction3.setDescription("Transfer to savings");
        transaction3.setInitiatingParty("John Doe");
        transaction3.setExternalReference("REF-003");
        mockTransactions.add(transaction3);
    }

    @Test
    void generateAccountStatement_Success() {
        // Arrange
        PaginationResponse<TransactionDTO> paginationResponse = new PaginationResponse<>(
                mockTransactions, 0, mockTransactions.size(), mockTransactions.size()
        );
        
        when(transactionService.filterTransactions(any(FilterRequest.class)))
                .thenReturn(Mono.just(paginationResponse));
        
        Statement savedStatement = new Statement();
        savedStatement.setStatementId(789L);
        savedStatement.setAccountId(accountId);
        savedStatement.setPeriodType(StatementPeriodEnum.MONTHLY);
        savedStatement.setStartDate(LocalDate.of(2023, 6, 1));
        savedStatement.setEndDate(LocalDate.of(2023, 6, 30));
        savedStatement.setGenerationDate(LocalDateTime.now());
        savedStatement.setTransactionCount(mockTransactions.size());
        savedStatement.setIncludedPending(true);
        savedStatement.setIncludedDetails(true);
        
        when(mapper.toEntity(any(StatementMetadataDTO.class))).thenReturn(new Statement());
        when(repository.save(any(Statement.class))).thenReturn(Mono.just(savedStatement));
        when(eventOutboxService.publishEvent(anyString(), anyString(), anyString(), any()))
                .thenReturn(Mono.empty());
        
        // Act
        Mono<StatementDTO> result = service.generateAccountStatement(accountId, requestDTO);
        
        // Assert
        StepVerifier.create(result)
                .assertNext(statementDTO -> {
                    assertNotNull(statementDTO);
                    assertNotNull(statementDTO.getMetadata());
                    assertEquals(accountId, statementDTO.getMetadata().getAccountId());
                    assertEquals(StatementPeriodEnum.MONTHLY, statementDTO.getMetadata().getPeriodType());
                    assertEquals(LocalDate.of(2023, 6, 1), statementDTO.getMetadata().getStartDate());
                    assertEquals(LocalDate.of(2023, 6, 30), statementDTO.getMetadata().getEndDate());
                    assertEquals(789L, statementDTO.getMetadata().getStatementId());
                    assertEquals(3, statementDTO.getEntries().size());
                    assertEquals(new BigDecimal("350.00"), statementDTO.getClosingBalance());
                    assertEquals(new BigDecimal("500.00"), statementDTO.getTotalCredits());
                    assertEquals(new BigDecimal("150.00"), statementDTO.getTotalDebits());
                })
                .verifyComplete();
        
        // Verify interactions
        verify(transactionService).filterTransactions(any(FilterRequest.class));
        verify(mapper).toEntity(any(StatementMetadataDTO.class));
        verify(repository).save(any(Statement.class));
        verify(eventOutboxService).publishEvent(eq("STATEMENT"), anyString(), eq("ACCOUNT_STATEMENT_GENERATED"), any());
    }

    @Test
    void generateAccountSpaceStatement_Success() {
        // Arrange
        // Update mock transactions to use accountSpaceId instead of accountId
        mockTransactions.forEach(tx -> {
            tx.setAccountId(null);
            tx.setAccountSpaceId(accountSpaceId);
        });
        
        PaginationResponse<TransactionDTO> paginationResponse = new PaginationResponse<>(
                mockTransactions, 0, mockTransactions.size(), mockTransactions.size()
        );
        
        when(transactionService.filterTransactions(any(FilterRequest.class)))
                .thenReturn(Mono.just(paginationResponse));
        
        Statement savedStatement = new Statement();
        savedStatement.setStatementId(789L);
        savedStatement.setAccountSpaceId(accountSpaceId);
        savedStatement.setPeriodType(StatementPeriodEnum.MONTHLY);
        savedStatement.setStartDate(LocalDate.of(2023, 6, 1));
        savedStatement.setEndDate(LocalDate.of(2023, 6, 30));
        savedStatement.setGenerationDate(LocalDateTime.now());
        savedStatement.setTransactionCount(mockTransactions.size());
        savedStatement.setIncludedPending(true);
        savedStatement.setIncludedDetails(true);
        
        when(mapper.toEntity(any(StatementMetadataDTO.class))).thenReturn(new Statement());
        when(repository.save(any(Statement.class))).thenReturn(Mono.just(savedStatement));
        when(eventOutboxService.publishEvent(anyString(), anyString(), anyString(), any()))
                .thenReturn(Mono.empty());
        
        // Act
        Mono<StatementDTO> result = service.generateAccountSpaceStatement(accountSpaceId, requestDTO);
        
        // Assert
        StepVerifier.create(result)
                .assertNext(statementDTO -> {
                    assertNotNull(statementDTO);
                    assertNotNull(statementDTO.getMetadata());
                    assertEquals(accountSpaceId, statementDTO.getMetadata().getAccountSpaceId());
                    assertEquals(StatementPeriodEnum.MONTHLY, statementDTO.getMetadata().getPeriodType());
                    assertEquals(LocalDate.of(2023, 6, 1), statementDTO.getMetadata().getStartDate());
                    assertEquals(LocalDate.of(2023, 6, 30), statementDTO.getMetadata().getEndDate());
                    assertEquals(789L, statementDTO.getMetadata().getStatementId());
                    assertEquals(3, statementDTO.getEntries().size());
                    assertEquals(new BigDecimal("350.00"), statementDTO.getClosingBalance());
                    assertEquals(new BigDecimal("500.00"), statementDTO.getTotalCredits());
                    assertEquals(new BigDecimal("150.00"), statementDTO.getTotalDebits());
                })
                .verifyComplete();
        
        // Verify interactions
        verify(transactionService).filterTransactions(any(FilterRequest.class));
        verify(mapper).toEntity(any(StatementMetadataDTO.class));
        verify(repository).save(any(Statement.class));
        verify(eventOutboxService).publishEvent(eq("STATEMENT"), anyString(), eq("ACCOUNT_SPACE_STATEMENT_GENERATED"), any());
        
        // Verify that the filter used accountSpaceId
        ArgumentCaptor<FilterRequest> filterCaptor = ArgumentCaptor.forClass(FilterRequest.class);
        verify(transactionService).filterTransactions(filterCaptor.capture());
        FilterRequest<TransactionDTO> capturedFilter = filterCaptor.getValue();
        assertEquals(accountSpaceId, capturedFilter.getFilter().getAccountSpaceId());
    }
}
