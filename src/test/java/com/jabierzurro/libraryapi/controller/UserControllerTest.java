package com.jabierzurro.libraryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabierzurro.libraryapi.dto.UserRequestDTO;
import com.jabierzurro.libraryapi.dto.UserResponseDTO;
import com.jabierzurro.libraryapi.dto.PatchUserRequestDTO;
import com.jabierzurro.libraryapi.dto.UpdateUserRequestDTO;
import com.jabierzurro.libraryapi.security.service.UserDetailsServiceImpl;
import com.jabierzurro.libraryapi.security.util.JwtService;
import com.jabierzurro.libraryapi.service.UserService;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



/**
 * Web layer test for {@link UserController}.
 *
 * <p>This test verifies the behavior of user-related endpoints in an
 * isolated MVC context. Dependencies are mocked to focus on request
 * handling, HTTP responses and JSON structure.
 *
 * <p>Security filters are disabled to test only controller logic.
 *
 * @author Jabier Zurro Aduriz
 */
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("GET /users should return user list")
    void getAllUsersShouldReturnOk() throws Exception {

        // Arrange
        List<UserResponseDTO> users = List.of(
            new UserResponseDTO(1, "12345678A", "John", "Wick", "jwick@test.com", "ROLE_USER")
        );

        when(userService.getAllUsers()).thenReturn(users);

        // Act + Assert
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /users/{id} should return user when it exists")
    void getUserByIdShouldReturnOk() throws Exception {

        // Arrange
        UserResponseDTO user = new UserResponseDTO(
            1, "12345678A", "John", "Wick", "jwick@test.com", "ROLE_USER"
        );

        when(userService.getUserById(1)).thenReturn(user);

        // Act + Assert
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.dni").value("12345678A"));
    }

    @Test
    @DisplayName("GET /users/search should return filtered users")
    void searchUsersShouldReturnOk() throws Exception {

        // Arrange
        List<UserResponseDTO> users = List.of(
            new UserResponseDTO(1, "12345678A", "John", "Wick", "john@test.com", "ROLE_USER")
        );

        when(userService.search("John", null, null, null)).thenReturn(users);

        // Act + Assert
        mockMvc.perform(get("/users/search")
                .param("firstName", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    @DisplayName("POST /users should create user and return 201")
    void createUserShouldReturnCreated() throws Exception {

        // Arrange
        UserRequestDTO request = new UserRequestDTO(
            "12345678A",
            "John",
            "Wick",
            "john@test.com",
            "password"
        );

        UserResponseDTO response = new UserResponseDTO(
            1,
            "12345678A",
            "John",
            "Wick",
            "john@test.com",
            "ROLE_USER"
        );

        when(userService.create(any(UserRequestDTO.class))).thenReturn(response);

        // Act + Assert
        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    @DisplayName("PUT /users/{id} should update user and return 200")
    void updateUserShouldReturnOk() throws Exception {

        // Arrange
        UpdateUserRequestDTO request = new UpdateUserRequestDTO(
            "12345678A",
            "John",
            "Wick",
            "newPassword123"
        );

        UserResponseDTO response = new UserResponseDTO(
            1,
            "12345678A",
            "John",
            "Wick",
            "john@test.com",
            "ROLE_USER"
        );

        when(userService.update(any(Integer.class), any(UpdateUserRequestDTO.class))).thenReturn(response);

        // Act + Assert
        mockMvc.perform(put("/users/1")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.dni").value("12345678A"))
                .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    @DisplayName("PATCH /users/{id} should partially update user and return 200")
    void patchUserShouldReturnOk() throws Exception {

        // Arrange
        PatchUserRequestDTO request = new PatchUserRequestDTO(
            null,
            "Johnny",
            null,
            null
        );

        UserResponseDTO response = new UserResponseDTO(
            1,
            "12345678A",
            "Johnny",
            "Wick",
            "john@test.com",
            "ROLE_USER"
        );

        when(userService.patch(any(Integer.class), any(PatchUserRequestDTO.class))).thenReturn(response);

        // Act + Assert
        mockMvc.perform(patch("/users/1")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Johnny"))
                .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    @DisplayName("DELETE /users/{id} should return 204")
    void deleteUserShouldReturnNoContent() throws Exception {

        // Act + Assert
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).delete(1);
    }
}