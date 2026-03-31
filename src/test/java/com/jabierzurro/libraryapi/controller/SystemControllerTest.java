package com.jabierzurro.libraryapi.controller;

import com.jabierzurro.libraryapi.security.service.UserDetailsServiceImpl;
import com.jabierzurro.libraryapi.security.util.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Web layer test for {@link SystemController}.
 *
 * <p>This test verifies public endpoints that provide system-level information,
 * such as health checks and API metadata.
 *
 * <p>Security filters are disabled to focus on controller behavior only.
 *
 * @author Jabier Zurro Aduriz
 */
@WebMvcTest(SystemController.class)
@AutoConfigureMockMvc(addFilters = false)
class SystemControllerTest {

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
     * MockMvc instance used to perform HTTP requests.
     */
    private final MockMvc mockMvc;

    /**
     * Constructor-based injection of {@link MockMvc}.
     *
     * @param mockMvc mock MVC environment
     */
    SystemControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    /**
     * Verifies that the health endpoint returns the expected running status.
     *
     * @throws Exception if the request fails
     */
    @Test
    @DisplayName("GET /health should return API running status")
    void getHealthShouldReturnRunningStatus() throws Exception {
        String endpoint = "/health";

        var result = mockMvc.perform(get(endpoint));

        result.andExpect(status().isOk())
              .andExpect(content().contentTypeCompatibleWith("application/json"))
              .andExpect(jsonPath("$.status").value("This API is running right now."));
    }

    /**
     * Verifies that the root endpoint returns API metadata.
     *
     * @throws Exception if the request fails
     */
    @Test
    @DisplayName("GET / should return API info")
    void getRootShouldReturnApiInfo() throws Exception {
        String endpoint = "/";

        var result = mockMvc.perform(get(endpoint));

        result.andExpect(status().isOk())
              .andExpect(content().contentTypeCompatibleWith("application/json"))
              .andExpect(jsonPath("$.service").value("library-api"))
              .andExpect(jsonPath("$.version").value("1.0.0"))
              .andExpect(jsonPath("$.description").value("REST API for library management"))
              .andExpect(jsonPath("$.author").value("Jabier Zurro Aduriz"));
    }

    /**
     * Verifies that both "/" and "/info" endpoints return the same API metadata.
     *
     * @throws Exception if any request fails
     */
    @Test
    @DisplayName("GET / and /info should return API info")
    void getInfoEndpointsShouldReturnApiInfo() throws Exception {
        String[] endpoints = {"/", "/info"};

        for (String endpoint : endpoints) {
            var result = mockMvc.perform(get(endpoint));

            result.andExpect(status().isOk())
                  .andExpect(content().contentTypeCompatibleWith("application/json"))
                  .andExpect(jsonPath("$.service").value("library-api"))
                  .andExpect(jsonPath("$.version").value("1.0.0"))
                  .andExpect(jsonPath("$.description").value("REST API for library management"))
                  .andExpect(jsonPath("$.author").value("Jabier Zurro Aduriz"));
        }
    }
}