package com.jabierzurro.libraryapi.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Integration tests for {@link SystemController}.
 *
 * <p>This test class verifies the behavior of the public system endpoints:
 * <ul>
 *     <li>GET /health - checks API health status</li>
 *     <li>GET / and GET /info - retrieves API metadata</li>
 * </ul>
 *
 * <p>Tests are executed using {@link org.springframework.test.web.servlet.MockMvc}
 * in a sliced web context provided by {@link WebMvcTest}.
 *
 * @author Jabier Zurro Aduriz
 */
@WebMvcTest(SystemController.class)
class SystemControllerTest {
    
    private final MockMvc mockMvc;

    SystemControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("GET /health should return API running status")
    void getHealthShouldReturnRunningStatus() throws Exception {

        // Arrange
        String endpoint = "/health";

        // Act
        var result = mockMvc.perform(get(endpoint));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(content().contentTypeCompatibleWith("application/json"))
              .andExpect(jsonPath("$.status").value("This API is running right now."));
    }

    @Test
    @DisplayName("GET / should return API info")
    void getRootShouldReturnApiInfo() throws Exception {

        // Arrange
        String endpoint = "/";

        // Act
        var result = mockMvc.perform(get(endpoint));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(content().contentTypeCompatibleWith("application/json"))
              .andExpect(jsonPath("$.service").value("library-api"))
              .andExpect(jsonPath("$.version").value("1.0.0"))
              .andExpect(jsonPath("$.description").value("REST API for library management"))
              .andExpect(jsonPath("$.author").value("Jabier Zurro Aduriz"));
    }

    @Test
    @DisplayName("GET / and /info should return API info")
    void getInfoEndpointsShouldReturnApiInfo() throws Exception {

        // Arrange
        String[] endpoints = {"/", "/info"};

        for (String endpoint : endpoints) {

            // Act
            var result = mockMvc.perform(get(endpoint));

            // Assert
            result.andExpect(status().isOk())
                  .andExpect(content().contentTypeCompatibleWith("application/json"))
                  .andExpect(jsonPath("$.service").value("library-api"))
                  .andExpect(jsonPath("$.version").value("1.0.0"))
                  .andExpect(jsonPath("$.description").value("REST API for library management"))
                  .andExpect(jsonPath("$.author").value("Jabier Zurro Aduriz"));
        }
    }
}