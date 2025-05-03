# Core Banking Ledger SDK

The Core Banking Ledger SDK provides a comprehensive client library for integrating with the Core Banking Ledger API. It offers reactive WebClient-based API clients for all endpoints, consistent error handling, and configurable connection settings.

## Installation

Add the SDK as a dependency to your project:

### Maven

```xml
<dependency>
    <groupId>com.catalis.core.banking.ledger</groupId>
    <artifactId>core-banking-ledger-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.catalis.core.banking.ledger:core-banking-ledger-sdk:1.0.0'
```

## SDK Clients

The Core Banking Ledger SDK provides reactive clients for all API endpoints:

### Core Clients
- **TransactionClient**: Client for transaction operations
- **TransactionStatusHistoryClient**: Client for transaction status history operations
- **TransactionCategoryClient**: Client for transaction category operations

### Transaction Line Clients
- **TransactionLineCardClient**: Client for card payment transaction lines
- **TransactionLineDirectDebitClient**: Client for direct debit transaction lines
- **TransactionLineSepaTransferClient**: Client for SEPA transfer transaction lines
- **TransactionLineWireTransferClient**: Client for wire transfer transaction lines
- **TransactionLineStandingOrderClient**: Client for standing order transaction lines
- **TransactionLineDepositClient**: Client for deposit transaction lines
- **TransactionLineWithdrawalClient**: Client for withdrawal transaction lines
- **TransactionLineFeeClient**: Client for fee transaction lines
- **TransactionLineInterestClient**: Client for interest transaction lines
- **TransactionLineTransferClient**: Client for general transfer transaction lines

## Usage

### Client Initialization

```java
// Create a client with default configuration
CoreBankingLedgerClient client = new CoreBankingLedgerClient();

// Create a client with custom base URL
CoreBankingLedgerClient customClient = new CoreBankingLedgerClient("https://api.example.com");

// Create a client with custom configuration
CoreBankingLedgerClientConfig config = CoreBankingLedgerClientConfig.builder()
    .baseUrl("https://api.example.com")
    .connectTimeoutMs(5000)
    .readTimeoutMs(5000)
    .writeTimeoutMs(5000)
    .enableLogging(true)
    .build();
CoreBankingLedgerClient configuredClient = new CoreBankingLedgerClient(config);
```

### Core Clients Examples

#### Transaction Client

```java
// Create a transaction
TransactionDTO transaction = new TransactionDTO();
transaction.setExternalReference("TX-2023-12345");
transaction.setTransactionDate(LocalDateTime.now());
transaction.setValueDate(LocalDateTime.now());
transaction.setTransactionType(TransactionType.PAYMENT);
transaction.setTransactionStatus(TransactionStatus.PENDING);
transaction.setTotalAmount(new BigDecimal("100.00"));
transaction.setCurrency("EUR");
transaction.setDescription("Test transaction");
transaction.setInitiatingParty("John Doe");
transaction.setAccountId(1001L);

// Create a transaction
client.getTransactionClient().createTransaction(transaction)
    .subscribe(createdTransaction -> {
        System.out.println("Created transaction with ID: " + createdTransaction.getTransactionId());
    });

// Get a transaction by ID
client.getTransactionClient().getTransaction(1001L)
    .subscribe(retrievedTransaction -> {
        System.out.println("Retrieved transaction: " + retrievedTransaction.getDescription());
    });

// Update a transaction
transaction.setTransactionStatus(TransactionStatus.COMPLETED);
client.getTransactionClient().updateTransaction(1001L, transaction)
    .subscribe(updatedTransaction -> {
        System.out.println("Updated transaction status: " + updatedTransaction.getTransactionStatus());
    });

// Delete a transaction
client.getTransactionClient().deleteTransaction(1001L)
    .subscribe(() -> {
        System.out.println("Transaction deleted successfully");
    });
```

#### Transaction Category Client

