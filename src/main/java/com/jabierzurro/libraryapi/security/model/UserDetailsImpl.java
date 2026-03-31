package com.jabierzurro.libraryapi.security.model;

import com.jabierzurro.libraryapi.entity.User;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Implementation of {@link UserDetails} used by Spring Security.
 *
 * <p>This class acts as an adapter between the application's {@link User}
 * entity and Spring Security's authentication system.
 *
 * <p>It encapsulates user credentials and authorities, allowing Spring Security
 * to perform authentication and authorization checks.
 *
 * <p>The user's email is used as the username, and the role is mapped to
 * a {@link GrantedAuthority} following the standard {@code ROLE_*} convention.
 *
 * @author Jabier Zurro Aduriz
 */
public final class UserDetailsImpl implements UserDetails {

    private final Integer id;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * Private constructor used internally to build instances.
     *
     * @param id user identifier
     * @param email user's email (used as username)
     * @param password hashed password
     * @param authorities granted authorities derived from the user's role
     */
    private UserDetailsImpl(
            Integer id,
            String email,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Factory method that converts a {@link User} entity into a
     * {@link UserDetailsImpl} instance.
     *
     * <p>The user's role is transformed into a {@link GrantedAuthority}
     * using the {@code ROLE_*} naming convention required by Spring Security.
     *
     * @param u user entity
     * @return {@link UserDetailsImpl} instance representing the user
     */
    public static UserDetailsImpl from(User u) {
        String roleName = u.getRole().getRoleName();

        return new UserDetailsImpl(
                u.getId(),
                u.getEmail(),
                u.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + roleName))
        );
    }

    /**
     * Returns the unique identifier of the user.
     *
     * @return user ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the authorities granted to the user.
     *
     * @return collection of {@link GrantedAuthority}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Returns the user's password.
     *
     * @return hashed password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username used for authentication.
     *
     * <p>In this application, the email is used as the username.
     *
     * @return user's email
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates whether the account has expired.
     *
     * @return always {@code true} (no expiration logic implemented)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the account is locked.
     *
     * @return always {@code true} (no locking logic implemented)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the credentials have expired.
     *
     * @return always {@code true} (no expiration logic implemented)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the account is enabled.
     *
     * @return always {@code true} (no enable/disable logic implemented)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}