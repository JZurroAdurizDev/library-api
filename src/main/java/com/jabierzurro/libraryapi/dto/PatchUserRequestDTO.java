package com.jabierzurro.libraryapi.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO used for partially updating a user.
 *
 * <p>All fields are optional. Only the fields provided in the request
 * will be updated, while the rest will remain unchanged.
 *
 * <p>This DTO is typically used in PATCH operations where partial
 * modifications are required instead of replacing the entire entity.
 *
 * <p>Validation constraints are applied only to limit field sizes,
 * but null values are allowed.
 *
 * @author Jabier Zurro Aduriz
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatchUserRequestDTO {

    /**
     * Optional DNI of the user.
     */
    @Size(max = 9)
    private String dni;

    /**
     * Optional first name of the user.
     */
    @Size(max = 100)
    private String firstName;

    /**
     * Optional last name of the user.
     */
    @Size(max = 100)
    private String lastName;

    /**
     * Optional password of the user.
     *
     * <p>If provided, it will be encoded before being stored.
     */
    @Size(max = 255)
    private String password;
}