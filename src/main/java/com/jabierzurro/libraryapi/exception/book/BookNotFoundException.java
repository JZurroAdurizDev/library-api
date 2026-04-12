package com.jabierzurro.libraryapi.exception.book;

import com.jabierzurro.libraryapi.exception.base.NotFoundException;

/**
 * Exception thrown when a requested book cannot be found.
 *
 * <p>This exception is typically used when attempting to retrieve,
 * update or delete a book that does not exist in the system.
 *
 * <p>It extends {@link NotFoundException}, representing a standard
 * HTTP 404 error in the API.
 *
 * @author Jabier Zurro Aduriz
 */
public class BookNotFoundException extends NotFoundException {

    /**
     * Constructs a new exception for a missing book.
     *
     * @param id identifier of the book that was not found
     */
    public BookNotFoundException(Integer id) {
        super("Book with id " + id + " not found");
    }
}