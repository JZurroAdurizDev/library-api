package com.jabierzurro.libraryapi.repository;

import com.jabierzurro.libraryapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Book} persistence operations.
 *
 * <p>This interface extends {@link JpaRepository}, providing standard CRUD
 * operations for the {@link Book} entity.
 *
 * <p>It also defines custom query methods for searching books based on
 * optional filtering criteria and retrieving books by ISBN.
 *
 * <p>This layer is responsible only for data access and does not contain
 * business logic.
 */
public interface BookRepository extends JpaRepository<Book, Integer> {

    /**
     * Searches books using optional filtering criteria.
     *
     * <p>This method executes a native SQL query against the {@code books} table.
     * Each parameter is optional:
     * <ul>
     *     <li>If a parameter is {@code null}, its condition is ignored</li>
     *     <li>{@code title} and {@code author} are matched using case-insensitive LIKE</li>
     *     <li>{@code year} is matched using exact equality</li>
     *     <li>{@code isbn} is matched using exact equality</li>
     * </ul>
     *
     * <p>The filtering logic is implemented directly in the query using
     * conditional expressions of the form {@code (:param IS NULL OR ...)}.
     *
     * @param title optional title filter (partial match, case-insensitive)
     * @param author optional author filter (partial match, case-insensitive)
     * @param year optional publication year filter (exact match)
     * @param isbn optional ISBN filter (exact match)
     * @return list of books matching the provided criteria
     */
    @Query(value = """
        SELECT *
        FROM books b
        WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
          AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')))
          AND (:year IS NULL OR b.published_year = :year)
          AND (:isbn IS NULL OR b.isbn = :isbn)
        """, nativeQuery = true)
    List<Book> searchBooks(
            @Param("title") String title,
            @Param("author") String author,
            @Param("year") Short year,
            @Param("isbn") String isbn
    );

    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn book ISBN
     * @return an {@link Optional} containing the book if found, or empty if not found
     */
    Optional<Book> findByIsbn(String isbn);
}