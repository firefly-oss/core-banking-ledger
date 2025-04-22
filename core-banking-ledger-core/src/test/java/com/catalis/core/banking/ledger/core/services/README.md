# Core Banking Ledger Service Tests

This directory contains the test suite for the Core Banking Ledger service implementations. The tests are organized to match the package structure of the service implementations.

## Overview

The test suite provides comprehensive coverage for all service implementations in the Core Banking Ledger microservice. Each service implementation has a corresponding test class that verifies the functionality of all methods, including both success and error scenarios.

## Test Structure

The tests are organized into the following packages, mirroring the structure of the service implementations:

- **card/v1**: Tests for card transaction line services
- **category/v1**: Tests for transaction category services
- **core/v1**: Tests for core transaction and transaction status history services
- **directdebit/v1**: Tests for direct debit transaction line services
- **ledger/v1**: Tests for ledger account and ledger entry services
- **sepa/v1**: Tests for SEPA transfer transaction line services
- **standingorder/v1**: Tests for standing order transaction line services
- **wire/v1**: Tests for wire transfer transaction line services

## Testing Approach

All tests follow a consistent approach:

1. **Mocking**: Using Mockito to mock dependencies (repositories, mappers)
2. **Test Data Setup**: Creating test DTOs and entities in the `setUp()` method
3. **Behavior Verification**: Using StepVerifier to test reactive streams
4. **Mock Verification**: Verifying that mocks were called with the expected parameters

## Service Test Classes

### Core Services

- **TransactionServiceImplTest**: Tests for transaction CRUD operations
  - `createTransaction_Success`
  - `getTransaction_Success`
  - `getTransaction_NotFound`
  - `updateTransaction_Success`
  - `updateTransaction_NotFound`
  - `deleteTransaction_Success`
  - `deleteTransaction_NotFound`
  - `filterTransactions_Success`

- **TransactionStatusHistoryServiceImplTest**: Tests for transaction status history operations
  - `listStatusHistory_Success`
  - `createStatusHistory_Success`
  - `getStatusHistory_Success`
  - `getStatusHistory_NotFound`
  - `getStatusHistory_WrongTransactionId`
  - `updateStatusHistory_Success`
  - `updateStatusHistory_NotFound`
  - `updateStatusHistory_WrongTransactionId`
  - `deleteStatusHistory_Success`
  - `deleteStatusHistory_NotFound`
  - `deleteStatusHistory_WrongTransactionId`

### Transaction Line Services

- **TransactionLineCardServiceImplTest**: Tests for card transaction line operations
  - `getCardLine_Success`
  - `getCardLine_NotFound`
  - `createCardLine_Success`
  - `createCardLine_Error`
  - `updateCardLine_Success`
  - `updateCardLine_NotFound`
  - `updateCardLine_Error`
  - `deleteCardLine_Success`
  - `deleteCardLine_NotFound`

- **TransactionLineDirectDebitServiceImplTest**: Tests for direct debit transaction line operations
  - `getDirectDebitLine_Success`
  - `getDirectDebitLine_NotFound`
  - `createDirectDebitLine_Success`
  - `updateDirectDebitLine_Success`
  - `updateDirectDebitLine_NotFound`
  - `deleteDirectDebitLine_Success`
  - `deleteDirectDebitLine_NotFound`

- **TransactionLineSepaTransferServiceImplTest**: Tests for SEPA transfer transaction line operations
  - `getSepaTransferLine_Success`
  - `getSepaTransferLine_NotFound`
  - `createSepaTransferLine_Success`
  - `updateSepaTransferLine_Success`
  - `updateSepaTransferLine_NotFound`
  - `deleteSepaTransferLine_Success`
  - `deleteSepaTransferLine_NotFound`

- **TransactionLineWireTransferServiceImplTest**: Tests for wire transfer transaction line operations
  - `getWireTransferLine_Success`
  - `getWireTransferLine_NotFound`
  - `createWireTransferLine_Success`
  - `updateWireTransferLine_Success`
  - `updateWireTransferLine_NotFound`
  - `deleteWireTransferLine_Success`
  - `deleteWireTransferLine_NotFound`

