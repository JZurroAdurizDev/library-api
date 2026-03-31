package com.jabierzurro.libraryapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a security role within the application.
 *
 * <p>This entity is mapped to the <b>roles</b> table and defines the different
 * roles that can be assigned to users (e.g., ROLE_USER, ROLE_ADMIN).
 *
 * <p>Each role has a unique name that is used by Spring Security to determine
 * access permissions.
 *
 * @author Jabier Zurro Aduriz
 */
@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {

    /**
     * Unique identifier of the role.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Name of the role (e.g., ROLE_USER, ROLE_ADMIN).
     *
     * <p>This value must be unique and is used by the security layer
     * to grant or restrict access to resources.
     */
    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    private String roleName;
}