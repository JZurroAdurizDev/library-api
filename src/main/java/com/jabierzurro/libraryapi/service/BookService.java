package com.jabierzurro.libraryapi.service;

import com.jabierzurro.libraryapi.dto.BookRequestDTO;
import com.jabierzurro.libraryapi.dto.BookResponseDTO;
import com.jabierzurro.libraryapi.dto.PatchBookRequestDTO;
import java.util.List;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public interface BookService {
    List<BookResponseDTO> getAllBooks();
    BookResponseDTO getBookById(Integer id);
    List<BookResponseDTO> search(String title, String author, Short year, String isbn);
    BookResponseDTO create(BookRequestDTO request);
    BookResponseDTO update(Integer id, BookRequestDTO request);
    BookResponseDTO patch(Integer id, PatchBookRequestDTO request);
    void delete(Integer id);
}
