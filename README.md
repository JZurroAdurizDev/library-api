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

## Project status
🚧 Work in progress.

The application is under active development. Features are added incrementally and documented as they are implemented.

## Development approach
- Database-first design using Flyway migrations.
- Layered architecture (Controller, Service, Repository, DTO).
- Security handled via Spring Security and JWT.
- Git flow with `main` and `develop` branches.