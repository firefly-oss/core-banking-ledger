# Core Banking Ledger

Core Banking Ledger is a microservice component of the Firefly platform that provides comprehensive banking transaction management and ledger operations.

## Overview

This microservice handles various types of financial transactions and banking operations, serving as the central ledger system for the Firefly platform. It provides APIs for managing different transaction types, account balances, and financial records.

## Architecture

The project is structured as a multi-module Maven application:

- **core-banking-ledger-interfaces**: Contains API interfaces, DTOs, and contracts
- **core-banking-ledger-models**: Contains data models and database entities
- **core-banking-ledger-core**: Contains core business logic and service implementations
- **core-banking-ledger-web**: Contains web controllers and REST endpoints

### Key Features

- Transaction processing for various payment methods:
  - Card transactions
  - Direct debit operations
  - SEPA transfers
  - Wire transfers
  - Standing orders
- Transaction categorization
- Ledger management
- Financial record keeping

## Technologies

- Java 21
- Spring Boot
- Maven
- Docker
- RESTful APIs
- OpenAPI/Swagger for API documentation

## Prerequisites

- JDK 21
- Maven 3.8+
- Docker (for containerized deployment)

## Setup and Installation

### Local Development

1. Clone the repository:
   ```
   git clone https://github.com/your-organization/core-banking-ledger.git
   cd core-banking-ledger
   ```

2. Build the project:
   ```
   mvn clean install
   ```

3. Run the application:
   ```
   mvn spring-boot:run -pl core-banking-ledger-web
   ```

### Docker Deployment

1. Build the Docker image:
   ```
   mvn clean package
   docker build -t core-banking-ledger:latest .
   ```

2. Run the container:
   ```
   docker run -p 8080:8080 core-banking-ledger:latest
   ```

## API Documentation

The API documentation is available via Swagger UI when the application is running:

```
http://localhost:8080/swagger-ui.html
```

## Configuration

Configuration properties can be set in the `application.properties` or `application.yml` file in the `core-banking-ledger-web/src/main/resources` directory.

## Development Guidelines

### Code Style

- Follow Java coding conventions
- Use meaningful variable and method names
- Write comprehensive JavaDoc comments
- Follow the SOLID principles

### Testing

- Write unit tests for all business logic
- Write integration tests for API endpoints
- Ensure test coverage is maintained

To run tests:

```
mvn test
```

## Continuous Integration

The project uses CI/CD pipelines for automated testing and deployment. Each commit triggers:

1. Code compilation
2. Unit and integration tests
3. Code quality checks
4. Docker image building
5. Deployment to the appropriate environment

## Monitoring and Logging

The application uses Spring Boot Actuator for monitoring and standard SLF4J for logging. Key metrics are exposed via:

```
http://localhost:8080/actuator
```

## Contributing

1. Create a feature branch from the main branch
2. Make your changes
3. Write or update tests as necessary
4. Submit a pull request
5. Ensure CI checks pass