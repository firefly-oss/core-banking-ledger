# Core Banking Ledger SDK

A WebClient SDK module for the core-banking-ledger microservice. This SDK provides a convenient way for other services to interact with the core-banking-ledger API programmatically using reactive programming patterns.

## Features

- Reactive programming with Spring WebFlux and Project Reactor
- Configurable WebClient with timeout settings and logging
- Support for all core-banking-ledger API endpoints
- Pagination and filtering support
- Comprehensive error handling

## Installation

Add the following dependency to your project's `pom.xml`:

```xml
<dependency>
    <groupId>com.catalis</groupId>
    <artifactId>core-banking-ledger-sdk</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Usage

### Creating a Client

You can create a client in several ways:

#### Default Configuration

```java
// Create a client with default configuration (localhost:8080)
CoreBankingLedgerClient client = new CoreBankingLedgerClient();
```

#### Custom Base URL

```java
// Create a client with a custom base URL
CoreBankingLedgerClient client = new CoreBankingLedgerClient("https://api.example.com");
```

#### Custom Configuration

```java
// Create a client with a fully customized configuration
CoreBankingLedgerClientConfig config = CoreBankingLedgerClientConfig.builder()
    .baseUrl("https://api.example.com")
    .connectTimeoutMs(10000)
    .readTimeoutMs(10000)
    .writeTimeoutMs(10000)
    .enableLogging(true)
    .build();
CoreBankingLedgerClient client = new CoreBankingLedgerClient(config);
```

#### Custom WebClient (for testing)

```java
// Create a client with a custom WebClient instance
WebClient webClient = WebClient.builder()
    .baseUrl("https://api.example.com")
    .build();
CoreBankingLedgerClient client = new CoreBankingLedgerClient(webClient);
```

### Working with Transactions

```java
// Get the transaction client
TransactionClient transactionClient = client.getTransactionClient();

// Create a new transaction
TransactionDTO newTransaction = new TransactionDTO();
// ... set transaction properties
Mono<TransactionDTO> createdTransaction = transactionClient.createTransaction(newTransaction);

// Get a transaction by ID
Mono<TransactionDTO> transaction = transactionClient.getTransaction(123L);

// Update a transaction
TransactionDTO updatedData = new TransactionDTO();
// ... set updated properties
Mono<TransactionDTO> updatedTransaction = transactionClient.updateTransaction(123L, updatedData);

// Delete a transaction
Mono<Void> deleteResult = transactionClient.deleteTransaction(123L);

// Update transaction status
Mono<TransactionDTO> statusUpdated = transactionClient.updateTransactionStatus(
    123L, TransactionStatusEnum.COMPLETED, "Transaction completed successfully");

// Create a reversal transaction
Mono<TransactionDTO> reversalTransaction = transactionClient.createReversalTransaction(
    123L, "Customer requested refund");

// Find transaction by external reference
Mono<TransactionDTO> foundTransaction = transactionClient.findByExternalReference("EXT-REF-123");

// Filter transactions
FilterRequest<TransactionDTO> filterRequest = new FilterRequest<>();
// ... set filter criteria
Mono<PaginationResponse<TransactionDTO>> filteredTransactions = 
    transactionClient.filterTransactions(filterRequest);
```

### Working with Transaction Categories

```java
// Get the transaction category client
TransactionCategoryClient categoryClient = client.getTransactionCategoryClient();

// List all categories with pagination
PaginationRequest paginationRequest = PaginationRequest.of(0, 20, "name", "ASC");
Mono<PaginationResponse<TransactionCategoryDTO>> categories = 
    categoryClient.listCategories(paginationRequest);

// Create a new category
TransactionCategoryDTO newCategory = new TransactionCategoryDTO();
// ... set category properties
Mono<TransactionCategoryDTO> createdCategory = categoryClient.createCategory(newCategory);

// Get a category by ID
Mono<TransactionCategoryDTO> category = categoryClient.getCategory(123L);

// Update a category
TransactionCategoryDTO updatedData = new TransactionCategoryDTO();
// ... set updated properties
Mono<TransactionCategoryDTO> updatedCategory = categoryClient.updateCategory(123L, updatedData);

// Delete a category
Mono<Void> deleteResult = categoryClient.deleteCategory(123L);
```

### Working with Transaction Status History

```java
// Get the transaction status history client
TransactionStatusHistoryClient historyClient = client.getTransactionStatusHistoryClient();

