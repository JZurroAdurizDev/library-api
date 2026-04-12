package com.jabierzurro.libraryapi.exception.book;

import com.jabierzurro.libraryapi.exception.base.NotFoundException;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public class BookNotFoundException extends NotFoundException {

    public BookNotFoundException(Integer id) {
        super("Book with id " + id + " not found");
    }
}
