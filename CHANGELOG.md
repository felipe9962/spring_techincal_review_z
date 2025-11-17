# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.0.1-SNAPSHOT] - 2024-11-17

### Added
- Initial implementation of Spring Boot pricing service
- REST API endpoint for price queries (GET /api/v1/prices)
- Hexagonal architecture with ports and adapters pattern
- Domain model using Java 17 records for immutability
- Reactive implementation using Spring WebFlux and R2DBC
- Priority-based price selection algorithm
- H2 in-memory database with sample data initialization
- OpenAPI specification and code generation
- Global exception handling with standardized error responses
- Multi-profile configuration (dev, test, prod)
- Comprehensive integration tests (5 required scenarios + edge cases)
- Unit tests for service layer and mappers
- AOP-based logging for request tracing
- Database query optimization with composite index
- Docker support with multi-stage build for optimal image size
- Docker Compose configuration for easy deployment
- Container health checks and resource limits

[0.0.1-SNAPSHOT]: https://github.com/username/repo/releases/tag/v0.0.1