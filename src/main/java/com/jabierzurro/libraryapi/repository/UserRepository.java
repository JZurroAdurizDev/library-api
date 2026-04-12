package com.jabierzurro.libraryapi.repository;

import com.jabierzurro.libraryapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link User} entity.
 *
 * <p>This interface provides data access methods for user persistence,
 * including standard CRUD operations inherited from {@link JpaRepository}
 * and custom query methods for specific use cases.
 *
 * <p>It is used by the service layer to interact with the database.
 *
 * @author Jabier Zurro Aduriz
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Searches users based on optional filtering criteria.
     *
     * <p>This query allows dynamic filtering by first name, last name,
     * email and DNI. All parameters are optional; if a parameter is null,
     * it will be ignored in the filtering process.
     *
     * <p>Partial matching is applied for first name and last name,
     * while email and DNI require exact matches.
     *
     * @param firstName optional first name filter
     * @param lastName optional last name filter
     * @param email optional email filter
     * @param dni optional DNI filter
     * @return list of users matching the criteria
     */
    @Query(value = "SELECT * FROM users u WHERE " +
            "(:firstName IS NULL OR LOWER(u.first_name) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(u.last_name) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:email IS NULL OR LOWER(u.email) = LOWER(:email)) AND " +
            "(:dni IS NULL OR u.dni = :dni)",
            nativeQuery = true)
    List<User> searchUsers(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("dni") String dni
    );

    /**
     * Finds a user by email.
     *
     * <p>This method is typically used during authentication
     * and user creation to ensure uniqueness.
     *
     * @param email user email
     * @return optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by DNI.
     *
     * <p>This method is used to enforce uniqueness of the DNI
     * during user creation and updates.
     *
     * @param dni user DNI
     * @return optional containing the user if found
     */
    Optional<User> findByDni(String dni);
}