package com.jabierzurro.libraryapi.service;

import com.jabierzurro.libraryapi.dto.BookRequestDTO;
import com.jabierzurro.libraryapi.dto.BookResponseDTO;
import com.jabierzurro.libraryapi.dto.PatchBookRequestDTO;
import java.util.List;

/**
 * Service interface for book-related business operations.
 *
 * <p>This interface defines the contract for managing books, including
 * retrieval, search, creation, full updates, partial updates and deletion.
 *
 * <p>It acts as an abstraction layer between the controller and the
 * persistence layer.
 *
 * <p>Implementations of this interface are responsible for applying
 * business rules, validations and coordinating data access operations.
 *
 * @author Jabier Zurro Aduriz
 */
public interface BookService {

    /**
     * Retrieves all books stored in the system.
     *
     * @return list of books as {@link BookResponseDTO}
     */
    List<BookResponseDTO> getAllBooks();

    /**
     * Retrieves a book by its unique identifier.
     *
     * @param id book identifier
     * @return the book as {@link BookResponseDTO}
     */
    BookResponseDTO getBookById(Integer id);

    /**
     * Searches books based on optional filtering criteria.
     *
     * <p>Any parameter may be null, in which case it is ignored.
     *
     * @param title optional title filter
     * @param author optional author filter
     * @param year optional publication year filter
     * @param isbn optional ISBN filter
     * @return list of matching books as {@link BookResponseDTO}
     */
    List<BookResponseDTO> search(String title, String author, Short year, String isbn);

    /**
     * Creates a new book.
     *
     * @param request DTO containing book creation data
     * @return the created book as {@link BookResponseDTO}
     */
    BookResponseDTO create(BookRequestDTO request);

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
    BookResponseDTO update(Integer id, BookRequestDTO request);

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
    BookResponseDTO patch(Integer id, PatchBookRequestDTO request);

    /**
     * Deletes a book by its identifier.
     *
     * @param id book identifier
     */
    void delete(Integer id);
}