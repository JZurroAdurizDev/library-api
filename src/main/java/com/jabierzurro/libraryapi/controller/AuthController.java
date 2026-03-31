package com.jabierzurro.libraryapi.controller;

import com.jabierzurro.libraryapi.security.dto.AuthResponseDTO;
import com.jabierzurro.libraryapi.security.dto.LoginRequestDTO;
import com.jabierzurro.libraryapi.security.dto.RegisterRequestDTO;
import com.jabierzurro.libraryapi.security.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller responsible for authentication operations.
 *
 * <p>This controller exposes endpoints related to user authentication,
 * such as login. It delegates the authentication logic to the {@link AuthService}.
 *
 * <p>Base path: <b>/auth</b>
 *
 * @author Jabier Zurro Aduriz
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructor for dependency injection of the authentication service.
     *
     * @param authService service responsible for handling authentication logic
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates a user and returns a JWT token if the credentials are valid.
     *
     * <p>This endpoint receives user credentials, validates them and, if correct,
     * returns an {@link AuthResponseDTO} containing the authentication token.
     *
     * @param request login request containing user credentials
     * @return a {@link ResponseEntity} with the authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(this.authService.login(request));
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        return ResponseEntity.ok(this.authService.register(request));
    }
}