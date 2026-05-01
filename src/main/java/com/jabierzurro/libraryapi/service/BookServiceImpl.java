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
 * Service implementation for book-related business operations.
 *
 * <p>This class contains the business logic for managing books, including
 * retrieval, search, creation, full updates, partial updates and deletion.
 *
 * <p>It enforces domain rules such as unique ISBN values before persistence
 * and transforms entity objects into response DTOs before returning them
 * to the controller layer.
 *
 * <p>This service acts as the bridge between the controller layer and the
 * persistence layer represented by the repository.
 *
 * @author Jabier Zurro Aduriz
 */
@Service
public class BookServiceImpl implements BookService {

    /**
     * Repository used to access book persistence operations.
     */
    private final BookRepository bookRepository;

    /**
     * Creates the service with the required book repository dependency.
     *
     * @param bookRepository repository used for book data access
     */
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Retrieves all books stored in the system.
     *
     * @return list of books as {@link BookResponseDTO}
     * @throws NotFoundException if no books are found in the database
     */
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

    /**
     * Retrieves a book by its unique identifier.
     *
     * @param id book identifier
     * @return the book as {@link BookResponseDTO}
     * @throws NotFoundException if no book is found with the given id
     */
    @Override
    public BookResponseDTO getBookById(Integer id) {
        Book book = this.bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found") {});
        return toResponseDTO(book);
    }

    /**
     * Searches books based on optional filtering criteria.
     *
     * <p>The filtering logic is delegated to the repository layer.
     * Any parameter may be null, in which case it is ignored.
     *
     * @param title optional title filter
     * @param author optional author filter
     * @param year optional publication year filter
     * @param isbn optional ISBN filter
     * @return list of matching books as {@link BookResponseDTO}
     */
    @Override
    public List<BookResponseDTO> search(String title, String author, Short year, String isbn) {
        List<Book> books = this.bookRepository.searchBooks(title, author, year, isbn);
        return books.stream()
                .map(BookServiceImpl::toResponseDTO)
                .toList();
    }

    /**
     * Creates a new book after validating business constraints.
     *
     * <p>This method ensures that the ISBN value is unique before creating
     * the book.
     *
     * @param request DTO containing book creation data
     * @return the created book as {@link BookResponseDTO}
     * @throws BookConflictException if the ISBN already exists
     */
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

    /**
     * Fully updates an existing book.
     *
     * <p>This method replaces the updatable fields of the book with the values
     * provided in the request. It also checks ISBN uniqueness before applying
     * the update.
     *
     * @param id book identifier
     * @param request DTO containing updated book data
     * @return the updated book as {@link BookResponseDTO}
     * @throws BookNotFoundException if the book does not exist
     * @throws BookConflictException if the new ISBN already belongs to another book
     */
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

    /**
     * Partially updates an existing book.
     *
     * <p>Only the fields present and valid in the request are updated.
     * Existing values remain unchanged when a field is omitted.
     *
     * <p>If an ISBN is provided, uniqueness is validated before applying
     * the change.
     *
     * @param id book identifier
     * @param request DTO containing partial book data
     * @return the updated book as {@link BookResponseDTO}
     * @throws BookNotFoundException if the book does not exist
     * @throws BookConflictException if the provided ISBN already belongs to another book
     */
    @Override
    public BookResponseDTO patch(Integer id, PatchBookRequestDTO request) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (hasText(request.getIsbn())) {
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

    /**
     * Deletes a book by its identifier.
     *
     * @param id book identifier
     * @throws BookNotFoundException if no book exists with the given id
     */
    @Override
    public void delete(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        this.bookRepository.delete(book);
    }

    /**
     * Maps a {@link Book} entity to its response DTO representation.
     *
     * @param book book entity
     * @return mapped {@link BookResponseDTO}
     */
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