package com.jabierzurro.libraryapi.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for security configuration.
 *
 * <p>This test verifies the behavior of the full security chain, including
 * filters, authentication configuration and request authorization rules.
 *
 * <p>Unlike controller tests, this test loads the complete Spring Boot context
 * and ensures that protected endpoints are not accessible without a valid JWT.
 *
 * @author Jabier Zurro Aduriz
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    /**
     * MockMvc instance used to perform HTTP requests against the application.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Verifies that accessing a protected endpoint without a JWT token
     * results in a 403 Forbidden response.
     *
     * @throws Exception if the request fails
     */
    @Test
    @DisplayName("Protected endpoint should return 403 when no JWT is provided")
    void protectedEndpointShouldReturnForbiddenWithoutToken() throws Exception {

        // Arrange
        String protectedEndpoint = "/users";

        // Act + Assert
        mockMvc.perform(get(protectedEndpoint))
                .andExpect(status().isForbidden());
    }
}