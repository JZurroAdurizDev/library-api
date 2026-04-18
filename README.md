# Library API

REST API for managing book loans in a library system.

This project is being built from scratch with a clean Git history, a database-first approach and a layered architecture.

---

## Tech stack

- Java 21
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Flyway
- Maven

---

## Domain overview

- Users can request book loans
- Each loan can include up to 5 books
- A user can only have one active loan at a time
- Books cannot be loaned to multiple users simultaneously

---

## Available endpoints

### System

- GET /health → Returns API status
- GET / → Returns API metadata
- GET /info → Returns API metadata

### Users

- GET /users → retrieve all users
- GET /users/{id} → retrieve user by id
- GET /users/search → search users by optional filters (firstName, lastName, email, dni)
- POST /users → create a new user (intended for admin use)
- PUT /users/{id} → full update of an existing user
- PATCH /users/{id} → partial update of an existing user
- DELETE /users/{id} → delete a user

### Books

- GET /books → retrieve all books
- GET /books/{id} → retrieve book by id
- GET /books/search → search books by optional filters (title, author, year, isbn)
- POST /books → create a new book (intended for admin use)
- PUT /books/{id} → full update of an existing book
- PATCH /books/{id} → partial update of an existing book
- DELETE /books/{id} → delete a book

---

## Example requests

The following examples show how to interact with the API using curl.

### Create a user

```bash
curl -X POST http://localhost:8080/users \
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
curl http://localhost:8080/users
```

### Search users
```bash
curl "http://localhost:8080/users/search?firstName=John"
```

### Create a book
```bash
curl -X POST http://localhost:8080/books \
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
curl http://localhost:8080/books
```

### Search books
```bash
curl "http://localhost:8080/books/search?title=Clean"
```

### Update a book (PUT)
```bash
curl -X PUT http://localhost:8080/books/1 \
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
curl -X PATCH http://localhost:8080/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code (Updated)"
  }'
```

### Delete a book
```bash
curl -X DELETE http://localhost:8080/books/1
```

---
        
## Security

Security is implemented using Spring Security and JWT.

- Authentication mechanism is in place
- JWT-based request filtering is configured
- Core security components (filters, services, utilities) are implemented

⚠️ Authorization rules (endpoint access restrictions) are not yet fully defined and will be completed in a future feature.

---

## Database

The database schema is managed using Flyway migrations.

- Initial schema:  
  `src/main/resources/db/migration/V1__init_schema.sql`

- Includes tables for:
    - users
    - roles
    - books
    - genres
    - loans
    - loan items

- The schema has been designed and manually tested before being versioned

### Local setup

Copy the example file:

```bash
cp sql/setup_local.sql.example sql/setup_local.sql
```

Edit the script and adjust credentials if needed.

Execute it manually in MySQL (e.g. using MySQL Workbench).

⚠️ The real setup_local.sql file is ignored by Git because it may contain local credentials.

## Architecture

The application follows a layered architecture:

- Controller layer → Handles HTTP requests and responses
- Service layer → Contains business logic and orchestration
- Repository layer → Data access using Spring Data JPA
- DTO layer → Data transfer between API boundaries
- Entity layer → Database representation

## Testing
- Controller tests implemented using MockMvc
- Tests validate HTTP status codes and JSON responses
- Dependencies are mocked to isolate the web layer
 
## Exception handling

A global exception handling mechanism is implemented using @ControllerAdvice.

Includes:

- Base exceptions for common HTTP errors (e.g. NotFoundException, ConflictException)
- Domain-specific exceptions per entity (Book, User, Loan)
- Standardized error responses (timestamp, message)
  
## Project status

🚧 Work in progress

Implemented so far:

- User endpoints (complete CRUD + search)
- Book endpoints (complete CRUD + search)
- DTO-based API design
- Service layer with business rules
- Repository layer with custom queries
- Web layer testing
- Base security configuration (JWT)

Planned:

- Endpoint authorization rules
- Loan domain implementation
- Integration tests
- Security refinement

## Development approach
- Database-first design using Flyway
- Layered architecture (Controller, Service, Repository, DTO)
- Explicit domain exception modeling
- Security handled via Spring Security and JWT
- Git workflow based on main, develop and feature branches
- Features developed in isolated branches and merged via Pull Requests