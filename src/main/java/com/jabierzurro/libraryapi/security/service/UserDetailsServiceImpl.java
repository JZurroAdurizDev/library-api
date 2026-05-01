package com.jabierzurro.libraryapi.security.service;

import com.jabierzurro.libraryapi.entity.User;
import com.jabierzurro.libraryapi.repository.UserRepository;
import com.jabierzurro.libraryapi.security.model.UserDetailsImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserDetailsService} used by Spring Security
 * to retrieve user information during the authentication process.
 *
 * <p>This service loads a user from the database using their email and
 * transforms it into a {@link UserDetails} instance understood by
 * Spring Security.
 *
 * <p>The flow is:
 * <ul>
 *     <li>Spring Security receives login credentials,</li>
 *     <li>calls this service to load the user,</li>
 *     <li>the user is retrieved from the database,</li>
 *     <li>it is mapped to {@link UserDetailsImpl},</li>
 *     <li>authentication continues using the returned object.</li>
 * </ul>
 *
 * <p>This implementation uses email as the username.
 *
 * @author Jabier Zurro Aduriz
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    
    /**
     * Constructor for injecting the user repository.
     *
     * @param userRepository repository used to retrieve users from the database
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository; 
    }

    /**
     * Loads a user by their email.
     *
     * <p>This method is used internally by Spring Security during authentication.
     * If the user is not found, a {@link UsernameNotFoundException} is thrown.
     *
     * @param email the user's email (used as username)
     * @return a {@link UserDetails} representation of the user
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return UserDetailsImpl.from(u); 
    }
}