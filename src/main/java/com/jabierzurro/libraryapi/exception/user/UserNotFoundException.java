/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jabierzurro.libraryapi.exception.user;

import com.jabierzurro.libraryapi.exception.base.NotFoundException;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Integer id) {
        super("User with id " + id + " not found");
    }
}
