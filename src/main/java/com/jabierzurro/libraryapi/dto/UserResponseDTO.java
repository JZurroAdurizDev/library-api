package com.jabierzurro.libraryapi.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
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
