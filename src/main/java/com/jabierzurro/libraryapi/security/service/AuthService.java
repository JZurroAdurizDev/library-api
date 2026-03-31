package com.jabierzurro.libraryapi.security.service;

import com.jabierzurro.libraryapi.security.dto.AuthResponseDTO;
import com.jabierzurro.libraryapi.security.dto.LoginRequestDTO;

/**
 * Service interface for authentication operations.
 *
 * <p>This interface defines the contract for handling user authentication,
 * including validating credentials and generating JWT tokens.
 *
 * <p>Implementations of this service are responsible for integrating with
 * Spring Security components and the application's user data source.
 *
 * @author Jabier Zurro Aduriz
 */
public interface AuthService {

    /**
     * Authenticates a user based on the provided credentials.
     *
     * <p>If the credentials are valid, a JWT token is generated and returned
     * inside an {@link AuthResponseDTO}.
     *
     * @param request login request containing user credentials
     * @return authentication response containing the JWT token and metadata
     */
    AuthResponseDTO login(LoginRequestDTO request);
}