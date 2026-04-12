package com.jabierzurro.libraryapi.exception.book;

import com.jabierzurro.libraryapi.exception.base.ConflictException;

/**
 * Exception thrown when a conflict occurs in book-related operations.
 *
 * <p>This exception represents business rule violations that prevent
 * an operation from being completed, such as attempting to create a book
 * with an existing ISBN or modifying/removing a book that has active loans.
 *
 * <p>Each conflict is associated with a specific {@link Reason} to allow
 * more precise error handling and response generation.
 *
 * @author Jabier Zurro Aduriz
 */
public class BookConflictException extends ConflictException {

    /**
     * Enumeration of possible conflict reasons.
     */
    public enum Reason {

        /**
         * Indicates that a book with the given ISBN already exists.
         */
        ISBN_ALREADY_EXISTS,

        /**
         * Indicates that the book has active loans and cannot be modified or removed.
         */
        ACTIVE_LOANS
    }

    /**
     * Specific reason for the conflict.
     */
    private final Reason reason;

    /**
     * Constructs a new {@code BookConflictException}.
     *
     * @param reason the specific conflict reason
     * @param message detailed error message
     */
    private BookConflictException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    /**
     * Returns the reason for the conflict.
     *
     * @return conflict reason
     */
    public Reason getReason() {
        return reason;
    }

    /**
     * Creates an exception indicating that a book with the given ISBN already exists.
     *
     * @param isbn duplicated ISBN
     * @return configured {@link BookConflictException}
     */
    public static BookConflictException isbnAlreadyExists(String isbn) {
        return new BookConflictException(
            Reason.ISBN_ALREADY_EXISTS,
            "A book with ISBN " + isbn + " already exists"
        );
    }

    /**
     * Creates an exception indicating that the book has active loans.
     *
     * @param id book identifier
     * @return configured {@link BookConflictException}
     */
    public static BookConflictException hasActiveLoans(Integer id) {
        return new BookConflictException(
            Reason.ACTIVE_LOANS,
            "Book with id " + id + " cannot be removed/modified because it has active loans"
        );
    }
}