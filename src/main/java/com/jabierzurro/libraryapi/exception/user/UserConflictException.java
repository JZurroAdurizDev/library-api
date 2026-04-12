package com.jabierzurro.libraryapi.exception.user;

import com.jabierzurro.libraryapi.exception.base.ConflictException;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public class UserConflictException extends ConflictException {

    public enum Reason {
        EMAIL_ALREADY_EXISTS,
        DNI_ALREADY_EXISTS
    }

    private final Reason reason;

    private UserConflictException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public Reason getReason() { return reason; }

    public static UserConflictException emailAlreadyExists(String email) {
        return new UserConflictException(
            Reason.EMAIL_ALREADY_EXISTS,
            "User with email " + email + " already exists"
        );
    }

    public static UserConflictException dniAlreadyExists(String dni) {
        return new UserConflictException(
            Reason.DNI_ALREADY_EXISTS,
            "User with DNI " + dni + " already exists"
        );
    }
}

