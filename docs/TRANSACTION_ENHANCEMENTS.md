# Transaction Microservice Enhancements

This document provides detailed information about the enhancements made to the Core Banking Ledger transaction microservice.

## Table of Contents

1. [Domain-Driven Boundaries](#domain-driven-boundaries)
2. [Data Model Hygiene](#data-model-hygiene)
3. [Performance & Storage](#performance--storage)
4. [Regulatory & Scheme Readiness](#regulatory--scheme-readiness)
5. [Security & Privacy](#security--privacy)
6. [API & Integration Patterns](#api--integration-patterns)
7. [Usage Examples](#usage-examples)

## Domain-Driven Boundaries

### Transaction Legs for Double-Entry Accounting

The system now implements proper double-entry accounting principles through the `TRANSACTION_LEG` table. Each transaction consists of at least two legs (debit and credit), which ensures that the accounting equation always balances.

#### Transaction Leg Structure

```sql
CREATE TABLE IF NOT EXISTS transaction_leg (
    transaction_leg_id      BIGINT NOT NULL PRIMARY KEY,
    transaction_id          BIGINT NOT NULL,
    account_id              BIGINT NOT NULL,
    account_space_id        BIGINT,
    leg_type                VARCHAR(10) NOT NULL CHECK (leg_type IN ('DEBIT', 'CREDIT')),
    amount                  DECIMAL(19,4) NOT NULL,
    currency                CHAR(3) NOT NULL,
    description             VARCHAR(255),
    value_date              TIMESTAMP,
    booking_date            TIMESTAMP,
    date_created            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

#### Usage Example

For a transfer of €100 from Account A to Account B:

1. Create a transaction record
2. Create a debit leg for Account A (€100)
3. Create a credit leg for Account B (€100)

This ensures that the total of all debits equals the total of all credits, maintaining the accounting equation.

### Event Outbox Pattern

The system implements the outbox pattern for reliable event publishing through the `EVENT_OUTBOX` table. This pattern ensures that domain events are reliably published even in the face of failures.

#### Event Outbox Structure

```sql
CREATE TABLE IF NOT EXISTS event_outbox (
    event_id                UUID NOT NULL PRIMARY KEY,
    aggregate_type          VARCHAR(100) NOT NULL,
    aggregate_id            VARCHAR(100) NOT NULL,
    event_type              VARCHAR(100) NOT NULL,
    payload                 JSONB NOT NULL,
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed               BOOLEAN NOT NULL DEFAULT FALSE,
    processed_at            TIMESTAMP,
    retry_count             INT NOT NULL DEFAULT 0,
    last_error              TEXT
);
```

#### Event Types

The system publishes the following domain events:

- `TRANSACTION_CREATED`: When a new transaction is created
- `TRANSACTION_UPDATED`: When a transaction is updated
- `TRANSACTION_STATUS_CHANGED`: When a transaction's status changes
- `TRANSACTION_DELETED`: When a transaction is deleted
- `REVERSAL_TRANSACTION_CREATED`: When a reversal transaction is created

### Transaction Relationships

The system now supports related transactions through the `related_transaction_id` and `relation_type` fields in the `TRANSACTION` table. This enables tracking of reversals, adjustments, and chargebacks.

#### Relation Types

- `REVERSAL`: A transaction that reverses a previous transaction
- `ADJUSTMENT`: A transaction that adjusts a previous transaction
- `CHARGEBACK`: A transaction that represents a chargeback
- `CORRECTION`: A transaction that corrects a previous transaction

#### Idempotency Support

The system ensures idempotent transaction processing through unique external references and request IDs. This prevents duplicate transactions from being created when the same request is received multiple times.

## Data Model Hygiene

### Money Value Object

The system now uses a `MONEY` value object for consistent handling of amount and currency. This ensures that currency is always paired with amount and standardizes money representation across the system.

#### Money Structure

```sql
CREATE TYPE money_type AS (
    amount DECIMAL(19,4),
    currency CHAR(3)
);

CREATE TABLE IF NOT EXISTS money (
    money_id BIGINT NOT NULL PRIMARY KEY,
    amount DECIMAL(19,4) NOT NULL,
    currency CHAR(3) NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### Timestamp Semantics

The system now has clear timestamp semantics:

- `transaction_date`: When the instruction entered the system
- `booking_date`: When the transaction hit the ledger balance
- `value_date`: Interest-calculation date

### Optimistic Locking

The system implements optimistic locking through the `row_version` field in the `TRANSACTION` table. This prevents lost updates when two processors race to update the same transaction.

## Performance & Storage

### Composite Indexes

The system includes composite indexes for common queries:

```sql
CREATE INDEX idx_transaction_status_date ON transaction(transaction_status, transaction_date);
CREATE UNIQUE INDEX idx_transaction_external_reference ON transaction(external_reference) WHERE external_reference IS NOT NULL;
CREATE INDEX idx_transaction_related_id ON transaction(related_transaction_id);
CREATE INDEX idx_transaction_request_id ON transaction(request_id);
CREATE INDEX idx_transaction_batch_id ON transaction(batch_id);
CREATE INDEX idx_transaction_booking_date ON transaction(booking_date);
```

## Regulatory & Scheme Readiness

### Anti-Money Laundering (AML)

The system includes fields for AML compliance:

- `aml_risk_score`: Risk score for the transaction
- `aml_screening_result`: Result of AML screening
- `aml_large_txn_flag`: Flag for large transactions that require additional scrutiny

### Strong Customer Authentication (SCA)

The system includes fields for SCA compliance:

- `sca_method`: Method used for SCA
- `sca_result`: Result of SCA

### Instant Payments

The system includes fields for instant payments compliance:

- `instant_flag`: Flag indicating an instant payment
- `confirmation_of_payee_result`: Result of confirmation of payee check

## Security & Privacy

### Transaction Attachments

The system supports transaction attachments through the `TRANSACTION_ATTACHMENT` table. This enables storing and retrieving documents related to transactions, such as invoices, receipts, and contracts.

#### Attachment Structure

```sql
CREATE TABLE IF NOT EXISTS transaction_attachment (
    transaction_attachment_id BIGINT NOT NULL PRIMARY KEY,
    transaction_id            BIGINT NOT NULL,
    attachment_type           VARCHAR(50) NOT NULL,
    attachment_name           VARCHAR(255) NOT NULL,
    attachment_description    VARCHAR(255),
    object_storage_url        VARCHAR(1000) NOT NULL,
    content_type              VARCHAR(100),
    size_bytes                BIGINT,
    hash_sha256               VARCHAR(64),
    uploaded_by               VARCHAR(100),
    upload_date               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_created              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## API & Integration Patterns

### Transaction Controller

The `TransactionController` now includes the following endpoints:

- `POST /api/v1/transactions`: Create a new transaction
- `GET /api/v1/transactions/{transactionId}`: Get a transaction by ID
- `PUT /api/v1/transactions/{transactionId}`: Update a transaction
- `DELETE /api/v1/transactions/{transactionId}`: Delete a transaction
- `PATCH /api/v1/transactions/{transactionId}/status`: Update transaction status
- `POST /api/v1/transactions/{transactionId}/reversal`: Create a reversal transaction
- `GET /api/v1/transactions/by-reference/{externalReference}`: Find a transaction by external reference

### Transaction Leg Controller

The `TransactionLegController` includes the following endpoints:

- `POST /api/v1/transactions/{transactionId}/legs`: Create a new transaction leg
- `GET /api/v1/transactions/{transactionId}/legs/{legId}`: Get a transaction leg by ID
- `GET /api/v1/transactions/{transactionId}/legs`: List all legs for a transaction

### Account Leg Controller

The `AccountLegController` includes the following endpoints:

- `GET /api/v1/accounts/{accountId}/legs`: List all legs for an account
- `GET /api/v1/accounts/{accountId}/legs/date-range`: List all legs for an account within a date range

### Transaction Attachment Controller

The `TransactionAttachmentController` includes the following endpoints:

- `POST /api/v1/transactions/{transactionId}/attachments`: Create a new attachment
- `GET /api/v1/transactions/{transactionId}/attachments/{attachmentId}`: Get an attachment by ID
- `GET /api/v1/transactions/{transactionId}/attachments`: List all attachments for a transaction
- `GET /api/v1/transactions/{transactionId}/attachments/type/{attachmentType}`: List all attachments of a specific type

## Usage Examples

### Creating a Transaction with Legs

```java
// Create a transaction
TransactionDTO transaction = new TransactionDTO();
transaction.setExternalReference("TX-2023-12345");
transaction.setTransactionDate(LocalDateTime.now());
transaction.setValueDate(LocalDateTime.now());
transaction.setBookingDate(LocalDateTime.now());
transaction.setTransactionType(TransactionType.PAYMENT);
transaction.setTransactionStatus(TransactionStatus.PENDING);
transaction.setTotalAmount(new BigDecimal("100.00"));
transaction.setCurrency("EUR");
transaction.setDescription("Test transaction");
transaction.setInitiatingParty("John Doe");
transaction.setAccountId(1001L);
transaction.setRequestId(UUID.randomUUID().toString()); // For idempotency

client.getTransactionClient().createTransaction(transaction)
    .flatMap(createdTransaction -> {
        // Create debit leg
        TransactionLegDTO debitLeg = new TransactionLegDTO();
        debitLeg.setAccountId(1001L);
        debitLeg.setLegType("DEBIT");
        debitLeg.setAmount(new BigDecimal("100.00"));
        debitLeg.setCurrency("EUR");
        debitLeg.setDescription("Debit leg for transaction");
        debitLeg.setValueDate(LocalDateTime.now());
        debitLeg.setBookingDate(LocalDateTime.now());
        
        return client.getTransactionLegClient().createTransactionLeg(
                createdTransaction.getTransactionId(), 
                debitLeg
        ).then(Mono.just(createdTransaction));
    })
    .flatMap(createdTransaction -> {
        // Create credit leg
        TransactionLegDTO creditLeg = new TransactionLegDTO();
        creditLeg.setAccountId(1002L);
        creditLeg.setLegType("CREDIT");
        creditLeg.setAmount(new BigDecimal("100.00"));
        creditLeg.setCurrency("EUR");
        creditLeg.setDescription("Credit leg for transaction");
        creditLeg.setValueDate(LocalDateTime.now());
        creditLeg.setBookingDate(LocalDateTime.now());
        
        return client.getTransactionLegClient().createTransactionLeg(
                createdTransaction.getTransactionId(), 
                creditLeg
        ).then(Mono.just(createdTransaction));
    })
    .subscribe(finalTransaction -> {
        System.out.println("Created transaction with ID: " + finalTransaction.getTransactionId());
    });
```

### Creating a Reversal Transaction

```java
client.getTransactionClient().createReversalTransaction(
        1001L,                      // Original transaction ID
        "Customer requested refund" // Reason
).subscribe(reversalTransaction -> {
    System.out.println("Created reversal transaction with ID: " + reversalTransaction.getTransactionId());
});
```

### Adding an Attachment to a Transaction

```java
TransactionAttachmentDTO attachment = new TransactionAttachmentDTO();
attachment.setAttachmentType("INVOICE");
attachment.setAttachmentName("invoice.pdf");
attachment.setAttachmentDescription("Invoice for transaction");
attachment.setObjectStorageUrl("https://storage.example.com/invoices/invoice-123.pdf");
attachment.setContentType("application/pdf");
attachment.setSizeBytes(1024L * 1024L);
attachment.setHashSha256("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
attachment.setUploadedBy("John Doe");
attachment.setUploadDate(LocalDateTime.now());

client.getTransactionAttachmentClient().createAttachment(1001L, attachment)
    .subscribe(createdAttachment -> {
        System.out.println("Created attachment with ID: " + createdAttachment.getTransactionAttachmentId());
    });
```

### Querying Transaction Legs for an Account

```java
PaginationRequest paginationRequest = PaginationRequest.of(0, 20, "bookingDate", "DESC");

client.getAccountLegClient().listAccountLegs(1001L, paginationRequest)
    .subscribe(response -> {
        System.out.println("Found " + response.getTotalElements() + " legs for account 1001");
        response.getContent().forEach(leg -> {
            System.out.println("Leg ID: " + leg.getTransactionLegId() + 
                    ", Type: " + leg.getLegType() + 
                    ", Amount: " + leg.getAmount() + " " + leg.getCurrency());
        });
    });
```

### Querying Transaction Legs for a Date Range

```java
LocalDateTime startDate = LocalDateTime.now().minusDays(30);
LocalDateTime endDate = LocalDateTime.now();
PaginationRequest paginationRequest = PaginationRequest.of(0, 20, "bookingDate", "DESC");

client.getAccountLegClient().listAccountLegsByDateRange(1001L, startDate, endDate, paginationRequest)
    .subscribe(response -> {
        System.out.println("Found " + response.getTotalElements() + " legs for account 1001 in the last 30 days");
        response.getContent().forEach(leg -> {
            System.out.println("Leg ID: " + leg.getTransactionLegId() + 
                    ", Type: " + leg.getLegType() + 
                    ", Amount: " + leg.getAmount() + " " + leg.getCurrency() + 
                    ", Booking Date: " + leg.getBookingDate());
        });
    });
```
