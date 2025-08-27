# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a JHipster 8.0.0 application called "nextjs_template_boot" - a Spring Boot backend application for an event management platform with features for event organization, ticketing, user management, and communication. The application integrates with AWS services (S3, SES) and Twilio for WhatsApp messaging.

## Key Commands

### Development
```bash
# Start application in development mode
./mvnw
# or
npm run app:start

# Start with debugging (port 8000)
npm run backend:debug

# Generate API code from OpenAPI spec
./mvnw generate-sources
```

### Testing
```bash
# Run all tests
./mvnw verify

# Run unit tests only
npm run backend:unit:test

# Run full CI test suite
npm run ci:backend:test
```

### Code Quality
```bash
# Format code with Prettier
npm run prettier:format

# Check code formatting
npm run prettier:check

# Run Checkstyle
npm run backend:nohttp:test
```

### Build & Package
```bash
# Build for production (JAR)
./mvnw -Pprod clean verify

# Build for production (WAR)
./mvnw -Pprod,war clean verify

# Build Docker image
npm run java:docker

# Build Docker image for ARM64
npm run java:docker:arm64
```

### Database
```bash
# Start PostgreSQL with Docker
npm run docker:db:up

# Stop PostgreSQL
npm run docker:db:down
```

## Architecture Overview

### Core Structure
- **Domain Layer**: JPA entities in `src/main/java/com/nextjstemplate/domain/`
- **Service Layer**: Business logic with service interfaces and implementations
- **Repository Layer**: Spring Data JPA repositories with criteria-based querying
- **Web Layer**: REST controllers in `src/main/java/com/nextjstemplate/web/rest/`
- **Configuration**: Spring configuration classes in `src/main/java/com/nextjstemplate/config/`

### Key Features
- **Event Management**: Complete event lifecycle (EventDetails, EventAttendee, EventTicketType, etc.)
- **User Management**: User profiles, registration, subscriptions
- **Communication**: Email (AWS SES) and WhatsApp (Twilio) integration
- **Ticketing System**: Ticket types, transactions, QR code generation
- **File Storage**: AWS S3 integration for media files
- **Audit Logging**: Comprehensive audit trails for admin actions

### Technology Stack
- **Backend**: Spring Boot 3.1.5, JHipster 8.0.0
- **Database**: PostgreSQL (production), H2 (testing)
- **Security**: Spring Security with JWT
- **API Documentation**: OpenAPI/Swagger
- **Caching**: Ehcache
- **Message Queue**: Not currently implemented
- **Cloud Services**: AWS S3, AWS SES, Twilio

### Data Model Patterns
- Uses MapStruct for DTO/Entity mapping
- Criteria-based dynamic querying with JHipster's query service pattern
- JPA auditing for created/modified tracking
- Liquibase for database schema management

### Development Patterns
- API-first development using OpenAPI specifications (`src/main/resources/swagger/api.yml`)
- Service layer abstraction with interfaces
- DTO pattern for API data transfer
- Exception handling with custom exception classes
- Comprehensive integration tests using Testcontainers

### Configuration Management
- Multi-profile configuration (dev, prod, test)
- External configuration via `application-*.yml` files
- AWS credentials and service configuration
- Database connection pooling with HikariCP

### Code Generation
This project heavily uses JHipster entity generation. When modifying entities:
1. Update the `.jhipster/*.json` configuration files
2. Regenerate entities using JHipster CLI
3. Review and merge any custom business logic

### Important Notes
- The application runs on port 8080 by default
- Uses JWT for authentication with OAuth2 resource server setup
- All entities follow JHipster naming conventions
- Business logic should be implemented in service layer, not controllers
- Always use DTOs for external API communication