package com.jabierzurro.libraryapi.repository;

import com.jabierzurro.libraryapi.entity.Role;
import com.jabierzurro.libraryapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

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

    Optional<User> findByEmail(String email);
    Optional<User> findByDni(String dni);
}