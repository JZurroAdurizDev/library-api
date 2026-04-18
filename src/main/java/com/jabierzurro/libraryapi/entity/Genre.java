package com.jabierzurro.libraryapi.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a genre in the system.
 *
 * <p>This class is mapped to the {@code genres} table in the database and
 * stores the name of each genre.
 *
 * <p>It participates in a bidirectional many-to-many relationship with
 * {@link Book}, where a genre can be associated with multiple books and
 * a book can belong to multiple genres.
 *
 * <p>The relationship is managed on the {@link Book} side, as indicated
 * by the {@code mappedBy} attribute.
 *
 * <p>Equality and hash code are based solely on the {@code id} field.
 *
 * <p>This class represents the persistence model and does not contain
 * business logic.
 *
 * @author Jabier Zurro Aduriz
 */
@Entity
@Table(name = "genres")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Genre {

    /**
     * Unique identifier of the genre.
     *
     * <p>Generated automatically using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    /**
     * Name of the genre.
     *
     * <p>Mapped to the {@code genre_name} column.
     * Cannot be null, must be unique and has a maximum length of 50 characters.
     */
    @Column(name = "genre_name", nullable = false, length = 50, unique = true)
    private String genreName;

    /**
     * Set of books associated with this genre.
     *
     * <p>Represents the inverse side of a many-to-many relationship
     * with {@link Book}. The owning side is defined in {@link Book}.
     *
     * <p>Lazy fetching is used to defer loading until explicitly accessed.
     */
    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();
}