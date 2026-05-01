package com.jabierzurro.libraryapi.exception.role;

import com.jabierzurro.libraryapi.exception.base.NotFoundException;

/**
 * Exception thrown when a requested role cannot be found.
 *
 * <p>This exception is typically used in security or user-related operations
 * when a role expected to exist in the system (e.g. ROLE_USER, ROLE_ADMIN)
 * is not present in the database.
 *
 * <p>It extends {@link NotFoundException}, representing a standard
 * HTTP 404 error in the API.
 *
 * @author Jabier Zurro Aduriz
 */
public class RoleNotFoundException extends NotFoundException {

    /**
     * Constructs a new exception for a missing role.
     *
     * @param roleName name of the role that was not found
     */
    public RoleNotFoundException(String roleName) {
        super("Role with name " + roleName + " not found");
    }
}