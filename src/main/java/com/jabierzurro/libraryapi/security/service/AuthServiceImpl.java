package com.jabierzurro.libraryapi.security.service;

import com.jabierzurro.libraryapi.security.dto.AuthResponseDTO;
import com.jabierzurro.libraryapi.security.dto.LoginRequestDTO;
import com.jabierzurro.libraryapi.security.util.JwtService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Implementation of {@link AuthService} responsible for handling authentication logic.
 *
 * <p>This service performs user authentication using Spring Security's
 * {@link AuthenticationManager} and generates a JWT token upon successful login.
 *
 * <p>The authentication flow is as follows:
 * <ul>
 *     <li>user credentials are received,</li>
 *     <li>authentication is delegated to the {@link AuthenticationManager},</li>
 *     <li>if successful, a JWT token is generated using {@link JwtService},</li>
 *     <li>the token and its expiration are returned in an {@link AuthResponseDTO}.</li>
 * </ul>
 *
 * @author Jabier Zurro Aduriz
 */
@Service
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    
    @Value("${security.jwt.expiration}")
    private long expirationMillis;

    /**
     * Constructor for injecting authentication dependencies.
     *
     * @param authenticationManager manager responsible for validating credentials
     * @param jwtService service used to generate JWT tokens
     */
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Authenticates a user and generates a JWT token if the credentials are valid.
     *
     * <p>This method delegates authentication to Spring Security and, upon success,
     * creates a signed JWT token representing the authenticated user.
     *
     * @param request login request containing user credentials
     * @return authentication response containing the JWT token and its expiration time
     */
    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String jwt = jwtService.createToken(auth);
        return new AuthResponseDTO(jwt, this.expirationMillis);
    }
}