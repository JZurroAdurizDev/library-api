package com.jabierzurro.libraryapi.repository;

import com.jabierzurro.libraryapi.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link Role} entities.
 *
 * <p>This repository provides access to role data stored in the database
 * and allows lookup operations required by the security layer.
 *
 * @author Jabier Zurro Aduriz
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Finds a role by its name.
     *
     * @param roleName name of the role to search for
     * @return an {@link Optional} containing the matching role if found
     */
    Optional<Role> findByRoleName(String roleName);
}