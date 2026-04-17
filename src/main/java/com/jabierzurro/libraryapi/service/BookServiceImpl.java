package com.jabierzurro.libraryapi.service;

import com.jabierzurro.libraryapi.dto.BookRequestDTO;
import com.jabierzurro.libraryapi.dto.BookResponseDTO;
import com.jabierzurro.libraryapi.dto.PatchBookRequestDTO;
import com.jabierzurro.libraryapi.entity.Book;
import com.jabierzurro.libraryapi.exception.base.NotFoundException;
import com.jabierzurro.libraryapi.exception.book.BookConflictException;
import com.jabierzurro.libraryapi.exception.book.BookNotFoundException;
import com.jabierzurro.libraryapi.repository.BookRepository;
import java.util.List;
import static org.flywaydb.core.internal.util.StringUtils.hasText;
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
                .map(BookServiceImpl::toResponseDTO)
                .toList();
    }

    @Override
    public BookResponseDTO getBookById(Integer id) {
        Book book = this.bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found") {});
        return toResponseDTO(book);
    }

    @Override
    public List<BookResponseDTO> search(String title, String author, Short year, String isbn) {
        List<Book> books = this.bookRepository.searchBooks(title, author, year, isbn);
        return books.stream()
                .map(BookServiceImpl::toResponseDTO)
                .toList();
    }
    
    @Override
    public BookResponseDTO create(BookRequestDTO request) {
        if (bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
            throw BookConflictException.isbnAlreadyExists(request.getIsbn());
        }
        
        Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getIsbn(),
                request.getPublishedYear(),
                request.getPages()
        );
        
        Book createdBook = bookRepository.save(book);
        
        return BookServiceImpl.toResponseDTO(createdBook);
    }

    @Override
    public BookResponseDTO update(Integer id, BookRequestDTO request) {
        Book findBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        
        bookRepository.findByIsbn(request.getIsbn())
                .filter(book -> !book.getId().equals(id))
                .ifPresent(book -> {
                    throw BookConflictException.isbnAlreadyExists(request.getIsbn());
                });
        
        findBook.setTitle(request.getTitle());
        findBook.setAuthor(request.getAuthor());
        findBook.setIsbn(request.getIsbn());
        findBook.setPublishedYear(request.getPublishedYear());
        findBook.setPages(request.getPages());
        
        Book updatedBook = bookRepository.save(findBook);
        return BookServiceImpl.toResponseDTO(updatedBook);
    }

    @Override
    public BookResponseDTO patch(Integer id, PatchBookRequestDTO request) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        
        if(hasText(request.getIsbn())) {
            bookRepository.findByIsbn(request.getIsbn())
                    .filter(book -> !book.getId().equals(id))
                    .ifPresent(book -> {
                       throw BookConflictException.isbnAlreadyExists(request.getIsbn());
                    });
        }
        
        if (hasText(request.getTitle())) {
            existingBook.setTitle(request.getTitle());
        }

        if (hasText(request.getAuthor())) {
            existingBook.setAuthor(request.getAuthor());
        }

        if (hasText(request.getIsbn())) {
            existingBook.setIsbn(request.getIsbn());
        }
        
        if (request.getPublishedYear() != null) {
            existingBook.setPublishedYear(request.getPublishedYear());
        }
        
        if (request.getPages() != null) {
            existingBook.setPages(request.getPages());
        }
        
        Book patchedBook = bookRepository.save(existingBook);
        return BookServiceImpl.toResponseDTO(patchedBook);
    }

    @Override
    public void delete(Integer id) {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new BookNotFoundException(id));
            this.bookRepository.delete(book);
    }
    
    private static BookResponseDTO toResponseDTO(Book book) {
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
