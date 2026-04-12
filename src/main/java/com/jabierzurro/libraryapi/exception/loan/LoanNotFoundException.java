package com.jabierzurro.libraryapi.exception.loan;

import com.jabierzurro.libraryapi.exception.base.NotFoundException;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public class LoanNotFoundException extends NotFoundException {
    public LoanNotFoundException(Integer id) {
        super("Loan with id " + id + " not found");
    }
}
