package com.jabierzurro.libraryapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a loan in the system.
 *
 * <p>A loan links a {@link User} with one or more {@link Book} instances
 * for a specific period of time.
 *
 * <p>Each loan contains:
 * <ul>
 *   <li>A user who borrows the books</li>
 *   <li>A start date and due date defining the loan period</li>
 *   <li>An optional closing timestamp when the loan is finalized</li>
 *   <li>A status indicating whether the loan is active or closed</li>
 *   <li>A collection of books associated with the loan</li>
 * </ul>
 *
 * <p>The relationship between loans and books is many-to-many and is
 * materialized through the {@code loan_items} join table.
 *
 * <p>Equality is based solely on the {@code id} field.
 *
 * @author Jabier Zurro Aduriz
 */
@Entity
@Table(name = "loans")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Loan {

    /**
     * Unique identifier of the loan.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    /**
     * User associated with the loan.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Start date of the loan.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * Due date of the loan.
     */
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    /**
     * Date and time when the loan was closed.
     * <p>Null if the loan is still active.
     */
    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    /**
     * Current status of the loan.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoanStatus status;

    /**
     * Books associated with the loan.
     *
     * <p>Represents a many-to-many relationship between loans and books.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "loan_items",
        joinColumns = @JoinColumn(name = "loan_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books = new HashSet<>();
    
    /**
     * Constructs a new loan with the given parameters.
     *
     * @param user      user associated with the loan
     * @param startDate start date of the loan
     * @param dueDate   due date of the loan
     * @param status    initial status of the loan
     */
    public Loan(User user, LocalDate startDate, LocalDate dueDate, LoanStatus status) {
        this.user = user;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
    }
}