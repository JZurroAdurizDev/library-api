package com.jabierzurro.libraryapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for creating or updating a book.
 *
 * <p>This DTO encapsulates the data required to create or fully update
 * a book entity. It is used as the request body in HTTP operations such as
 * POST and PUT.
 *
 * <p>Validation constraints are applied to ensure data integrity before
 * the request reaches the business logic layer.
 *
 * <ul>
 *     <li>{@code title}, {@code author} and {@code isbn} must not be blank</li>
 *     <li>{@code title} maximum length: 200 characters</li>
 *     <li>{@code author} maximum length: 150 characters</li>
 *     <li>{@code isbn} length must be between 10 and 13 characters</li>
 *     <li>{@code publishedYear} and {@code pages} must not be null</li>
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
public class BookRequestDTO {

    /**
     * Title of the book.
     *
     * <p>Must not be blank and must not exceed 200 characters.
     */
    @NotBlank
    @Size(max = 200)
    private String title;

    /**
     * Author of the book.
     *
     * <p>Must not be blank and must not exceed 150 characters.
     */
    @NotBlank
    @Size(max = 150)
    private String author;

    /**
     * ISBN of the book.
     *
     * <p>Must not be blank and must contain between 10 and 13 characters.
     */
    @NotBlank
    @Size(min = 10, max = 13)
    private String isbn;

    /**
     * Year when the book was published.
     *
     * <p>Must not be null.
     */
    @NotNull
    private Short publishedYear;
    
    /**
     * Total number of pages in the book.
     *
     * <p>Must not be null.
     */
    @NotNull
    private Integer pages;
}