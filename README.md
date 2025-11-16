# E-Commerce Pricing Service

A Spring Boot reactive REST API service for querying product prices with priority-based rate selection.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technical Stack](#technical-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Database Schema](#database-schema)
- [Design Decisions](#design-decisions)
- [Logging Strategy](#logging-strategy)

## Overview

This service provides a REST endpoint to query the applicable price for a product. The system determines which price applies based on three criteria:

- **Application Date** - The date and time when the price should be valid
- **Product ID** - The product identifier
- **Brand ID** - The brand identifier (e.g., 1 = ZARA)

When multiple prices match the search criteria and have overlapping date ranges, the system selects the one with the highest priority value. This ensures that promotional or special pricing always takes precedence over standard rates.

## Architecture

This project follows **Hexagonal Architecture** (Ports and Adapters) with clean separation of concerns:

```
src/
├── domain/                    # Business logic layer (core)
│   ├── model/                # Domain entities (immutable)
│   ├── port/
│   │   ├── in/              # Input ports (use cases)
│   │   └── out/             # Output ports (repository interfaces)
│   └── service/             # Business logic implementation
├── application/              # Application configuration
│   └── exception/           # Business exceptions
└── infrastructure/           # External concerns
    ├── adapter/
    │   ├── in/rest/        # REST controllers (input adapters)
    │   └── out/persistence/ # Database adapters (output adapters)
    └── bootstrap/          # Application initialization
```

### SOLID Principles

The codebase adheres to SOLID principles throughout:

- **Single Responsibility** - Each class has one clearly defined purpose
- **Open/Closed** - The port/adapter pattern allows extension without modifying core logic
- **Liskov Substitution** - Implementations can be swapped transparently
- **Interface Segregation** - Small, focused interfaces define specific use cases
- **Dependency Inversion** - All dependencies point inward toward the domain layer

## Technical Stack

- **Java**: 17
- **Spring Boot**: 3.5.7
- **Spring WebFlux**: Reactive REST endpoints
- **Spring Data R2DBC**: Reactive database access
- **H2 Database**: In-memory database
- **Lombok**: Boilerplate code reduction
- **OpenAPI Generator**: Contract-first API development
- **JUnit 5 & Mockito**: Testing framework
- **Maven**: Build tool

## Prerequisites

- **JDK 17** or higher
- **Maven 3.6+** (or use included Maven wrapper)
- Git (for cloning)

## Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd spring_techincal_review_z
```

### 2. Build the Project

Using Maven wrapper (recommended):

```bash
# Windows
mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

Or using system Maven:

```bash
mvn clean install
```

This will:
- Generate OpenAPI models from `openapi/products.yaml`
- Compile the application
- Run all tests
- Package the application as a JAR

## Running the Application

### Option 1: Using Maven

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Option 2: Using JAR

```bash
java -jar target/spring_techincal_review_z-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8080**

### Verify the Application

```bash
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T10:00:00Z&productId=35455&brandId=1"
```

## API Documentation

### Endpoint

```
GET /api/v1/prices
```

### Request Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| applicationDate | DateTime | Yes | ISO 8601 format | 2020-06-14T10:00:00Z |
| productId | Long | Yes | Product identifier | 35455 |
| brandId | Long | Yes | Brand identifier | 1 |

### Response Example

**Success (200 OK)**:

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00Z",
  "endDate": "2020-12-31T23:59:59Z",
  "price": 35.50,
  "currency": "EUR"
}
```

**Error (404 Not Found)**:

```json
{
  "timestamp": "2024-11-16T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "No applicable price found for the given parameters",
  "path": "/api/v1/prices"
}
```

**Error (400 Bad Request)**:

```json
{
  "timestamp": "2024-11-16T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Product ID and Brand ID must be positive",
  "path": "/api/v1/prices"
}
```

### Interactive API Documentation

Once the application is running, access Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

## Testing

### Run All Tests

```bash
mvn test
```

### Test Coverage

The project includes comprehensive test coverage at multiple levels:

#### Integration Tests

All five required test cases from the specification are implemented and passing:

1. Query at 10:00 on June 14 → Returns price 35.50 (priceList 1)
2. Query at 16:00 on June 14 → Returns price 25.45 (priceList 2, higher priority)
3. Query at 21:00 on June 14 → Returns price 35.50 (priceList 1)
4. Query at 10:00 on June 15 → Returns price 30.50 (priceList 3)
5. Query at 21:00 on June 16 → Returns price 38.95 (priceList 4)

Additional test for error handling when no price is found (404 response).

#### Unit Tests

- **PricingService** - Business logic validation with mocked dependencies
- **PriceEntityMapper** - Domain/entity mapping correctness
- Uses Mockito for mocking and StepVerifier for reactive stream testing

### Run Specific Test Class

```bash
mvn test -Dtest=SpringTechincalReviewZApplicationTests
mvn test -Dtest=PricingServiceTest
mvn test -Dtest=PriceEntityMapperTest
```

## Database Schema

### PRICES Table

```sql
CREATE TABLE PRICES (
    ID           BIGINT PRIMARY KEY AUTO_INCREMENT,
    BRAND_ID     BIGINT NOT NULL,
    PRODUCT_ID   BIGINT NOT NULL,
    PRICE_LIST   INT NOT NULL,
    START_DATE   TIMESTAMP NOT NULL,
    END_DATE     TIMESTAMP NOT NULL,
    PRIORITY     INT NOT NULL,
    PRICE        DECIMAL(10, 2) NOT NULL,
    CURR         VARCHAR(3) NOT NULL
);

CREATE INDEX idx_prices_lookup 
ON PRICES (PRODUCT_ID, BRAND_ID, START_DATE, END_DATE);
```

### Sample Data

The database is automatically initialized with test data:

| BRAND_ID | START_DATE | END_DATE | PRICE_LIST | PRODUCT_ID | PRIORITY | PRICE | CURR |
|----------|------------|----------|------------|------------|----------|-------|------|
| 1 | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 1 | 35455 | 0 | 35.50 | EUR |
| 1 | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 | 2 | 35455 | 1 | 25.45 | EUR |
| 1 | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 | 3 | 35455 | 1 | 30.50 | EUR |
| 1 | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 | 4 | 35455 | 1 | 38.95 | EUR |

## Design Decisions

### Reactive Programming

The application uses Spring WebFlux and R2DBC for fully reactive, non-blocking I/O. This choice provides better resource utilization and scalability under high concurrent load compared to traditional blocking approaches. The reactive model allows the server to handle thousands of concurrent requests with a small thread pool.

### Immutable Domain Models

The `Price` domain model uses Java 17 records, which provide:

- Immutability by default (thread-safe)
- Protection against accidental state mutations
- Concise syntax with automatic getters, equals(), hashCode(), and toString()
- Clear alignment with Domain-Driven Design principles

### OpenAPI-First Development

The API contract is defined first in `openapi/products.yaml`, then models and interfaces are generated. This ensures:

- The API contract is the single source of truth
- Client and server implementations stay in sync
- Automatic documentation generation
- Type-safe request/response handling

### Error Handling Strategy

A global exception handler (`@RestControllerAdvice`) provides consistent error responses across the API:

- Business exceptions (like `PriceNotFoundException`) map to appropriate HTTP status codes
- All error responses follow a consistent structure with timestamp, status, message, and path
- Validation errors return 400 (Bad Request) with clear messages
- Internal errors return 500 with safe, non-exposing messages

### Input Validation

Validation occurs at the controller boundary (adapter layer):

- Null checks for required parameters
- Business rule validation (positive IDs)
- Early failure with clear error messages
- Prevents invalid data from reaching the domain layer

## Logging Strategy

The application implements production-ready logging following these principles:

### Log Levels

- **INFO** - Key business events (price found, request received, successful operations)
- **WARN** - Recoverable issues (price not found, validation failures)
- **ERROR** - Unexpected errors requiring investigation
- **DEBUG** - Detailed information useful during development and troubleshooting

### Structured Logging

Each log entry includes relevant context:

```
INFO  - Price request fulfilled - productId: 35455, brandId: 1, priceList: 2, price: 25.45 EUR
WARN  - No applicable price found - applicationDate: 2021-01-01T10:00, productId: 99999, brandId: 1
ERROR - Price request failed - productId: 35455, brandId: 1, error: Database connection error
```

This structured approach allows for easy log aggregation, filtering, and analysis in production monitoring tools.

### Request Tracing

The system logs at key points in the request lifecycle:

1. Controller layer - Request received with parameters
2. Service layer - Business operation result
3. Repository layer - Database query execution
4. Error handler - Exception details

This allows tracing a request through all layers when troubleshooting issues.

## Performance Considerations

**Database Indexing** - A composite index on (PRODUCT_ID, BRAND_ID, START_DATE, END_DATE) ensures fast query execution even with large datasets.

**Query Optimization** - Priority sorting and limiting happens in the database, avoiding unnecessary data transfer and in-memory processing.

**Reactive Streams** - Non-blocking I/O throughout the stack allows handling high concurrency with minimal resource consumption.

**Efficient Mapping** - Single-pass conversion between entities and domain models with no intermediate collections or transformations.

## Future Enhancements

- Add caching layer (Redis) for frequently queried prices
- Implement rate limiting
- Add metrics and monitoring (Micrometer/Prometheus)
- Add pagination for bulk queries
- Support for multiple currencies with conversion
- Add authentication and authorization
- Implement circuit breaker pattern
- Add distributed tracing

## Author

Felipe Casanueva - Backend Java Developer

## License

This project is for technical review purposes.

---

Built with Spring Boot 3.5, Java 17, and Hexagonal Architecture principles.
