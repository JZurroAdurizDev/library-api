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
- An user can only have one active loan at a time.
- Books cannot be loaned to multiple users simultaneously.

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

⚠️ The real setup_local.sql file is intentionally ignored by Git, as it may contain local credentials.

### Local database setup (development)
For local development, a setup script template is provided:

## Project status
🚧 Work in progress.

The application is under active development. Features are added incrementally and documented as they are implemented.

## Development approach
- Database-first design using Flyway migrations.
- Layered architecture (Controller, Service, Repository, DTO).
- Security handled via Spring Security and JWT.
- Git flow with `main` and `develop` branches.