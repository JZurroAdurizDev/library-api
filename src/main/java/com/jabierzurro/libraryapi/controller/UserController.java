package com.jabierzurro.libraryapi.controller;


import com.jabierzurro.libraryapi.dto.PatchUserRequestDTO;
import com.jabierzurro.libraryapi.dto.UpdateUserRequestDTO;
import com.jabierzurro.libraryapi.dto.UserRequestDTO;
import com.jabierzurro.libraryapi.dto.UserResponseDTO;
import com.jabierzurro.libraryapi.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for managing user-related operations.
 *
 * <p>This controller exposes endpoints for creating, retrieving, updating,
 * partially updating and deleting users. It acts as the entry point of the
 * application for all user-related requests and delegates business logic
 * to the {@link UserService}.
 *
 * <p>All responses are returned as {@link UserResponseDTO} objects, ensuring
 * that sensitive information such as passwords is never exposed.
 *
 * @author Jabier Zurro Aduriz
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Retrieves all users in the system.
     *
     * @return list of users as {@link UserResponseDTO}
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Retrieves a user by its unique identifier.
     *
     * @param id user identifier
     * @return the user as {@link UserResponseDTO}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Searches users based on optional filtering criteria.
     *
     * <p>All parameters are optional. If no parameters are provided,
     * all users will be returned.
     *
     * @param firstName optional first name filter
     * @param lastName optional last name filter
     * @param email optional email filter
     * @param dni optional DNI filter
     * @return list of matching users
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String dni
    ) {
        return ResponseEntity.ok(userService.search(firstName, lastName, email, dni));
    }

    /**
     * Creates a new user in the system.
     *
     * <p>This endpoint validates the incoming request and delegates
     * user creation to the service layer. Business rules such as
     * unique email and DNI are enforced at service level.
     *
     * @param request DTO containing user data
     * @return the created user as {@link UserResponseDTO}
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO request
    ) {
        return ResponseEntity.status(201).body(userService.create(request));
    }

    /**
     * Updates an existing user.
     *
     * <p>This operation replaces the user data with the provided values.
     * Only non-sensitive fields are updated through this endpoint.
     *
     * @param id user identifier
     * @param request DTO containing updated user data
     * @return the updated user as {@link UserResponseDTO}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUserRequestDTO request
    ) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    /**
     * Partially updates an existing user.
     *
     * <p>Only the fields present in the request will be updated.
     * Fields that are null or absent will remain unchanged.
     *
     * @param id user identifier
     * @param request DTO containing partial user data
     * @return the updated user as {@link UserResponseDTO}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> patchUser(
            @PathVariable Integer id,
            @Valid @RequestBody PatchUserRequestDTO request
    ) {
        return ResponseEntity.ok(userService.patch(id, request));
    }

    /**
     * Deletes a user by its identifier.
     *
     * @param id user identifier
     * @return empty response with HTTP 204 status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}