```java
// Create a category
TransactionCategoryDTO category = new TransactionCategoryDTO();
category.setCategoryName("Utilities");
category.setCategoryDescription("Utility payments");
category.setCategoryType(CategoryType.EXPENSE);

client.getTransactionCategoryClient().createCategory(category)
    .subscribe(createdCategory -> {
        System.out.println("Created category with ID: " + createdCategory.getTransactionCategoryId());
    });

// Get a category by ID
client.getTransactionCategoryClient().getCategory(1001L)
    .subscribe(retrievedCategory -> {
        System.out.println("Retrieved category: " + retrievedCategory.getCategoryName());
    });

// List categories with pagination
PaginationRequest paginationRequest = new PaginationRequest();
paginationRequest.setPage(0);
paginationRequest.setSize(10);

client.getTransactionCategoryClient().listCategories(paginationRequest)
    .subscribe(categories -> {
        System.out.println("Retrieved " + categories.getTotalElements() + " categories");
        categories.getContent().forEach(cat -> {
            System.out.println(" - " + cat.getCategoryName());
        });
    });
```

#### Transaction Status History Client

```java
// Create a status history entry
TransactionStatusHistoryDTO statusHistory = new TransactionStatusHistoryDTO();
statusHistory.setTransactionId(1001L);
statusHistory.setStatusCode(TransactionStatus.PENDING);
statusHistory.setStatusStartDatetime(LocalDateTime.now());
statusHistory.setReason("Initial status");

client.getTransactionStatusHistoryClient().createStatusHistory(1001L, statusHistory)
    .subscribe(createdHistory -> {
        System.out.println("Created status history with ID: " + createdHistory.getTransactionStatusHistoryId());
    });

// List status history for a transaction
client.getTransactionStatusHistoryClient().listStatusHistory(1001L, paginationRequest)
    .subscribe(historyList -> {
        System.out.println("Retrieved " + historyList.getTotalElements() + " status history entries");
    });
```

### Transaction Line Clients Examples

#### Card Transaction Client

```java
// Create a card transaction line
TransactionLineCardDTO cardLine = new TransactionLineCardDTO();
cardLine.setCardAuthCode("AUTH123456");
cardLine.setCardMerchantCategoryCode("5411");
cardLine.setCardMerchantName("GROCERY STORE XYZ");
cardLine.setCardPresentFlag(true);
cardLine.setCardFraudFlag(false);
cardLine.setCardFeeAmount(new BigDecimal("0.50"));

client.getTransactionLineCardClient().createCardLine(1001L, cardLine)
    .subscribe(createdCardLine -> {
        System.out.println("Created card line with ID: " + createdCardLine.getTransactionLineCardId());
    });

// Get a card line for a transaction
client.getTransactionLineCardClient().getCardLine(1001L)
    .subscribe(retrievedCardLine -> {
        System.out.println("Retrieved card line for merchant: " + retrievedCardLine.getCardMerchantName());
    });
```

#### SEPA Transfer Client

```java
// Create a SEPA transfer line
TransactionLineSepaTransferDTO sepaLine = new TransactionLineSepaTransferDTO();
sepaLine.setSepaEndToEndId("E2E-REF-12345");
sepaLine.setSepaOriginIban("ES9121000418450200051332");
sepaLine.setSepaDestinationIban("DE89370400440532013000");
sepaLine.setSepaTransactionStatus("ACCEPTED");
sepaLine.setSepaFeeAmount(new BigDecimal("2.50"));

client.getTransactionLineSepaTransferClient().createSepaTransferLine(1001L, sepaLine)
    .subscribe(createdSepaLine -> {
        System.out.println("Created SEPA transfer line with ID: " + createdSepaLine.getTransactionLineSepaId());
    });
```

#### Wire Transfer Client

