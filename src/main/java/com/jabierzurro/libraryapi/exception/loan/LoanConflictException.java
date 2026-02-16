package com.jabierzurro.libraryapi.exception.loan;

import com.jabierzurro.libraryapi.exception.base.ConflictException;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public class LoanConflictException extends ConflictException {

    public enum Reason {
        BOOK_NOT_AVAILABLE,
        USER_HAS_ACTIVE_LOAN,
        LOAN_ALREADY_RETURNED
    }

    private final Reason reason;

    private LoanConflictException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public Reason getReason() { return reason; }

    public static LoanConflictException bookNotAvailable(Integer bookId) {
        return new LoanConflictException(
            Reason.BOOK_NOT_AVAILABLE,
            "Book with id " + bookId + " is not available for loan"
        );
    }

    public static LoanConflictException userHasActiveLoan(Integer userId) {
        return new LoanConflictException(
            Reason.USER_HAS_ACTIVE_LOAN,
            "User with id " + userId + " already has an active loan"
        );
    }

    public static LoanConflictException loanAlreadyReturned(Integer loanId) {
        return new LoanConflictException(
            Reason.LOAN_ALREADY_RETURNED,
            "Loan with id " + loanId + " has already been returned"
        );
    }
}