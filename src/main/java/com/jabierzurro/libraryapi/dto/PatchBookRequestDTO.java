package com.jabierzurro.libraryapi.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for partially updating a book.
 *
 * <p>This DTO is used in PATCH operations, where only a subset of the
 * book fields may be provided. All fields are optional, and only those
 * present in the request will be considered for update.
 *
 * <p>Validation constraints are applied only when a field is present.
 * Fields not included in the request remain unchanged.
 *
 * <ul>
 *     <li>{@code title} maximum length: 200 characters</li>
 *     <li>{@code author} maximum length: 150 characters</li>
 *     <li>{@code isbn} length must be between 10 and 13 characters</li>
 * </ul>
 *
 * <p>This class does not contain any business logic.
 *
 * @author Jabier Zurro Aduriz
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatchBookRequestDTO {

    /**
     * Title of the book.
     *
     * <p>If provided, must not exceed 200 characters.
     */
    @Size(max = 200)
    private String title;

    /**
     * Author of the book.
     *
     * <p>If provided, must not exceed 150 characters.
     */
    @Size(max = 150)
    private String author;

    /**
     * ISBN of the book.
     *
     * <p>If provided, must contain between 10 and 13 characters.
     */
    @Size(min = 10, max = 13)
    private String isbn;

    /**
     * Year when the book was published.
     *
     * <p>This field is optional.
     */
    private Short publishedYear;

    /**
     * Total number of pages in the book.
     *
     * <p>This field is optional.
     */
    private Integer pages;
}