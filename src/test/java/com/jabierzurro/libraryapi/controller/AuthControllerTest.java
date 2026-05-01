package com.jabierzurro.libraryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabierzurro.libraryapi.security.dto.AuthResponseDTO;
import com.jabierzurro.libraryapi.security.dto.LoginRequestDTO;
import com.jabierzurro.libraryapi.security.dto.RegisterRequestDTO;
import com.jabierzurro.libraryapi.security.service.AuthService;
import com.jabierzurro.libraryapi.security.service.UserDetailsServiceImpl;
import com.jabierzurro.libraryapi.security.util.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Web layer test for {@link AuthController}.
 *
 * <p>This test verifies the behavior of the authentication controller in an
 * isolated MVC context. The controller dependencies are mocked in order to
 * focus exclusively on request handling, HTTP status codes and JSON responses.
 *
 * <p>Security filters are disabled for this test because the goal is to validate
 * the controller contract rather than the full security chain.
 *
 * @author Jabier Zurro Aduriz
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    /**
     * MockMvc instance used to perform HTTP requests against the controller.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * JSON mapper used to serialize request DTOs.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Mocked authentication service used by the controller.
     */
    @MockitoBean
    private AuthService authService;

    /**
     * Mocked JWT service required by the application context.
     */
    @MockitoBean
    private JwtService jwtService;

    /**
     * Mocked user details service required by the application context.
     */
    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Verifies that a valid login request returns a successful response
     * containing a JWT token and its metadata.
     *
     * @throws Exception if the mock HTTP request fails
     */
    @Test
    @DisplayName("POST /auth/login should return JWT when credentials are valid")
    void loginShouldReturnJwt() throws Exception {

        // Arrange
        LoginRequestDTO request = new LoginRequestDTO("test@email.com", "password");

        AuthResponseDTO response = new AuthResponseDTO(
                "fake-jwt-token",
                "Bearer",
                1800000L
        );

        when(authService.login(request)).thenReturn(response);

        // Act + Assert
        mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value("fake-jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(1800000L));
    }

    /**
     * Verifies that a valid registration request returns a successful response
     * containing a JWT token and its metadata.
     *
     * @throws Exception if the mock HTTP request fails
     */
    @Test
    @DisplayName("POST /auth/register should return JWT when registration data is valid")
    void registerShouldReturnJwt() throws Exception {

        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO(
                "12345678A",
                "Jabier",
                "Zurro",
                "jabier@email.com",
                "password123"
        );

        AuthResponseDTO response = new AuthResponseDTO(
                "fake-register-jwt-token",
                "Bearer",
                1800000L
        );

        when(authService.register(request)).thenReturn(response);

        // Act + Assert
        mockMvc.perform(post("/auth/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value("fake-register-jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(1800000L));
    }
}