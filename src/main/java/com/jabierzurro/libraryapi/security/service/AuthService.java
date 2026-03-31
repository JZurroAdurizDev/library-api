package com.jabierzurro.libraryapi.security.service;

import com.jabierzurro.libraryapi.security.dto.AuthResponseDTO;
import com.jabierzurro.libraryapi.security.dto.LoginRequestDTO;
import com.jabierzurro.libraryapi.security.dto.RegisterRequestDTO;

/**
 * Service interface for authentication operations.
 *
 * <p>This interface defines the contract for handling authentication-related
 * actions such as user login and registration.
 *
 * <p>Implementations of this service are responsible for validating credentials,
 * registering new users and generating JWT tokens.
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

    /**
     * Registers a new user in the system.
     *
     * <p>If the registration is successful, the user is authenticated and
     * a JWT token is returned inside an {@link AuthResponseDTO}.
     *
     * @param request registration request containing user data
     * @return authentication response containing the JWT token and metadata
     */
    AuthResponseDTO register(RegisterRequestDTO request);
}