```java
// Create a wire transfer line
TransactionLineWireTransferDTO wireLine = new TransactionLineWireTransferDTO();
wireLine.setWireTransferReference("WIRE-REF-12345");
wireLine.setWireOriginSwiftBic("CHASUS33");
wireLine.setWireDestinationSwiftBic("DEUTDEFF");
wireLine.setWireTransferPriority("HIGH");
wireLine.setWireFeeAmount(new BigDecimal("25.00"));

client.getTransactionLineWireTransferClient().createWireTransferLine(1001L, wireLine)
    .subscribe(createdWireLine -> {
        System.out.println("Created wire transfer line with ID: " + createdWireLine.getTransactionLineWireTransferId());
    });
```

#### Deposit Client

```java
// Create a deposit line
TransactionLineDepositDTO depositLine = new TransactionLineDepositDTO();
depositLine.setDepositReference("DEP-REF-12345");
depositLine.setDepositMethod("CASH");
depositLine.setDepositTimestamp(LocalDateTime.now());
depositLine.setDepositLocation("Branch #123");
depositLine.setDepositFeeAmount(new BigDecimal("0.00"));

client.getTransactionLineDepositClient().createDepositLine(1001L, depositLine)
    .subscribe(createdDepositLine -> {
        System.out.println("Created deposit line with ID: " + createdDepositLine.getTransactionLineDepositId());
    });
```

#### Withdrawal Client

```java
// Create a withdrawal line
TransactionLineWithdrawalDTO withdrawalLine = new TransactionLineWithdrawalDTO();
withdrawalLine.setWithdrawalReference("WD-REF-12345");
withdrawalLine.setWithdrawalMethod("ATM");
withdrawalLine.setWithdrawalTimestamp(LocalDateTime.now());
withdrawalLine.setWithdrawalLocation("ATM #456");
withdrawalLine.setWithdrawalFeeAmount(new BigDecimal("2.50"));

client.getTransactionLineWithdrawalClient().createWithdrawalLine(1001L, withdrawalLine)
    .subscribe(createdWithdrawalLine -> {
        System.out.println("Created withdrawal line with ID: " + createdWithdrawalLine.getTransactionLineWithdrawalId());
    });
```

#### Fee Client

```java
// Create a fee line
TransactionLineFeeDTO feeLine = new TransactionLineFeeDTO();
feeLine.setFeeType("MONTHLY_MAINTENANCE");
feeLine.setFeeDescription("Monthly account maintenance fee");
feeLine.setFeeAmount(new BigDecimal("5.00"));
feeLine.setFeeTaxAmount(new BigDecimal("0.00"));
feeLine.setFeeRefundable(false);

client.getTransactionLineFeeClient().createFeeLine(1001L, feeLine)
    .subscribe(createdFeeLine -> {
        System.out.println("Created fee line with ID: " + createdFeeLine.getTransactionLineFeeId());
    });
```

#### Interest Client

```java
// Create an interest line
TransactionLineInterestDTO interestLine = new TransactionLineInterestDTO();
interestLine.setInterestRate(new BigDecimal("0.025"));
interestLine.setInterestType("SAVINGS");
interestLine.setInterestCalculationDate(LocalDate.now());
interestLine.setInterestAmount(new BigDecimal("12.50"));
interestLine.setInterestTaxAmount(new BigDecimal("2.50"));

client.getTransactionLineInterestClient().createInterestLine(1001L, interestLine)
    .subscribe(createdInterestLine -> {
        System.out.println("Created interest line with ID: " + createdInterestLine.getTransactionLineInterestId());
    });
```

#### Transfer Client

```java
// Create a transfer line
TransactionLineTransferDTO transferLine = new TransactionLineTransferDTO();
transferLine.setTransferReference("TRF-REF-12345");
transferLine.setTransferType("INTERNAL");
transferLine.setSourceAccount("1001");
transferLine.setDestinationAccount("2001");
transferLine.setTransferFeeAmount(new BigDecimal("0.00"));

client.getTransactionLineTransferClient().createTransferLine(1001L, transferLine)
    .subscribe(createdTransferLine -> {
        System.out.println("Created transfer line with ID: " + createdTransferLine.getTransactionLineTransferId());
    });
```

