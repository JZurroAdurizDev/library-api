package com.jabierzurro.libraryapi.controller;

import com.jabierzurro.libraryapi.dto.BookRequestDTO;
import com.jabierzurro.libraryapi.dto.BookResponseDTO;
import com.jabierzurro.libraryapi.dto.PatchBookRequestDTO;
import com.jabierzurro.libraryapi.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing book-related operations.
 *
 * <p>This class exposes HTTP endpoints to perform CRUD operations on books,
 * including retrieval, search, creation, full updates, partial updates and deletion.
 *
 * <p>It acts as the entry point for client requests and delegates all business
 * logic to the {@link BookService}.
 *
 * <p>All responses are wrapped in {@link ResponseEntity} to provide appropriate
 * HTTP status codes.
 *
 * <p>Access control rules:
 * <ul>
 *     <li>ADMIN users can create, update and delete books</li>
 *     <li>USER and ADMIN roles can retrieve and search books</li>
 * </ul>
 *
 * @author Jabier Zurro Aduriz
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    /**
     * Service responsible for handling book business logic.
     */
    private final BookService bookService;

    /**
     * Retrieves all books stored in the system.
     *
     * @return list of books as {@link BookResponseDTO}
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    /**
     * Retrieves a book by its unique identifier.
     *
     * @param id book identifier
     * @return the book as {@link BookResponseDTO}
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    /**
     * Searches books based on optional filtering criteria.
     *
     * <p>Any parameter may be null. If a parameter is not provided,
     * it will be ignored during the search process.
     *
     * @param title optional book title filter
     * @param author optional author filter
     * @param year optional publication year filter
     * @param isbn optional ISBN filter
     * @return list of matching books as {@link BookResponseDTO}
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Short year,
            @RequestParam(required = false) String isbn
    ) {
        return ResponseEntity.ok(bookService.search(title, author, year, isbn));
    }

    /**
     * Creates a new book.
     *
     * <p>The request body is validated before being processed.
     *
     * @param request DTO containing book creation data
     * @return the created book as {@link BookResponseDTO}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(
            @Valid @RequestBody BookRequestDTO request
    ) {
        return ResponseEntity.status(201).body(bookService.create(request));
    }

    /**
     * Fully updates an existing book.
     *
     * <p>This operation replaces all updatable fields of the book
     * with the values provided in the request.
     *
     * @param id book identifier
     * @param request DTO containing updated book data
     * @return the updated book as {@link BookResponseDTO}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Integer id,
            @Valid @RequestBody BookRequestDTO request
    ){
        return ResponseEntity.ok(bookService.update(id, request));
    }

    /**
     * Partially updates an existing book.
     *
     * <p>Only the fields present in the request are updated.
     * Fields not provided remain unchanged.
     *
     * @param id book identifier
     * @param request DTO containing partial book data
     * @return the updated book as {@link BookResponseDTO}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<BookResponseDTO> patchBook(
            @PathVariable Integer id,
            @Valid @RequestBody PatchBookRequestDTO request
    ) {
        return ResponseEntity.ok(bookService.patch(id, request));
    }

    /**
     * Deletes a book by its identifier.
     *
     * @param id book identifier
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}