/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jabierzurro.libraryapi.exception.book;

import com.jabierzurro.libraryapi.exception.base.NotFoundException;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public class BookNotFoundException extends NotFoundException {

    public BookNotFoundException(Integer id) {
        super("Book with id " + id + " not found");
    }
}
