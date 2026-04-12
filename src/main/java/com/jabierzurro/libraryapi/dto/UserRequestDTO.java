package com.jabierzurro.libraryapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO used for creating a new user.
 *
 * <p>This DTO is used in POST operations and requires all fields
 * to be provided and valid. It ensures that a new user is created
 * with complete and consistent data.
 *
 * <p>Validation constraints guarantee that mandatory fields are not blank
 * and that the provided values meet size and format requirements.
 *
 * <p>The password is received in plain text and will be encoded
 * before being stored in the system.
 *
 * @author Jabier Zurro Aduriz
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequestDTO {

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
     * Email address of the user.
     *
     * <p>This field is required and must follow a valid email format.
     */
    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    /**
     * Password of the user.
     *
     * <p>This field is required and must not be blank. It will be
     * encoded before persistence.
     */
    @NotBlank
    @Size(max = 255)
    private String password;
}