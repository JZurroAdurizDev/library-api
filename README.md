# Library API

REST API for managing book loans in a library system.

This project has been built from scratch following a database-first approach, a layered architecture and a clean Git workflow.

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

- GET /users → retrieve all users (ADMIN only)
- GET /users/{id} → retrieve user by id (ADMIN or owner)
- GET /users/search → search users (ADMIN only)
- POST /users → create a new user (ADMIN only)
- PUT /users/{id} → full update (ADMIN or owner)
- PATCH /users/{id} → partial update (ADMIN or owner)
- DELETE /users/{id} → delete user (ADMIN or owner)

### Books

- GET /books → retrieve all books
- GET /books/{id} → retrieve book by id
- GET /books/search → search books (title, author, publishedYear, isbn)
- POST /books → create a new book (ADMIN only)
- PUT /books/{id} → full update (ADMIN only)
- PATCH /books/{id} → partial update (ADMIN only)
- DELETE /books/{id} → delete book (ADMIN only)

### Loans

- GET /loans → retrieve all loans (USER or ADMIN)
- GET /loans/{id} → retrieve loan by id (USER or ADMIN)
- GET /loans/search → search loans (userId, status, startDate, dueDate) (USER or ADMIN)
- POST /loans → create a new loan (USER or ADMIN)
- PUT /loans/{id} → full update of an existing loan (ADMIN only)
- PATCH /loans/{id} → partial update of an existing loan (ADMIN or owner)
- DELETE /loans/{id} → delete loan (ADMIN only)

---

## Security

Security is implemented using Spring Security and JWT.

- Authentication mechanism is in place
- JWT-based request filtering is configured
- Core security components (filters, services, utilities) are implemented

---

### Authentication

- JWT-based authentication
- Tokens must be included in the `Authorization` header:

```http
Authorization: Bearer <token>
```
---

### Roles

- `ROLE_USER`
- `ROLE_ADMIN`

### Authorization rules

- ADMIN users:
    - Full access to all endpoints
    - Can manage users and books
- USER users:
    - Can access their own user data
    - Cannot access admin-only endpoints

---

## HTTP status behavior

- 401 Unauthorized → Missing or invalid token
- 403 Forbidden → Authenticated but insufficient permissions
- 404 Not Found → Resource does not exist
- 409 Conflict → Business rule violation (e.g. duplicate email or ISBN)

---

## API behavior

- Search endpoints return 200 OK with an empty array when no results are found
- Resource retrieval by ID returns 404 if not found
- All responses are DTO-based (no sensitive data exposed)

## Example requests

The following examples show how to interact with the API using curl.

### Create a user

```bash
curl -X POST http://localhost:8080/users \
  -H "Authorization: Bearer <token>" \
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
curl http://localhost:8080/users \
  -H "Authorization: Bearer <token>"
```

### Search users
```bash
curl "http://localhost:8080/users/search?firstName=John" \
  -H "Authorization: Bearer <token>"
```

### Create a book (ADMIN only)
```bash
curl -X POST http://localhost:8080/books \
  -H "Authorization: Bearer <token>" \
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
curl http://localhost:8080/books \
   -H "Authorization: Bearer <token>"
```

### Search books
```bash
curl "http://localhost:8080/books/search?title=Clean" \
   -H "Authorization: Bearer <token>"
```

### Update a book (PUT)
```bash
curl -X PUT http://localhost:8080/books/1 \
  -H "Authorization: Bearer <token>" \
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
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code (Updated)"
  }'
```

### Delete a book
```bash
curl -X DELETE http://localhost:8080/books/1 \
  -H "Authorization: Bearer <token>"
```

### Search loans
```bash
curl "http://localhost:8080/loans/search?userId=1&status=ACTIVE" \
  -H "Authorization: Bearer <token>"
```

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

✅ Completed (API phase)

The REST API is fully implemented and stable, including:

- User management (CRUD + search)
- Book management (CRUD + search)
- Loan management (business rules implemented)
- JWT authentication
- Role-based authorization
- Global exception handling
- Web layer testing (MockMvc)

---

## Next steps

The project will be extended with:

- A Spring Data-based microservice
- Communication between the main API and the microservice
- Independent database persistence for the microservice
- Advanced deployment strategies

## Development approach
- Database-first design using Flyway
- Layered architecture (Controller, Service, Repository, DTO)
- Explicit domain exception modeling
- Security handled via Spring Security and JWT
- Git workflow based on main, develop and feature branches
- Features developed in isolated branches and merged via Pull Requests