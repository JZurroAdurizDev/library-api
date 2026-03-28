# Library API

REST API for managing book loans in a library system.

This project is being built from scratch with a clean Git history, a database-first approach and a layered architecture.

## Tech stack
- Java 21
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Flyway
- Maven

## Domain overview
- Users can request book loans.
- Each loan can include up to 5 books.
- A user can only have one active loan at a time.
- Books cannot be loaned to multiple users simultaneously.

## Available endpoints
- GET /health → Returns API status
- GET / and /info → Returns API metadata (name, version, description, author)

## Database
The database schema is managed using **Flyway migrations**.

- The initial schema is defined in:
    `src/main/resources/db/migration/V1__init_schema.sql`

- It includes tables for users, roles, books, genres, loans and loan items.
- The schema has been designed and manually tested before being versioned.

1. Copy the example file:
```bash
cp sql/setup_local.sql.example sql/setup_local.sql
```
2. Edit the script and adjust credentials if needed.

3. Execute it manually in MySQL (e.g. using MySQL Workbench).

⚠️ The real `setup_local.sql` file is intentionally ignored by Git, as it may contain local credentials.

## Architecture
The application follows a layered architecture:
- **Controller layer** → Handles HTTP requests and responses
- **Service layer** → Contains business logic and orchestration
- **Repository layer** → Data access using Spring Data JPA
- **DTO layer** → Data transfer between API boundaries
- **Entity layer** → Database representation

## Testing
Controller tests are implemented using MockMvc to validate HTTP responses and endpoint behavior.

### Exception handling
A global exception handling mechanism is implemented using `@ControllerAdvice`.

The API includes:
- Base exceptions for common HTTP errors (`NotFoundException`, `ConflictException`)
- Domain-specific exceptions per entity (Book, Loan, User)
- Standardized error responses with timestamp and message

## Project status
🚧 Work in progress.

The application is under active development. Features are added incrementally and documented as they are implemented.

## Development approach
- Database-first design using Flyway migrations.
- Layered architecture (Controller, Service, Repository, DTO).
- Explicit domain exception modeling.
- Security handled via Spring Security and JWT.
- Git workflow based on `main`, `develop` and feature branches.
- New features are developed in isolated branches and merged into `develop` through Pull Requests.