// List status history for a transaction
PaginationRequest paginationRequest = PaginationRequest.of(0, 20, "createdAt", "DESC");
Mono<PaginationResponse<TransactionStatusHistoryDTO>> historyList = 
    historyClient.listStatusHistory(123L, paginationRequest);

// Create a new status history record
TransactionStatusHistoryDTO newHistory = new TransactionStatusHistoryDTO();
// ... set history properties
Mono<TransactionStatusHistoryDTO> createdHistory = historyClient.createStatusHistory(123L, newHistory);

// Get a status history record by ID
Mono<TransactionStatusHistoryDTO> history = historyClient.getStatusHistory(123L, 456L);

// Update a status history record
TransactionStatusHistoryDTO updatedData = new TransactionStatusHistoryDTO();
// ... set updated properties
Mono<TransactionStatusHistoryDTO> updatedHistory = 
    historyClient.updateStatusHistory(123L, 456L, updatedData);

// Delete a status history record
Mono<Void> deleteResult = historyClient.deleteStatusHistory(123L, 456L);
```

### Working with Transaction Legs

```java
// Get the transaction leg client
TransactionLegClient legClient = client.getTransactionLegClient();

// Create a new transaction leg
TransactionLegDTO newLeg = new TransactionLegDTO();
// ... set leg properties
Mono<TransactionLegDTO> createdLeg = legClient.createTransactionLeg(123L, newLeg);

// Get a transaction leg by ID
Mono<TransactionLegDTO> leg = legClient.getTransactionLeg(123L, 456L);

// List transaction legs
PaginationRequest paginationRequest = PaginationRequest.of(0, 20, "createdAt", "DESC");
Mono<PaginationResponse<TransactionLegDTO>> legs = 
    legClient.listTransactionLegs(123L, paginationRequest);
```

### Working with Account Legs

```java
// Get the account leg client
AccountLegClient accountLegClient = client.getAccountLegClient();

// List legs for an account
PaginationRequest paginationRequest = PaginationRequest.of(0, 20, "createdAt", "DESC");
Mono<PaginationResponse<TransactionLegDTO>> legs = 
    accountLegClient.listAccountLegs(123L, paginationRequest);

// List legs for an account within a date range
LocalDateTime startDate = LocalDateTime.now().minusDays(30);
LocalDateTime endDate = LocalDateTime.now();
Mono<PaginationResponse<TransactionLegDTO>> legsInDateRange = 
    accountLegClient.listAccountLegsByDateRange(123L, startDate, endDate, paginationRequest);
```

### Working with Transaction Attachments

```java
// Get the transaction attachment client
TransactionAttachmentClient attachmentClient = client.getTransactionAttachmentClient();

// Create a new attachment
TransactionAttachmentDTO newAttachment = new TransactionAttachmentDTO();
// ... set attachment properties
Mono<TransactionAttachmentDTO> createdAttachment = 
    attachmentClient.createAttachment(123L, newAttachment);

// Get an attachment by ID
Mono<TransactionAttachmentDTO> attachment = attachmentClient.getAttachment(123L, 456L);

// List attachments for a transaction
PaginationRequest paginationRequest = PaginationRequest.of(0, 20, "createdAt", "DESC");
Mono<PaginationResponse<TransactionAttachmentDTO>> attachments = 
    attachmentClient.listAttachments(123L, paginationRequest);

// List attachments of a specific type
Mono<PaginationResponse<TransactionAttachmentDTO>> receiptAttachments = 
    attachmentClient.listAttachmentsByType(123L, "RECEIPT", paginationRequest);
```

### Working with Transaction Line Interest

```java
// Get the transaction line interest client
TransactionLineInterestClient interestClient = client.getTransactionLineInterestClient();

// Get the interest line for a transaction
Mono<TransactionLineInterestDTO> interestLine = interestClient.getInterestLine(123L);

// Create a new interest line
TransactionLineInterestDTO newInterestLine = new TransactionLineInterestDTO();
// ... set interest line properties
Mono<TransactionLineInterestDTO> createdInterestLine = 
    interestClient.createInterestLine(123L, newInterestLine);

// Update an interest line
TransactionLineInterestDTO updatedInterestLine = new TransactionLineInterestDTO();
// ... set updated properties
Mono<TransactionLineInterestDTO> updatedLine = 
    interestClient.updateInterestLine(123L, updatedInterestLine);

