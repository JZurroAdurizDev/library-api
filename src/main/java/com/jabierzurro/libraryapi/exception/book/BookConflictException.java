package com.jabierzurro.libraryapi.exception.book;

import com.jabierzurro.libraryapi.exception.base.ConflictException;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public class BookConflictException extends ConflictException {

    public enum Reason {
        ISBN_ALREADY_EXISTS,
        ACTIVE_LOANS
    }

    private final Reason reason;

    private BookConflictException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public Reason getReason() { return reason; }

    public static BookConflictException isbnAlreadyExists(String isbn) {
        return new BookConflictException(
            Reason.ISBN_ALREADY_EXISTS,
            "A book with ISBN " + isbn + " already exists"
        );
    }

    public static BookConflictException hasActiveLoans(Integer id) {
        return new BookConflictException(
            Reason.ACTIVE_LOANS,
            "Book with id " + id + " cannot be removed/modified because it has active loans"
        );
    }
}