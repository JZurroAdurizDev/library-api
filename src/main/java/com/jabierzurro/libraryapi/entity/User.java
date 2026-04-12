package com.jabierzurro.libraryapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing an application user.
 *
 * <p>This entity is mapped to the <b>users</b> table and acts as a central model
 * used both in the domain layer (library management) and in the security layer
 * (authentication and authorization).
 *
 * <p>Each user has a unique identifier, personal information, credentials and
 * an associated {@link Role} that defines their permissions within the system.
 *
 * <p>The password is stored as a hashed value and is not exposed in API responses.
 *
 * @author Jabier Zurro Aduriz
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    /**
     * Unique identifier of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * National identification number of the user.
     *
     * <p>This value is unique and used as an additional identifier.
     */
    @Column(nullable = false, unique = true, length = 9)
    private String dni;

    /**
     * First name of the user.
     */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    /**
     * Last name of the user.
     */
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    /**
     * Email address of the user.
     *
     * <p>This value is unique and is typically used for authentication.
     */
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Hashed password of the user.
     *
     * <p>This field is ignored in JSON serialization to prevent exposure
     * of sensitive data.
     */
    @JsonIgnore
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    /**
     * Role assigned to the user.
     *
     * <p>This relationship is used by the security layer to determine
     * access permissions.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    
    public User(String dni, String firstName, String lastName, String email, String passwordHash, Role role) {
        this.dni = dni;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}