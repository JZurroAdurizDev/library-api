package com.jabierzurro.libraryapi.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Data Transfer Object representing a successful authentication response.
 *
 * <p>The access token is used internally to create the authentication cookie,
 * but it is excluded from the serialized HTTP response.
 *
 * @author Jabier Zurro Aduriz
 */
@JsonPropertyOrder({"expiresIn"})
public record AuthResponseDTO(

        @JsonIgnore
        String accessToken,

        long expiresIn
) {
}