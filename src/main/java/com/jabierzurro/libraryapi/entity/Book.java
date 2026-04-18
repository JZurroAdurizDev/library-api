package com.jabierzurro.libraryapi.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a book in the system.
 *
 * <p>This class is mapped to the {@code books} table in the database and
 * contains the core attributes of a book such as title, author, ISBN,
 * publication year and number of pages.
 *
 * <p>The entity also defines a many-to-many relationship with {@link Genre},
 * representing the genres associated with each book.
 *
 * <p>Equality and hash code are based solely on the {@code id} field.
 *
 * <p>This class may contain basic domain structure but does not enforce
 * complex business rules.
 *
 * @author Jabier Zurro Aduriz
 */
@Entity
@Table(name = "books")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Book {

    /**
     * Unique identifier of the book.
     *
     * <p>Generated automatically using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    /**
     * Title of the book.
     *
     * <p>Cannot be null and has a maximum length of 200 characters.
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * Author of the book.
     *
     * <p>Cannot be null and has a maximum length of 150 characters.
     */
    @Column(nullable = false, length = 150)
    private String author;

    /**
     * ISBN of the book.
     *
     * <p>Cannot be null, must be unique and has a maximum length of 13 characters.
     */
    @Column(nullable = false, length = 13, unique = true)
    private String isbn;

    /**
     * Year when the book was published.
     *
     * <p>Mapped to the {@code published_year} column and cannot be null.
     */
    @Column(name = "published_year", nullable = false)
    private Short publishedYear;

    /**
     * Total number of pages in the book.
     *
     * <p>This field is optional.
     */
    @Column
    private Integer pages;

    /**
     * Set of genres associated with the book.
     *
     * <p>Represents a many-to-many relationship with {@link Genre}.
     *
     * <p>This association is mapped through the {@code book_genres} join table:
     * <ul>
     *     <li>{@code book_id} references this entity</li>
     *     <li>{@code genre_id} references the {@link Genre} entity</li>
     * </ul>
     *
     * <p>Lazy fetching is used to defer loading until explicitly accessed.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "book_genres",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    /**
     * Constructs a book with the required fields.
     *
     * @param title book title
     * @param author book author
     * @param isbn book ISBN
     * @param publishedYear publication year
     * @param pages number of pages
     */
    public Book(String title, String author, String isbn, Short publishedYear, Integer pages) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.pages = pages;
    }
}