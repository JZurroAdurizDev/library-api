package com.jabierzurro.libraryapi.service;

import com.jabierzurro.libraryapi.dto.PatchUserRequestDTO;
import com.jabierzurro.libraryapi.dto.UpdateUserRequestDTO;
import com.jabierzurro.libraryapi.dto.UserRequestDTO;
import com.jabierzurro.libraryapi.dto.UserResponseDTO;
import com.jabierzurro.libraryapi.entity.Role;
import com.jabierzurro.libraryapi.entity.User;
import com.jabierzurro.libraryapi.exception.role.RoleNotFoundException;
import com.jabierzurro.libraryapi.exception.user.UserConflictException;
import com.jabierzurro.libraryapi.exception.user.UserNotFoundException;
import com.jabierzurro.libraryapi.repository.RoleRepository;
import com.jabierzurro.libraryapi.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import static org.flywaydb.core.internal.util.StringUtils.hasText;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for user-related business operations.
 *
 * <p>This class contains the business logic for managing users, including
 * retrieval, search, creation, full updates, partial updates and deletion.
 *
 * <p>It also enforces domain rules such as unique email and DNI values,
 * password encoding before persistence and default role assignment during
 * user creation.
 *
 * <p>This service acts as the bridge between the controller layer and the
 * persistence layer represented by the repositories.
 *
 * @author Jabier Zurro Aduriz
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * Repository used to access user persistence operations.
     */
    private final UserRepository userRepository;

    /**
     * Repository used to retrieve roles from the database.
     */
    private final RoleRepository roleRepository;

    /**
     * Password encoder used to hash plain text passwords before persistence.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves all users stored in the system.
     *
     * @return list of users as {@link UserResponseDTO}
     */
    @Override
    public List<UserResponseDTO> getAllUsers() {
        return this.userRepository.findAll().stream()
                .map(UserServiceImpl::toResponseDTO)
                .toList();
    }

    /**
     * Retrieves a user by its unique identifier.
     *
     * @param id user identifier
     * @return the user as {@link UserResponseDTO}
     * @throws UserNotFoundException if no user is found with the given id
     */
    @Override
    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserServiceImpl.toResponseDTO(user);

    }

    /**
     * Searches users based on optional filtering criteria.
     *
     * <p>The filtering logic is delegated to the repository layer.
     * Any parameter may be null, in which case it is ignored.
     *
     * @param firstName optional first name filter
     * @param lastName optional last name filter
     * @param email optional email filter
     * @param dni optional DNI filter
     * @return list of matching users as {@link UserResponseDTO}
     */
    @Override
    public List<UserResponseDTO> search(String firstName, String lastName, String email, String dni) {
        List<User> users = this.userRepository.searchUsers(firstName, lastName, email, dni);
        return users.stream()
                .map(UserServiceImpl::toResponseDTO)
                .toList();
    }

    /**
     * Creates a new user after validating business constraints.
     *
     * <p>This method ensures that both email and DNI are unique before
     * creating the user. It also encodes the password and assigns the
     * default {@code ROLE_USER} role.
     *
     * @param request DTO containing user creation data
     * @return the created user as {@link UserResponseDTO}
     * @throws UserConflictException if the email or DNI already exists
     * @throws RoleNotFoundException if the default role cannot be found
     */
    @Override
    public UserResponseDTO create(UserRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw UserConflictException.emailAlreadyExists(request.getEmail());
        }
        if (userRepository.findByDni(request.getDni()).isPresent()) {
            throw UserConflictException.dniAlreadyExists(request.getDni());
        }

        String passwordHash = this.passwordEncoder.encode(request.getPassword());
        Role role = roleRepository.findByRoleName("ROLE_USER")
        .orElseThrow(() -> new RoleNotFoundException("ROLE_USER"));

        User user = new User(
            request.getDni(),
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            passwordHash,
            role
        );

        User createdUser = userRepository.save(user);

        return UserServiceImpl.toResponseDTO(createdUser);
    }

    /**
     * Fully updates an existing user.
     *
     * <p>This method replaces the updatable fields of the user with the values
     * provided in the request. It also checks DNI uniqueness and encodes the
     * password if a non-blank value is provided.
     *
     * @param id user identifier
     * @param request DTO containing updated user data
     * @return the updated user as {@link UserResponseDTO}
     * @throws UserNotFoundException if the user does not exist
     * @throws UserConflictException if the new DNI already belongs to another user
     */
    @Override
    public UserResponseDTO update(Integer id, UpdateUserRequestDTO request) {
        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.findByDni(request.getDni())
            .filter(user -> !user.getId().equals(id))
            .ifPresent(user -> {
                throw UserConflictException.dniAlreadyExists(request.getDni());
        });

        if(hasText(request.getPassword())) {
            findUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        findUser.setDni(request.getDni());
        findUser.setFirstName(request.getFirstName());
        findUser.setLastName(request.getLastName());

        User updatedUser = userRepository.save(findUser);

        return UserServiceImpl.toResponseDTO(updatedUser);
    }

    /**
     * Partially updates an existing user.
     *
     * <p>Only the fields present and non-blank in the request are updated.
     * Existing values remain unchanged when a field is omitted.
     *
     * <p>If a DNI is provided, uniqueness is validated before applying the change.
     * If a password is provided, it is encoded before persistence.
     *
     * @param id user identifier
     * @param request DTO containing partial user data
     * @return the updated user as {@link UserResponseDTO}
     * @throws UserNotFoundException if the user does not exist
     * @throws UserConflictException if the provided DNI already belongs to another user
     */
    @Override
    public UserResponseDTO patch(Integer id, PatchUserRequestDTO request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (hasText(request.getDni())) {
            userRepository.findByDni(request.getDni())
                    .filter(user -> !user.getId().equals(id))
                    .ifPresent(user -> {
                        throw UserConflictException.dniAlreadyExists(request.getDni());
                    });

            existingUser.setDni(request.getDni());
        }

        if (hasText(request.getFirstName())) {
            existingUser.setFirstName(request.getFirstName());
        }

        if (hasText(request.getLastName())) {
            existingUser.setLastName(request.getLastName());
        }

        if (hasText(request.getPassword())) {
            existingUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        User patchedUser = userRepository.save(existingUser);

        return UserServiceImpl.toResponseDTO(patchedUser);
    }

    /**
     * Deletes a user by its identifier.
     *
     * @param id user identifier
     * @throws UserNotFoundException if no user exists with the given id
     */
    @Override
    public void delete(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        this.userRepository.delete(user);
    }

    /**
     * Maps a {@link User} entity to its response DTO representation.
     *
     * <p>This method transforms the internal entity into a safe API response
     * that excludes sensitive information such as the password hash.
     *
     * @param user user entity
     * @return mapped {@link UserResponseDTO}
     */
    private static UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getDni(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().getRoleName()
        );
    }
}