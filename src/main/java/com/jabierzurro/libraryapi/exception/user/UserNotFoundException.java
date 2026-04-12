package com.jabierzurro.libraryapi.exception.user;

import com.jabierzurro.libraryapi.exception.base.NotFoundException;

/**
 * Exception thrown when a requested user cannot be found.
 *
 * <p>This exception is typically used when attempting to retrieve,
 * update or delete a user that does not exist in the system.
 *
 * <p>It extends {@link NotFoundException}, representing a standard
 * HTTP 404 error in the API.
 *
 * @author Jabier Zurro Aduriz
 */
public class UserNotFoundException extends NotFoundException {

    /**
     * Constructs a new exception for a missing user.
     *
     * @param id identifier of the user that was not found
     */
    public UserNotFoundException(Integer id) {
        super("User with id " + id + " not found");
    }
}