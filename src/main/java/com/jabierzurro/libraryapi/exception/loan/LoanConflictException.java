package com.jabierzurro.libraryapi.exception.loan;

import com.jabierzurro.libraryapi.exception.base.ConflictException;

/**
 * Exception thrown when a conflict occurs in loan-related operations.
 *
 * <p>This exception represents business rule violations that prevent
 * a loan operation from being completed, such as attempting to borrow
 * an unavailable book, creating multiple active loans for the same user
 * or returning a loan that has already been processed.
 *
 * <p>Each conflict is associated with a specific {@link Reason} to allow
 * more precise error handling and response generation.
 *
 * @author Jabier Zurro Aduriz
 */
public class LoanConflictException extends ConflictException {

    /**
     * Enumeration of possible conflict reasons.
     */
    public enum Reason {

        /**
         * Indicates that the requested book is not available for loan.
         */
        BOOK_NOT_AVAILABLE,

        /**
         * Indicates that the user already has an active loan.
         */
        USER_HAS_ACTIVE_LOAN,

        /**
         * Indicates that the loan has already been returned.
         */
        LOAN_ALREADY_RETURNED
    }

    /**
     * Specific reason for the conflict.
     */
    private final Reason reason;

    /**
     * Constructs a new {@code LoanConflictException}.
     *
     * @param reason the specific conflict reason
     * @param message detailed error message
     */
    private LoanConflictException(Reason reason, String message) {
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
     * Creates an exception indicating that the book is not available for loan.
     *
     * @param bookId identifier of the book
     * @return configured {@link LoanConflictException}
     */
    public static LoanConflictException bookNotAvailable(Integer bookId) {
        return new LoanConflictException(
            Reason.BOOK_NOT_AVAILABLE,
            "Book with id " + bookId + " is not available for loan"
        );
    }
    
    /**
     * Creates an exception indicating that one or more books are not available for loan.
     *
     * @return configured {@link LoanConflictException}
     */
    public static LoanConflictException oneOrMoreBooksNotAvailable() {
        return new LoanConflictException(
            Reason.BOOK_NOT_AVAILABLE,
            "One or more books are not available for loan"
        );
    }

    /**
     * Creates an exception indicating that the user already has an active loan.
     *
     * @param userId identifier of the user
     * @return configured {@link LoanConflictException}
     */
    public static LoanConflictException userHasActiveLoan(Integer userId) {
        return new LoanConflictException(
            Reason.USER_HAS_ACTIVE_LOAN,
            "User with id " + userId + " already has an active loan"
        );
    }

    /**
     * Creates an exception indicating that the loan has already been returned.
     *
     * @param loanId identifier of the loan
     * @return configured {@link LoanConflictException}
     */
    public static LoanConflictException loanAlreadyReturned(Integer loanId) {
        return new LoanConflictException(
            Reason.LOAN_ALREADY_RETURNED,
            "Loan with id " + loanId + " has already been returned"
        );
    }
}