package com.jabierzurro.libraryapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO used for updating a user.
 *
 * <p>This DTO is intended for PUT operations, where the user data
 * is expected to be fully replaced with the provided values.
 *
 * <p>All main fields are required and must not be blank. This ensures
 * that the entity remains in a consistent state after the update.
 *
 * <p>The password field is optional. If provided, it will be encoded
 * before being stored.
 *
 * @author Jabier Zurro Aduriz
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserRequestDTO {

    /**
     * DNI of the user.
     *
     * <p>This field is required and must not be blank.
     */
    @NotBlank
    @Size(max = 9)
    private String dni;

    /**
     * First name of the user.
     *
     * <p>This field is required and must not be blank.
     */
    @NotBlank
    @Size(max = 100)
    private String firstName;

    /**
     * Last name of the user.
     *
     * <p>This field is required and must not be blank.
     */
    @NotBlank
    @Size(max = 100)
    private String lastName;

    /**
     * Password of the user.
     *
     * <p>This field is optional. If provided, it will be encoded
     * before persistence.
     */
    @Size(max = 255)
    private String password;

}