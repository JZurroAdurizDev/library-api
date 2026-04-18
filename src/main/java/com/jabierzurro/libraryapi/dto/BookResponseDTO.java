package com.jabierzurro.libraryapi.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Data Transfer Object for returning book data in API responses.
 *
 * <p>This DTO represents the data exposed to clients when retrieving
 * book information. It is typically used as the response body in
 * GET operations.
 *
 * <p>The properties are serialized in a fixed order defined by
 * {@link JsonPropertyOrder}.
 *
 * <p>This class is implemented as a Java {@code record}, providing
 * an immutable representation of the response data.
 *
 * <p>This DTO does not contain any business logic.
 *
 * @author Jabier Zurro Aduriz
 */
@JsonPropertyOrder({"bookId", "title", "author", "isbn", "publishedYear", "pages"})
public record BookResponseDTO(

        /**
         * Unique identifier of the book.
         */
        Integer bookId,

        /**
         * Title of the book.
         */
        String title,

        /**
         * Author of the book.
         */
        String author,

        /**
         * ISBN of the book.
         */
        String isbn,

        /**
         * Year when the book was published.
         */
        Short publishedYear,

        /**
         * Total number of pages in the book.
         */
        Integer pages
) {}