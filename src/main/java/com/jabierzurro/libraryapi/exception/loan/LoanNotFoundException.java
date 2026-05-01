package com.jabierzurro.libraryapi.exception.loan;

import com.jabierzurro.libraryapi.exception.base.NotFoundException;

/**
 * Exception thrown when a requested loan cannot be found.
 *
 * <p>This exception is typically used when attempting to retrieve,
 * update or process a loan that does not exist in the system.
 *
 * <p>It extends {@link NotFoundException}, representing a standard
 * HTTP 404 error in the API.
 *
 * @author Jabier Zurro Aduriz
 */
public class LoanNotFoundException extends NotFoundException {

    /**
     * Constructs a new exception for a missing loan.
     *
     * @param id identifier of the loan that was not found
     */
    public LoanNotFoundException(Integer id) {
        super("Loan with id " + id + " not found");
    }
}