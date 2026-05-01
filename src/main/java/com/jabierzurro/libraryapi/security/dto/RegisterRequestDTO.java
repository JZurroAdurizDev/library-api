package com.jabierzurro.libraryapi.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO used for user registration requests.
 *
 * <p>This record encapsulates the data required to create a new user
 * in the system, including personal information and credentials.
 *
 * <p>Validation constraints ensure that all required fields are provided
 * and follow basic format rules.
 *
 * @author Jabier Zurro Aduriz
 */
public record RegisterRequestDTO(

    /**
     * National identification number of the user.
     */
    @NotBlank
    @Size(max = 9)
    String dni,

    /**
     * First name of the user.
     */
    @NotBlank
    @Size(max = 100)
    String firstName,

    /**
     * Last name of the user.
     */
    @NotBlank
    @Size(max = 100)
    String lastName,

    /**
     * Email address used for authentication.
     */
    @NotBlank
    @Email
    @Size(max = 150)
    String email,

    /**
     * Raw password provided by the user.
     *
     * <p>This value will be encoded before being stored in the database.
     */
    @NotBlank
    @Size(min = 6, max = 255)
    String password
) {}