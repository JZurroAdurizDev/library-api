package com.jabierzurro.libraryapi.service;

import com.jabierzurro.libraryapi.dto.PatchUserRequestDTO;
import com.jabierzurro.libraryapi.dto.UserRequestDTO;
import com.jabierzurro.libraryapi.dto.UserResponseDTO;
import java.util.List;


/**
 *
 * @author Jabier Zurro Aduriz
 */
public interface UserService {

    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Integer id);
    List<UserResponseDTO> search(String firstName, String lastName, String email, String dni);
    UserResponseDTO create(UserRequestDTO request);
    UserResponseDTO update(Integer id, UserRequestDTO request);
    UserResponseDTO patch(Integer id, PatchUserRequestDTO request);
    void delete(Integer id);
    
}
