package com.jabierzurro.libraryapi.exception;

/**
 * Base exception for resources that cannot be found (HTTP 404).
 * Should be extended by entity-specific exceptions.
 * 
 * @author Jabier Zurro Aduriz
 */
public abstract class NotFoundException extends RuntimeException {

    protected NotFoundException(String message) {
        super(message);
    }
}
