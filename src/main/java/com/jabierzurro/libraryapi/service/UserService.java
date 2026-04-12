package com.jabierzurro.libraryapi.service;

import com.jabierzurro.libraryapi.dto.PatchUserRequestDTO;
import com.jabierzurro.libraryapi.dto.UpdateUserRequestDTO;
import com.jabierzurro.libraryapi.dto.UserRequestDTO;
import com.jabierzurro.libraryapi.dto.UserResponseDTO;
import java.util.List;

/**
 * Service interface for user-related business operations.
 *
 * <p>This interface defines the contract for managing users in the application,
 * including retrieval, search, creation, full update, partial update and deletion.
 *
 * <p>Implementations of this service are responsible for applying business rules,
 * validating constraints and coordinating repository access.
 *
 * @author Jabier Zurro Aduriz
 */
public interface UserService {

    /**
     * Retrieves all users in the system.
     *
     * @return list of users as {@link UserResponseDTO}
     */
    List<UserResponseDTO> getAllUsers();

    /**
     * Retrieves a user by its unique identifier.
     *
     * @param id user identifier
     * @return the user as {@link UserResponseDTO}
     */
    UserResponseDTO getUserById(Integer id);

    /**
     * Searches users based on optional filtering criteria.
     *
     * @param firstName optional first name filter
     * @param lastName optional last name filter
     * @param email optional email filter
     * @param dni optional DNI filter
     * @return list of users matching the provided filters
     */
    List<UserResponseDTO> search(String firstName, String lastName, String email, String dni);

    /**
     * Creates a new user.
     *
     * @param request DTO containing user creation data
     * @return the created user as {@link UserResponseDTO}
     */
    UserResponseDTO create(UserRequestDTO request);

    /**
     * Fully updates an existing user.
     *
     * @param id user identifier
     * @param request DTO containing updated user data
     * @return the updated user as {@link UserResponseDTO}
     */
    UserResponseDTO update(Integer id, UpdateUserRequestDTO request);

    /**
     * Partially updates an existing user.
     *
     * @param id user identifier
     * @param request DTO containing partial user data
     * @return the updated user as {@link UserResponseDTO}
     */
    UserResponseDTO patch(Integer id, PatchUserRequestDTO request);

    /**
     * Deletes a user by its identifier.
     *
     * @param id user identifier
     */
    void delete(Integer id);

}