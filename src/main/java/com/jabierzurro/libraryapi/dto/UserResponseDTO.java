package com.jabierzurro.libraryapi.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Response DTO representing a user exposed by the API.
 *
 * <p>This DTO is used to return user data to clients, ensuring that
 * only non-sensitive information is exposed.
 *
 * <p>The password and other internal fields are intentionally excluded
 * to maintain security and data encapsulation.
 *
 * <p>The order of JSON properties is explicitly defined to provide
 * a consistent and predictable response structure.
 *
 * @param id unique identifier of the user
 * @param dni national identification number
 * @param firstName user's first name
 * @param lastName user's last name
 * @param email user's email address
 * @param role role assigned to the user
 *
 * @author Jabier Zurro Aduriz
 */
@JsonPropertyOrder({"id", "dni", "firstName", "lastName", "email", "role"})
public record UserResponseDTO(
        Integer id,
        String dni,
        String firstName,
        String lastName,
        String email,
        String role
) {
}