package com.jabierzurro.libraryapi.exception.user;

import com.jabierzurro.libraryapi.exception.base.ConflictException;

/**
 * Exception thrown when a conflict occurs in user-related operations.
 *
 * <p>This exception represents business rule violations that prevent
 * a user operation from being completed, such as attempting to create
 * a user with an already existing email or DNI.
 *
 * <p>Each conflict is associated with a specific {@link Reason} to allow
 * more precise error handling and response generation.
 *
 * @author Jabier Zurro Aduriz
 */
public class UserConflictException extends ConflictException {

    /**
     * Enumeration of possible conflict reasons.
     */
    public enum Reason {

        /**
         * Indicates that a user with the given email already exists.
         */
        EMAIL_ALREADY_EXISTS,

        /**
         * Indicates that a user with the given DNI already exists.
         */
        DNI_ALREADY_EXISTS
    }

    /**
     * Specific reason for the conflict.
     */
    private final Reason reason;

    /**
     * Constructs a new {@code UserConflictException}.
     *
     * @param reason the specific conflict reason
     * @param message detailed error message
     */
    private UserConflictException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    /**
     * Returns the reason for the conflict.
     *
     * @return conflict reason
     */
    public Reason getReason() {
        return reason;
    }

    /**
     * Creates an exception indicating that the email is already in use.
     *
     * @param email duplicated email
     * @return configured {@link UserConflictException}
     */
    public static UserConflictException emailAlreadyExists(String email) {
        return new UserConflictException(
            Reason.EMAIL_ALREADY_EXISTS,
            "User with email " + email + " already exists"
        );
    }

    /**
     * Creates an exception indicating that the DNI is already in use.
     *
     * @param dni duplicated DNI
     * @return configured {@link UserConflictException}
     */
    public static UserConflictException dniAlreadyExists(String dni) {
        return new UserConflictException(
            Reason.DNI_ALREADY_EXISTS,
            "User with DNI " + dni + " already exists"
        );
    }
}