package com.jabierzurro.libraryapi.security.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Data Transfer Object representing the authentication response.
 *
 * <p>This DTO is returned after a successful login operation and contains
 * the generated JWT access token along with its metadata.
 *
 * <p>The response follows a standard structure used in token-based
 * authentication systems:
 * <ul>
 *     <li>accessToken: the generated JWT token</li>
 *     <li>tokenType: the type of token (typically "Bearer")</li>
 *     <li>expiresIn: token validity duration in milliseconds</li>
 * </ul>
 *
 * <p>The property order is explicitly defined to ensure consistent
 * JSON serialization.
 *
 * @author Jabier Zurro Aduriz
 */
@JsonPropertyOrder({"accessToken", "tokenType", "expiresIn"})
public record AuthResponseDTO(String accessToken, String tokenType, long expiresIn) {

    /**
     * Convenience constructor that sets the token type to "Bearer" by default.
     *
     * @param accessToken generated JWT token
     * @param expiresIn token expiration time in milliseconds
     */
    public AuthResponseDTO(String accessToken, long expiresIn) {
        this(accessToken, "Bearer", expiresIn);
    }
}