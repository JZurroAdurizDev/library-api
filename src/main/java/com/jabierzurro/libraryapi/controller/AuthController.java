package com.jabierzurro.libraryapi.controller;

import com.jabierzurro.libraryapi.security.dto.AuthResponseDTO;
import com.jabierzurro.libraryapi.security.dto.LoginRequestDTO;
import com.jabierzurro.libraryapi.security.dto.LogoutResponseDTO;
import com.jabierzurro.libraryapi.security.dto.RegisterRequestDTO;
import com.jabierzurro.libraryapi.security.service.AuthService;
import com.jabierzurro.libraryapi.security.service.JwtCookieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    private final JwtCookieService jwtCookieService;

    /**
     * Constructor for dependency injection of the authentication service.
     *
     * @param authService service responsible for handling authentication logic
     */
    public AuthController(AuthService authService, JwtCookieService jwtCookieService) {
        this.authService = authService;
        this.jwtCookieService = jwtCookieService;
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
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO request
    ) {
        AuthResponseDTO authResponse = this.authService.login(request);

        ResponseCookie cookie = this.jwtCookieService
                .createAuthenticationCookie(authResponse.accessToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @RequestBody @Valid RegisterRequestDTO request
    ) {
        AuthResponseDTO authResponse = this.authService.register(request);

        ResponseCookie cookie = this.jwtCookieService
                .createAuthenticationCookie(authResponse.accessToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDTO> logout() {
        ResponseCookie expiredCookie =
                this.jwtCookieService.createExpiredAuthenticationCookie();

        LogoutResponseDTO response =
                new LogoutResponseDTO("Logout successful");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
                .body(response);
    }
}