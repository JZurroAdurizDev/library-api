package com.jabierzurro.libraryapi.repository;

import com.jabierzurro.libraryapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

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

    Optional<Book> findByIsbn(String isbn);
}