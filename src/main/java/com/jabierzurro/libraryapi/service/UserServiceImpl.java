package com.jabierzurro.libraryapi.service;

import com.jabierzurro.libraryapi.dto.PatchUserRequestDTO;
import com.jabierzurro.libraryapi.dto.UpdateUserRequestDTO;
import com.jabierzurro.libraryapi.dto.UserRequestDTO;
import com.jabierzurro.libraryapi.dto.UserResponseDTO;
import com.jabierzurro.libraryapi.entity.Role;
import com.jabierzurro.libraryapi.entity.User;
import com.jabierzurro.libraryapi.exception.user.UserNotFoundException;
import com.jabierzurro.libraryapi.exception.role.RoleNotFoundException;
import com.jabierzurro.libraryapi.exception.user.UserConflictException;
import com.jabierzurro.libraryapi.repository.RoleRepository;
import com.jabierzurro.libraryapi.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import static org.flywaydb.core.internal.util.StringUtils.hasText;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jabier Zurro Aduriz
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return this.userRepository.findAll().stream()
                .map(UserServiceImpl::toResponseDTO)
                .toList();
    }

    @Override
    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserServiceImpl.toResponseDTO(user);
               
    }

    @Override
    public List<UserResponseDTO> search(String firstName, String lastName, String email, String dni) {
        List<User> users = this.userRepository.searchUsers(firstName, lastName, email, dni);
        return users.stream()
                .map(UserServiceImpl::toResponseDTO)
                .toList();
    }

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

    @Override
    public void delete(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        this.userRepository.delete(user);
    }

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
