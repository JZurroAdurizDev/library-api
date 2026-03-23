/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.jabierzurro.libraryapi.repository;

import com.jabierzurro.libraryapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query(value = """
        SELECT *
        FROM books b
        WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
          AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')))
          AND (:year IS NULL OR b.published_year = :year)
          AND (:isbn IS NULL OR b.isbn LIKE CONCAT('%', :isbn, '%'))
        """, nativeQuery = true)
    List<Book> searchBooks(
            @Param("title") String title,
            @Param("author") String author,
            @Param("year") Integer year,
            @Param("isbn") String isbn
    );
}