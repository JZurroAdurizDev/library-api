/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.jabierzurro.libraryapi.service;

import com.jabierzurro.libraryapi.dto.BookResponseDTO;
import java.util.List;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public interface BookService {
    public List<BookResponseDTO> getAllBooks();
    public BookResponseDTO getBookById(Integer id);
    public List<BookResponseDTO> search(String title, String author, Short year, String isbn);
}
