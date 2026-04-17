package com.jabierzurro.libraryapi.controller;

import com.jabierzurro.libraryapi.dto.BookRequestDTO;
import com.jabierzurro.libraryapi.dto.BookResponseDTO;
import com.jabierzurro.libraryapi.dto.PatchBookRequestDTO;
import com.jabierzurro.libraryapi.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Jabier Zurro Aduriz
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Short year,
            @RequestParam(required = false) String isbn
    ) {
        return ResponseEntity.ok(bookService.search(title, author, year, isbn));
    }
    
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(
            @Valid @RequestBody BookRequestDTO request
    ) {
        return ResponseEntity.status(201).body(bookService.create(request));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Integer id,
            @Valid @RequestBody BookRequestDTO request
    ){
        return ResponseEntity.ok(bookService.update(id, request));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<BookResponseDTO> patchBook(
            @PathVariable Integer id,
            @Valid @RequestBody PatchBookRequestDTO request
    ) {
        return ResponseEntity.ok(bookService.patch(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    } 
}
