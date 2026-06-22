# Library API

REST API for managing book loans in a library system.

This project has been built from scratch following a database-first approach, a layered architecture and a clean Git workflow.

## Tech stack

* Java 21
* Spring Boot
* Spring Security (JWT)
* Spring Data JPA
* Spring for Apache Kafka
* Apache Kafka
* MySQL
* Flyway
* Maven

## Domain overview

* Users can request book loans
* Each loan can include up to 5 books
* A user can only have one active loan at a time
* Books cannot be loaned to multiple users simultaneously

## Event-driven communication

The application includes an event-driven communication layer based on Apache Kafka.

Currently published domain events:

* `LoanCreatedEvent`
* `LoanUpdatedEvent`
* `LoanClosedEvent`

Events are:

* Serialized as JSON
* Published asynchronously to Kafka
* Keyed by loan identifier to preserve ordering consistency
* Intended to be consumed by external microservices

Current consumer support:

* `notification-service` currently consumes:

  * `LoanCreatedEvent`
  * `LoanUpdatedEvent`
  * `LoanClosedEvent`

## Available endpoints

### System

* `GET /health` → Returns API status
* `GET /` → Returns API metadata
* `GET /info` → Returns API metadata

### Authentication

* `POST /auth/register` → Register a new user and create an authentication cookie
* `POST /auth/login` → Authenticate a user and create an authentication cookie
* `POST /auth/logout` → Log out by expiring the authentication cookie

Successful login and registration responses contain the token expiration time without exposing the JWT:

```json
{
  "expiresIn": 1800000
}
```

### Users

* `GET /users` → retrieve all users (ADMIN only)
* `GET /users/me` → retrieve the currently authenticated user
* `GET /users/{id}` → retrieve user by id (ADMIN or owner)
* `GET /users/search` → search users (ADMIN only)
* `POST /users` → create a new user (ADMIN only)
* `PUT /users/{id}` → full update (ADMIN or owner)
* `PATCH /users/{id}` → partial update (ADMIN or owner)
* `DELETE /users/{id}` → delete user (ADMIN or owner)

### Books

* `GET /books` → retrieve all books
* `GET /books/{id}` → retrieve book by id
* `GET /books/search` → search books (title, author, publishedYear, isbn)
* `POST /books` → create a new book (ADMIN only)
* `PUT /books/{id}` → full update (ADMIN only)
* `PATCH /books/{id}` → partial update (ADMIN only)
* `DELETE /books/{id}` → delete book (ADMIN only)

### Loans

* `GET /loans` → retrieve all loans (USER or ADMIN)
* `GET /loans/{id}` → retrieve loan by id (USER or ADMIN)
* `GET /loans/search` → search loans (userId, status, startDate, dueDate) (USER or ADMIN)
* `POST /loans` → create a new loan (USER or ADMIN)
* `PUT /loans/{id}` → full update of an existing loan (ADMIN only)
* `PATCH /loans/{id}` → partial update of an existing loan (ADMIN or owner)
* `DELETE /loans/{id}` → delete loan (ADMIN only)

## Security

Security is implemented using Spring Security and JWT.

* JWT-based authentication is configured
* JWT request filtering is implemented
* Authentication cookies are handled by dedicated services
* Role-based authorization is applied at endpoint level
* Credentialed CORS requests are enabled for Library Web
* Core security components, filters, services and utilities are implemented

## Authentication

The main browser authentication flow uses a JWT stored in an authentication cookie.

The `access_token` cookie is configured with:

* `HttpOnly`
* `Secure`
* `SameSite=None`
* Application-wide path
* Expiration aligned with the JWT lifetime

Because the cookie is `HttpOnly`, the JWT cannot be read directly from JavaScript. The browser sends it automatically with credentialed requests.

The JWT is not included in login or registration response bodies.

Logout is performed by returning an expired `access_token` cookie.

Compatibility with the following authorization header is also maintained for direct API clients:

```text
Authorization: Bearer <token>
```

During local frontend development, credentialed CORS requests are accepted from:

```text
http://localhost:4200
```

## Roles

* `ROLE_USER`
* `ROLE_ADMIN`

## Authorization rules

ADMIN users:

* Full access to all endpoints
* Can manage users and books

USER users:

* Can access their own user data
* Cannot access admin-only endpoints

## HTTP status behavior

* `401 Unauthorized` → Missing, invalid or expired token
* `403 Forbidden` → Authenticated but insufficient permissions
* `404 Not Found` → Resource does not exist
* `409 Conflict` → Business rule violation, such as duplicate email or ISBN

## API behavior

* Search endpoints return `200 OK` with an empty array when no results are found
* Resource retrieval by ID returns `404` if not found
* All responses are DTO-based
* Sensitive data and JWT values are not exposed in response bodies

## Example requests

The following examples show how to interact with the deployed API using `curl`.

The examples use `-k` because the local Docker deployment uses a self-signed HTTPS certificate.

### Login and store the authentication cookie

