# Account and Account Space Statements

## Overview

The Core Banking Ledger system provides functionality for generating and retrieving statements for accounts and account spaces. Statements provide a summary of transactions over a specific period, including opening and closing balances, total credits and debits, and detailed transaction entries.

## Table of Contents

- [Statement Periods](#statement-periods)
- [Statement Generation](#statement-generation)
- [Statement Retrieval](#statement-retrieval)
- [Statement Data Model](#statement-data-model)
- [API Endpoints](#api-endpoints)
- [SDK Usage](#sdk-usage)

## Statement Periods

Statements can be generated for the following periods:

- **Monthly**: A statement for a specific month and year.
- **Quarterly**: A statement for a specific quarter (Q1, Q2, Q3, Q4) and year.
- **Yearly**: A statement for an entire year.
- **Custom**: A statement for a custom date range.

## Statement Generation

Statements can be generated for both accounts and account spaces. When generating a statement, you can specify:

- The period type (monthly, quarterly, yearly, custom)
- The specific period (month, quarter, year, or custom date range)
- Whether to include pending transactions
- Whether to include transaction details

The system will:

1. Fetch all transactions for the specified account or account space within the date range
2. Calculate the opening and closing balances
3. Calculate the total credits and debits
4. Create a list of transaction entries with running balances
5. Store the statement metadata in the database
6. Return the complete statement data

## Statement Retrieval

You can retrieve:

- A list of all statements for an account or account space
- A list of statements within a specific date range
- A specific statement by ID

## Statement Data Model

### Statement Metadata

The statement metadata includes:

- Statement ID
- Account ID or Account Space ID
- Period type
- Start date
- End date
- Generation date
- Transaction count
- Whether pending transactions were included
- Whether transaction details were included

### Statement Entry

Each statement entry represents a transaction and includes:

- Transaction ID
- Transaction date
- Value date
- Booking date
- Transaction type
- Transaction status
- Amount
- Currency
- Description
- Initiating party
- External reference
- Running balance
- Transaction category ID
- Transaction category name

### Statement

A complete statement includes:

- Statement metadata
- Opening balance
- Closing balance
- Total credits
- Total debits
- List of statement entries

## API Endpoints

### Account Statements

- `POST /api/v1/accounts/{accountId}/statements`: Generate a statement for an account
- `GET /api/v1/accounts/{accountId}/statements`: List statements for an account
- `GET /api/v1/accounts/{accountId}/statements/date-range`: List statements for an account within a date range
- `GET /api/v1/accounts/{accountId}/statements/{statementId}`: Get a specific statement for an account

### Account Space Statements

- `POST /api/v1/account-spaces/{accountSpaceId}/statements`: Generate a statement for an account space
- `GET /api/v1/account-spaces/{accountSpaceId}/statements`: List statements for an account space
- `GET /api/v1/account-spaces/{accountSpaceId}/statements/date-range`: List statements for an account space within a date range
- `GET /api/v1/account-spaces/{accountSpaceId}/statements/{statementId}`: Get a specific statement for an account space

## SDK Usage

### Generating a Monthly Statement for an Account

```java
// Get the account statement client
AccountStatementClient statementClient = client.getAccountStatementClient(accountId);

// Create a statement request for the current month
StatementRequestDTO requestDTO = new StatementRequestDTO();
requestDTO.setPeriodType(StatementPeriodEnum.MONTHLY);
requestDTO.setMonth(Month.JUNE.getValue());
requestDTO.setYear(Year.now().getValue());
requestDTO.setIncludePending(true);
requestDTO.setIncludeDetails(true);

// Generate the statement
Mono<StatementDTO> statementMono = statementClient.generateStatement(requestDTO);
```

### Generating a Quarterly Statement for an Account Space

```java
// Get the account space statement client
AccountSpaceStatementClient statementClient = client.getAccountSpaceStatementClient(accountSpaceId);

// Create a statement request for the current quarter
StatementRequestDTO requestDTO = new StatementRequestDTO();
requestDTO.setPeriodType(StatementPeriodEnum.QUARTERLY);
int currentMonth = Month.from(LocalDate.now()).getValue();
int currentQuarter = ((currentMonth - 1) / 3) + 1;
requestDTO.setQuarter(currentQuarter);
requestDTO.setYear(Year.now().getValue());
requestDTO.setIncludePending(false);
requestDTO.setIncludeDetails(true);

// Generate the statement
Mono<StatementDTO> statementMono = statementClient.generateStatement(requestDTO);
```

### Listing Statements for an Account

```java
// Get the account statement client
AccountStatementClient statementClient = client.getAccountStatementClient(accountId);

// Create pagination request
PaginationRequest paginationRequest = PaginationRequest.of(0, 10, "generationDate", "DESC");

// List statements
Mono<PaginationResponse<StatementMetadataDTO>> statementsMono = statementClient.listStatements(paginationRequest);
```

### Getting a Specific Statement

```java
// Get the account statement client
AccountStatementClient statementClient = client.getAccountStatementClient(accountId);

// Get statement metadata
Mono<StatementMetadataDTO> statementMono = statementClient.getStatement(statementId);
```