- **TransactionLineStandingOrderServiceImplTest**: Tests for standing order transaction line operations
  - `getStandingOrderLine_Success`
  - `getStandingOrderLine_NotFound`
  - `createStandingOrderLine_Success`
  - `updateStandingOrderLine_Success`
  - `updateStandingOrderLine_NotFound`
  - `deleteStandingOrderLine_Success`
  - `deleteStandingOrderLine_NotFound`

### Category Services

- **TransactionCategoryServiceImplTest**: Tests for transaction category operations
  - `listCategories_Success`
  - `createCategory_Success`
  - `getCategory_Success`
  - `getCategory_NotFound`
  - `updateCategory_Success`
  - `updateCategory_NotFound`
  - `deleteCategory_Success`

### Ledger Services

- **LedgerAccountServiceImplTest**: Tests for ledger account operations
  - `listLedgerAccounts_Success`
  - `filterLedgerAccounts_Success`
  - `createLedgerAccount_Success`
  - `getLedgerAccount_Success`
  - `getLedgerAccount_NotFound`
  - `updateLedgerAccount_Success`
  - `updateLedgerAccount_NotFound`
  - `deleteLedgerAccount_Success`
  - `deleteLedgerAccount_NotFound`

- **LedgerEntryServiceImplTest**: Tests for ledger entry operations
  - `listLedgerEntries_Success`
  - `createLedgerEntry_Success`
  - `getLedgerEntry_Success`
  - `getLedgerEntry_NotFound`
  - `updateLedgerEntry_Success`
  - `updateLedgerEntry_NotFound`
  - `deleteLedgerEntry_Success`

## Common Patterns and Utilities

### StepVerifier

All tests use the `StepVerifier` from the `reactor-test` library to test reactive streams:

```
StepVerifier.create(service.getTransaction(1L))
        .expectNext(transactionDTO)
        .verifyComplete();
```

### Mock Setup

Tests use Mockito to mock dependencies:

```
@Mock
private TransactionRepository repository;

@Mock
private TransactionMapper mapper;

@InjectMocks
private TransactionServiceImpl service;
```

### Test Data Setup

Test data is initialized in the `setUp()` method:

```
@BeforeEach
void setUp() {
    // Initialize test data
    transactionDTO = new TransactionDTO();
    transactionDTO.setTransactionId(1L);
    // ... other properties

    transaction = new Transaction();
    transaction.setTransactionId(1L);
    // ... other properties
}
```

### Pagination and Filtering

Some tests for pagination and filtering methods are simplified due to the complexity of mocking utility classes:

```
@Test
void filterTransactions_Success() {
    // This test is simplified due to the complexity of mocking FilterUtils
    FilterRequest<TransactionDTO> filterRequest = new FilterRequest<>();
    // ... simplified test implementation
}
```

## Running the Tests

To run all tests:

```bash
mvn test
```

To run tests for a specific service:

```bash
mvn test -Dtest=TransactionServiceImplTest
```

## Test Coverage and Maintenance

The test suite aims to provide comprehensive coverage of all service methods, including both success and error scenarios. When adding new functionality to the services, corresponding tests should be added to maintain test coverage.

Key areas to focus on when maintaining tests:

1. **Edge Cases**: Ensure all edge cases are covered
2. **Error Handling**: Test error scenarios thoroughly
3. **Reactive Streams**: Verify that reactive streams behave as expected
4. **Mock Verification**: Verify that mocks are called with the expected parameters

## Notes on Reactive Testing

Testing reactive services requires special attention to the asynchronous nature of the code. The `StepVerifier` API provides a way to test reactive streams in a synchronous manner, making it easier to write and understand tests.

When testing reactive services, consider:

1. **Completion Signals**: Verify that streams complete properly
2. **Error Signals**: Test error handling in reactive streams
3. **Backpressure**: Consider testing backpressure scenarios for complex streams
4. **Concurrency**: Be aware of concurrency issues in reactive tests
