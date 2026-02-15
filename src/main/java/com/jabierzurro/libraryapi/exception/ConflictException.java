package com.jabierzurro.libraryapi.exception;

/**
 * Base exception for business conflicts (HTTP 409).
 * Should be extended by domain-specific conflict exceptions.
 * 
 * @author Jabier Zurro Aduriz
 */
public abstract class ConflictException extends RuntimeException {
    protected ConflictException(String message) {
        super(message);
    }
}