```bash
curl -k -X POST https://localhost/auth/login \
  -H "Content-Type: application/json" \
  -c cookies.txt \
  -d '{
    "email": "john.doe@example.com",
    "password": "password123"
  }'
```

### Get the currently authenticated user

```bash
curl -k https://localhost/users/me \
  -b cookies.txt
```

### Create a user

Requires an authenticated ADMIN account.

```bash
curl -k -X POST https://localhost/users \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "dni": "12345678A",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "password123"
  }'
```

### Get all users

```bash
curl -k https://localhost/users \
  -b cookies.txt
```

### Search users

```bash
curl -k "https://localhost/users/search?firstName=John" \
  -b cookies.txt
```

### Create a book (ADMIN only)

```bash
curl -k -X POST https://localhost/books \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "9780132350884",
    "publishedYear": 2008,
    "pages": 464
  }'
```

### Get all books

```bash
curl -k https://localhost/books \
  -b cookies.txt
```

### Search books

```bash
curl -k "https://localhost/books/search?title=Clean" \
  -b cookies.txt
```

### Update a book (PUT)

```bash
curl -k -X PUT https://localhost/books/1 \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "9780132350884",
    "publishedYear": 2008,
    "pages": 500
  }'
```

### Partial update (PATCH)

```bash
curl -k -X PATCH https://localhost/books/1 \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code (Updated)"
  }'
```

### Delete a book

```bash
curl -k -X DELETE https://localhost/books/1 \
  -b cookies.txt
```

### Search loans

```bash
curl -k "https://localhost/loans/search?userId=1&status=ACTIVE" \
  -b cookies.txt
```

### Logout

```bash
curl -k -X POST https://localhost/auth/logout \
  -b cookies.txt \
  -c cookies.txt
```

## Database

The database schema is managed using Flyway migrations.

Initial schema:

```text
src/main/resources/db/migration/V1__init_schema.sql
```

Includes tables for:

* users
* roles
* books
* genres
* loans
* loan items

The schema has been designed and manually tested before being versioned.

## Local setup

Copy the example file:

```bash
cp sql/setup_local.sql.example sql/setup_local.sql
```

Edit the script and adjust credentials if needed.

Execute it manually in MySQL, for example using MySQL Workbench.

⚠️ The real `setup_local.sql` file is ignored by Git because it may contain local credentials.

## Architecture

The application follows a layered architecture:

* Controller layer → Handles HTTP requests and responses
* Service layer → Contains business logic and orchestration
* Repository layer → Data access using Spring Data JPA
* DTO layer → Data transfer between API boundaries
* Entity layer → Database representation
* Event layer → Kafka event DTOs and producers

The application also includes an event-driven communication layer based on Apache Kafka for asynchronous integration with external microservices.

## Testing

* Controller tests implemented using MockMvc
* Tests validate HTTP status codes, headers and JSON responses
* Authentication controller tests validate cookie creation and hidden JWT responses
* Current-user endpoint behavior is covered by controller tests
* Dependencies are mocked to isolate application layers during testing

## Exception handling

A global exception handling mechanism is implemented using `@ControllerAdvice`.

Includes:

* Base exceptions for common HTTP errors, such as `NotFoundException` and `ConflictException`
* Domain-specific exceptions per entity: Book, User and Loan
* Standardized error responses with timestamp and message

## Project status

✅ Completed (API phase)

The REST API is fully implemented and stable, including:

* User management (CRUD + search)
* Book management (CRUD + search)
* Loan management with business rules implemented
* JWT authentication through Secure HttpOnly cookies
* Authentication state recovery through `GET /users/me`
* Logout through authentication cookie expiration
* Credentialed CORS configuration for Library Web
* Role-based authorization
* Global exception handling
* Web layer testing with MockMvc
* Kafka producer integration
* Asynchronous publishing of:

  * `LoanCreatedEvent`
  * `LoanUpdatedEvent`
  * `LoanClosedEvent`
* Service layer unit testing for Kafka event publishing

✅ Consumer-side support for:

* `LoanCreatedEvent`
* `LoanUpdatedEvent`
* `LoanClosedEvent`

is fully implemented in `notification-service`.

## Deployment

The application stack is fully containerized using Docker Compose.

Current infrastructure includes:

* Nginx reverse proxy with HTTPS
* Spring Boot REST API
* Kafka broker and ZooKeeper
* Notification microservice
* Independent MySQL databases per service
* Environment-based configuration using `.env`

The authentication flow has been validated through the HTTPS reverse proxy:

```text
Login → HttpOnly cookie → protected resource → logout → rejected access
```

The full event-driven flow has been validated end-to-end:

```text
API → Kafka → notification-service
```

## Infrastructure

* Docker Compose
* Nginx
* HTTPS with self-signed SSL
* Apache Kafka
* MySQL 8

## Development approach

* Database-first design using Flyway
* Layered architecture with Controller, Service, Repository and DTO layers
* Explicit domain exception modeling
* Security handled through Spring Security, JWT and HttpOnly cookies
* Git workflow based on `main`, `develop` and feature branches
* Features developed in isolated branches and merged through Pull Requests
* Event-driven communication using Apache Kafka