// Delete an interest line
Mono<Void> deleteResult = interestClient.deleteInterestLine(123L);
```

### Working with Transaction Line Card

```java
// Get the transaction line card client
TransactionLineCardClient cardClient = client.getTransactionLineCardClient();

// Get the card line for a transaction
Mono<TransactionLineCardDTO> cardLine = cardClient.getCardLine(123L);

// Create a new card line
TransactionLineCardDTO newCardLine = new TransactionLineCardDTO();
// ... set card line properties
Mono<TransactionLineCardDTO> createdCardLine = 
    cardClient.createCardLine(123L, newCardLine);

// Update a card line
TransactionLineCardDTO updatedCardLine = new TransactionLineCardDTO();
// ... set updated properties
Mono<TransactionLineCardDTO> updatedLine = 
    cardClient.updateCardLine(123L, updatedCardLine);

// Delete a card line
Mono<Void> deleteResult = cardClient.deleteCardLine(123L);
```

### Working with Transaction Line Deposit

```java
// Get the transaction line deposit client
TransactionLineDepositClient depositClient = client.getTransactionLineDepositClient();

// Get the deposit line for a transaction
Mono<TransactionLineDepositDTO> depositLine = depositClient.getDepositLine(123L);

// Create a new deposit line
TransactionLineDepositDTO newDepositLine = new TransactionLineDepositDTO();
// ... set deposit line properties
Mono<TransactionLineDepositDTO> createdDepositLine = 
    depositClient.createDepositLine(123L, newDepositLine);

// Update a deposit line
TransactionLineDepositDTO updatedDepositLine = new TransactionLineDepositDTO();
// ... set updated properties
Mono<TransactionLineDepositDTO> updatedLine = 
    depositClient.updateDepositLine(123L, updatedDepositLine);

// Delete a deposit line
Mono<Void> deleteResult = depositClient.deleteDepositLine(123L);
```

### Working with Transaction Line Direct Debit

```java
// Get the transaction line direct debit client
TransactionLineDirectDebitClient directDebitClient = client.getTransactionLineDirectDebitClient();

// Get the direct debit line for a transaction
Mono<TransactionLineDirectDebitDTO> directDebitLine = directDebitClient.getDirectDebitLine(123L);

// Create a new direct debit line
TransactionLineDirectDebitDTO newDirectDebitLine = new TransactionLineDirectDebitDTO();
// ... set direct debit line properties
Mono<TransactionLineDirectDebitDTO> createdDirectDebitLine = 
    directDebitClient.createDirectDebitLine(123L, newDirectDebitLine);

// Update a direct debit line
TransactionLineDirectDebitDTO updatedDirectDebitLine = new TransactionLineDirectDebitDTO();
// ... set updated properties
Mono<TransactionLineDirectDebitDTO> updatedLine = 
    directDebitClient.updateDirectDebitLine(123L, updatedDirectDebitLine);

// Delete a direct debit line
Mono<Void> deleteResult = directDebitClient.deleteDirectDebitLine(123L);
```

## Error Handling

The SDK uses Reactor's error handling mechanisms. You can handle errors using `onErrorResume`, `onErrorMap`, etc.

```java
transactionClient.getTransaction(123L)
    .doOnNext(transaction -> {
        // Process transaction
    })
    .onErrorResume(WebClientResponseException.NotFound.class, e -> {
        // Handle 404 Not Found
        return Mono.empty();
    })
    .onErrorResume(WebClientResponseException.class, e -> {
        // Handle other HTTP errors
        return Mono.error(new RuntimeException("API error: " + e.getStatusCode()));
    })
    .onErrorResume(e -> {
        // Handle other errors
        return Mono.error(new RuntimeException("Error retrieving transaction", e));
    })
    .subscribe();
```

## Extending the SDK

To add support for additional API endpoints, create a new client class that extends `BaseClient` and implement methods corresponding to each API endpoint.

```java
public class NewResourceClient extends BaseClient {
    private static final String BASE_PATH = "/api/v1/new-resources";

    public NewResourceClient(WebClient webClient) {
        super(webClient, BASE_PATH);
    }

    // Implement methods for the new resource
}
```

Then add a getter method for the new client in `CoreBankingLedgerClient`.
