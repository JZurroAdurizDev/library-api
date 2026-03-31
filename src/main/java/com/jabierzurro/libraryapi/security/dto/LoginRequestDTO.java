package com.jabierzurro.libraryapi.security.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object representing a login request.
 *
 * <p>This DTO is used to capture user credentials when performing
 * an authentication request against the API.
 *
 * <p>It includes validation constraints to ensure that:
 * <ul>
 *     <li>the email is not blank and follows a valid format,</li>
 *     <li>the password is not blank.</li>
 * </ul>
 *
 * <p>The property order is explicitly defined to ensure consistent
 * JSON serialization.
 *
 * @param email user's email address used for authentication
 * @param password user's raw password
 * 
 * @author Jabier Zurro Aduriz
 */
@JsonPropertyOrder({"email", "password"})
public record LoginRequestDTO(
        @NotBlank @Email String email,
        @NotBlank String password
) {}