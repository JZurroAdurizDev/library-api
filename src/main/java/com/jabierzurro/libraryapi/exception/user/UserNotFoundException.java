package com.jabierzurro.libraryapi.exception.user;

import com.jabierzurro.libraryapi.exception.base.NotFoundException;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Integer id) {
        super("User with id " + id + " not found");
    }
}
