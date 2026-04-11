package com.jabierzurro.libraryapi.exception.role;

import com.jabierzurro.libraryapi.exception.base.NotFoundException;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException(String roleName) {
        super("Role with name " + roleName + " not found");
    }
}