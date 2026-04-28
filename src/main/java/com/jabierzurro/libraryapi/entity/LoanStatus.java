package com.jabierzurro.libraryapi.entity;

/**
 * Enumeration representing the possible states of a loan.
 *
 * <p>A loan can be in one of the following states:
 * <ul>
 *   <li>{@link #ACTIVE}: The loan is currently ongoing and not yet returned</li>
 *   <li>{@link #CLOSED}: The loan has been completed and the books have been returned</li>
 * </ul>
 *
 * <p>This enum is persisted as a String in the database.
 *
 * @author Jabier Zurro Aduriz
 */
public enum LoanStatus {

    /**
     * Indicates that the loan is currently active.
     */
    ACTIVE,

    /**
     * Indicates that the loan has been closed.
     */
    CLOSED
}