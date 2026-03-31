package com.jabierzurro.libraryapi.security.service;

import com.jabierzurro.libraryapi.entity.Role;
import com.jabierzurro.libraryapi.entity.User;
import com.jabierzurro.libraryapi.repository.RoleRepository;
import com.jabierzurro.libraryapi.repository.UserRepository;
import com.jabierzurro.libraryapi.security.dto.AuthResponseDTO;
import com.jabierzurro.libraryapi.security.dto.LoginRequestDTO;
import com.jabierzurro.libraryapi.security.dto.RegisterRequestDTO;
import com.jabierzurro.libraryapi.security.util.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AuthService} responsible for authentication operations.
 *
 * <p>This service handles both login and user registration.
 *
 * <p>Its responsibilities include:
 * <ul>
 *     <li>authenticating existing users through Spring Security,</li>
 *     <li>registering new users in the database,</li>
 *     <li>assigning the default security role to newly registered users,</li>
 *     <li>generating JWT tokens for authenticated users.</li>
 * </ul>
 *
 * <p>After a successful login or registration, the service returns an
 * {@link AuthResponseDTO} containing the generated JWT token and its metadata.
 *
 * @author Jabier Zurro Aduriz
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.jwt.expiration}")
    private long expirationMillis;

    /**
     * Constructor for injecting authentication and persistence dependencies.
     *
     * @param authenticationManager manager responsible for validating credentials
     * @param jwtService service used to generate JWT tokens
     * @param userRepository repository used to access users
     * @param roleRepository repository used to access roles
     * @param passwordEncoder encoder used to hash user passwords
     */
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticates a user and generates a JWT token if the credentials are valid.
     *
     * <p>This method delegates authentication to Spring Security and, upon success,
     * creates a signed JWT token representing the authenticated user.
     *
     * @param request login request containing user credentials
     * @return authentication response containing the JWT token and its expiration time
     */
    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String jwt = jwtService.createToken(auth);
        return new AuthResponseDTO(jwt, this.expirationMillis);
    }

    /**
     * Registers a new user and returns a JWT token for the newly created account.
     *
     * <p>This method:
     * <ul>
     *     <li>checks that the email is not already in use,</li>
     *     <li>loads the default role for new users,</li>
     *     <li>creates and stores the user with an encoded password,</li>
     *     <li>authenticates the newly registered user,</li>
     *     <li>generates a JWT token for immediate access.</li>
     * </ul>
     *
     * <p>As a result, the user can access protected endpoints immediately
     * after registration without performing a separate login request.
     *
     * @param request registration request containing user data
     * @return authentication response containing the JWT token and its expiration time
     * @throws RuntimeException if the email is already in use or the default role is missing
     */
    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        Role role = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = new User();
        user.setDni(request.dni());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(role);

        userRepository.save(user);

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String jwt = jwtService.createToken(auth);
        return new AuthResponseDTO(jwt, this.expirationMillis);
    }
}