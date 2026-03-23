/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jabierzurro.libraryapi.service;

import com.jabierzurro.libraryapi.dto.BookResponseDTO;
import com.jabierzurro.libraryapi.entity.Book;
import com.jabierzurro.libraryapi.exception.base.NotFoundException;
import com.jabierzurro.libraryapi.repository.BookRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jabier Zurro Aduriz
 */
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    @Override
    public List<BookResponseDTO> getAllBooks() {
        List<Book> books = this.bookRepository.findAll();
        if (books.isEmpty()) {
            throw new NotFoundException("No books found in the database.") {};
        }
        return books.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponseDTO getBookById(Integer id) {
        Book book = this.bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found") {});
        return convertToResponseDTO(book);
    }

    @Override
    public List<BookResponseDTO> search(String title, String author, Short year, String isbn) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private BookResponseDTO convertToResponseDTO(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear(),
                book.getPages()
        );
    }
}