#### Direct Debit Client

```java
// Create a direct debit line
TransactionLineDirectDebitDTO directDebitLine = new TransactionLineDirectDebitDTO();
directDebitLine.setDirectDebitMandateId("MANDATE-12345");
directDebitLine.setDirectDebitCreditorId("CREDITOR-12345");
directDebitLine.setDirectDebitSequenceType("RCUR");
directDebitLine.setDirectDebitDueDate(LocalDate.now().plusDays(5));
directDebitLine.setDirectDebitProcessingStatus("PENDING");

client.getTransactionLineDirectDebitClient().createDirectDebitLine(1001L, directDebitLine)
    .subscribe(createdDirectDebitLine -> {
        System.out.println("Created direct debit line with ID: " + createdDirectDebitLine.getTransactionLineDirectDebitId());
    });
```

#### Standing Order Client

```java
// Create a standing order line
TransactionLineStandingOrderDTO standingOrderLine = new TransactionLineStandingOrderDTO();
standingOrderLine.setStandingOrderId("SO-12345");
standingOrderLine.setStandingOrderFrequency("MONTHLY");
standingOrderLine.setStandingOrderStartDate(LocalDate.now());
standingOrderLine.setStandingOrderEndDate(LocalDate.now().plusYears(1));
standingOrderLine.setStandingOrderStatus("ACTIVE");

client.getTransactionLineStandingOrderClient().createStandingOrderLine(1001L, standingOrderLine)
    .subscribe(createdStandingOrderLine -> {
        System.out.println("Created standing order line with ID: " + createdStandingOrderLine.getTransactionLineStandingOrderId());
    });
```

### Error Handling

The Core Banking Ledger SDK provides comprehensive error handling capabilities:

```java
// Example of error handling with the SDK
client.getTransactionClient().getTransaction(999999L)
    .subscribe(
        transaction -> {
            System.out.println("Retrieved transaction: " + transaction.getDescription());
        },
        error -> {
            if (error instanceof WebClientResponseException) {
                WebClientResponseException wcre = (WebClientResponseException) error;
                if (wcre.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                    System.err.println("Transaction not found");
                } else if (wcre.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                    System.err.println("Authentication required");
                } else {
                    System.err.println("Error: " + wcre.getStatusCode() + " - " + wcre.getStatusText());
                }
            } else {
                System.err.println("Unexpected error: " + error.getMessage());
            }
        }
    );

// Using onErrorResume for fallback behavior
client.getTransactionClient().getTransaction(999999L)
    .onErrorResume(error -> {
        if (error instanceof WebClientResponseException.NotFound) {
            System.out.println("Transaction not found, creating a new one");
            return client.getTransactionClient().createTransaction(new TransactionDTO());
        }
        return Mono.error(error);
    })
    .subscribe(transaction -> {
        System.out.println("Transaction ID: " + transaction.getTransactionId());
    });
```

## Configuration Options

The SDK can be configured with various options:

```java
CoreBankingLedgerClientConfig config = CoreBankingLedgerClientConfig.builder()
    .baseUrl("https://api.example.com")       // API base URL
    .connectTimeoutMs(5000)                   // Connection timeout in milliseconds
    .readTimeoutMs(5000)                      // Read timeout in milliseconds
    .writeTimeoutMs(5000)                     // Write timeout in milliseconds
    .enableLogging(true)                      // Enable request/response logging
    .authToken("your-auth-token")             // Authentication token
    .build();
```

## Thread Safety

All clients in the SDK are thread-safe and can be shared across multiple threads.

## Reactive Programming

The SDK is built on top of Spring WebFlux and Project Reactor, providing a fully reactive API. This allows for non-blocking, asynchronous operations that can handle high concurrency efficiently.

## Requirements

- Java 11 or higher
- Spring WebFlux
- Project Reactor