# E-Commerce Pricing Service

A Spring Boot reactive REST API service for querying product prices with priority-based rate selection.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Features](#features)
- [Technical Stack](#technical-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Database Schema](#database-schema)
- [Design Decisions](#design-decisions)

## 🎯 Overview

This service provides a REST endpoint to query the applicable price for a product based on:
- **Application Date**: The date and time when the price should apply
- **Product ID**: Unique identifier for the product
- **Brand ID**: Unique identifier for the brand (e.g., 1 = ZARA)

The service returns the single most applicable price based on date range and priority rules.

## 🏗️ Architecture

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

### SOLID Principles Applied

- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Extensible without modification (port/adapter pattern)
- **Liskov Substitution**: Interfaces can be swapped with implementations
- **Interface Segregation**: Small, focused interfaces (use cases)
- **Dependency Inversion**: Dependencies point inward to domain layer

## ✨ Features

- ✅ REST API following OpenAPI specification
- ✅ Reactive programming with Spring WebFlux and R2DBC
- ✅ H2 in-memory database with automatic initialization
- ✅ Priority-based price selection with database-level optimization
- ✅ Comprehensive error handling with meaningful messages
- ✅ Input validation
- ✅ Immutable domain models using Java records
- ✅ Full test coverage (integration and unit tests)
- ✅ Structured logging
- ✅ Clean code architecture

## 🛠️ Technical Stack

- **Java**: 17
- **Spring Boot**: 3.5.7
- **Spring WebFlux**: Reactive REST endpoints
- **Spring Data R2DBC**: Reactive database access
- **H2 Database**: In-memory database
- **Lombok**: Boilerplate code reduction
- **OpenAPI Generator**: Contract-first API development
- **JUnit 5 & Mockito**: Testing framework
- **Maven**: Build tool

## 📦 Prerequisites

- **JDK 17** or higher
- **Maven 3.6+** (or use included Maven wrapper)
- Git (for cloning)

## 🚀 Installation & Setup

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

## 🏃 Running the Application

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

## 📚 API Documentation

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

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Test Coverage

The project includes:

#### Integration Tests
- ✅ Test 1: Query at 10:00 on June 14 → Returns price 35.50 (priceList 1)
- ✅ Test 2: Query at 16:00 on June 14 → Returns price 25.45 (priceList 2, higher priority)
- ✅ Test 3: Query at 21:00 on June 14 → Returns price 35.50 (priceList 1)
- ✅ Test 4: Query at 10:00 on June 15 → Returns price 30.50 (priceList 3)
- ✅ Test 5: Query at 21:00 on June 16 → Returns price 38.95 (priceList 4)
- ✅ Test 6: Query with non-existent data → Returns 404

#### Unit Tests
- **PricingService**: Business logic validation
- **PriceEntityMapper**: Domain/entity mapping
- Mock-based testing with Mockito
- Reactive testing with StepVerifier

### Run Specific Test Class

```bash
mvn test -Dtest=SpringTechincalReviewZApplicationTests
mvn test -Dtest=PricingServiceTest
mvn test -Dtest=PriceEntityMapperTest
```

## 💾 Database Schema

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

## 🎨 Design Decisions

### 1. Reactive Programming

**Why**: Chosen Spring WebFlux + R2DBC for non-blocking I/O, better resource utilization, and scalability under high load.

### 2. Database-Level Optimization

**Implementation**: Query includes `ORDER BY PRIORITY DESC LIMIT 1` to:
- Reduce network overhead (single result instead of multiple)
- Minimize memory usage (no in-memory sorting)
- Leverage database indexing
- Improve performance significantly

```sql
SELECT * FROM PRICES 
WHERE PRODUCT_ID = ? AND BRAND_ID = ? 
  AND ? BETWEEN START_DATE AND END_DATE
ORDER BY PRIORITY DESC 
LIMIT 1
```

### 3. Immutable Domain Models

**Why**: Used Java records for the `Price` domain model:
- Thread-safe by design
- Prevents accidental mutations
- Follows DDD principles
- Concise syntax with built-in getters, equals, hashCode, toString

### 4. OpenAPI-First Approach

**Why**: Define API contract first, then generate code:
- Contract-driven development
- Ensures API consistency
- Automatic model generation
- Built-in documentation

### 5. Hexagonal Architecture

**Benefits**:
- Business logic isolated from infrastructure
- Easy to test (mock external dependencies)
- Technology-agnostic domain layer
- Clear boundaries between layers
- Maintainable and extensible

### 6. Comprehensive Error Handling

**Implementation**:
- Global exception handler
- Meaningful error messages
- Proper HTTP status codes (400, 404, 500)
- Consistent error response format
- Structured logging

### 7. Input Validation

**Validation Rules**:
- All parameters required (null checks)
- Product ID and Brand ID must be positive
- Early validation at controller level
- Clear error messages for invalid input

## 📊 Performance Considerations

1. **Indexed Queries**: Composite index on (PRODUCT_ID, BRAND_ID, START_DATE, END_DATE)
2. **Database Sorting**: Priority sorting done in database, not application
3. **Single Result**: `LIMIT 1` returns only the needed record
4. **Reactive Streams**: Non-blocking I/O for better concurrency
5. **Efficient Mapping**: Single-pass entity-to-domain conversion

## 🔍 Code Quality

- ✅ Clean code principles
- ✅ SOLID principles applied
- ✅ Meaningful names and comments
- ✅ Proper logging levels
- ✅ Separation of concerns
- ✅ DRY (Don't Repeat Yourself)
- ✅ Testable design

## 📝 Future Enhancements

- Add caching layer (Redis) for frequently queried prices
- Implement rate limiting
- Add metrics and monitoring (Micrometer/Prometheus)
- Add pagination for bulk queries
- Support for multiple currencies with conversion
- Add authentication and authorization
- Implement circuit breaker pattern
- Add distributed tracing

## 👨‍💻 Author

Felipe

## 📄 License

This project is for technical review purposes.

---

**Built with ❤️ using Spring Boot and Clean Architecture principles**
