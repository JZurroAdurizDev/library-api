package com.jabierzurro.libraryapi.service;

import com.jabierzurro.libraryapi.dto.LoanRequestDTO;
import com.jabierzurro.libraryapi.entity.Book;
import com.jabierzurro.libraryapi.entity.LoanStatus;
import com.jabierzurro.libraryapi.entity.User;
import com.jabierzurro.libraryapi.exception.base.ConflictException;
import com.jabierzurro.libraryapi.exception.base.NotFoundException;
import com.jabierzurro.libraryapi.exception.loan.LoanConflictException;
import com.jabierzurro.libraryapi.exception.user.UserNotFoundException;
import com.jabierzurro.libraryapi.repository.BookRepository;
import com.jabierzurro.libraryapi.repository.LoanRepository;
import com.jabierzurro.libraryapi.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit test class for {@link LoanServiceImpl}.
 *
 * <p>This test suite verifies the business logic of loan creation in isolation,
 * using Mockito to mock repository dependencies.
 *
 * <p>The tests focus on validating core business rules and conflict scenarios,
 * ensuring that invalid operations correctly throw exceptions.
 *
 * <p>Each test follows the Arrange-Act-Assert pattern for clarity.
 *
 * @author Jabier Zurro Aduriz
 */
@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LoanServiceImpl loanService;
    
    /**
     * Verifies that creating a loan fails when the user already has an active loan.
     *
     * @throws LoanConflictException if the user already has an active loan
     */
    @Test
    @DisplayName("Should throw exception when user has active loan")
    void shouldThrowExceptionWhenUserHasActiveLoan() {
        
        // Arrange
        LoanRequestDTO request = new LoanRequestDTO(
            1,
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(5),
            List.of(1)
        );

        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(loanRepository.existsByUser_IdAndStatus(1, LoanStatus.ACTIVE))
            .thenReturn(true);

        // Act + Assert
        LoanConflictException ex = assertThrows(
            LoanConflictException.class,
            () -> loanService.create(request)
        );

        assertEquals("User with id 1 already has an active loan", ex.getMessage());
    }
    
    /**
     * Verifies that creating a loan fails when more than five books are requested.
     *
     * @throws ConflictException if the number of books exceeds the allowed limit
     */
    @Test
    @DisplayName("Should throw exception when more than five books are requested")
    void shouldThrowExceptionWhenMoreThanFiveBooksAreRequested() {

        // Arrange
        LoanRequestDTO request = new LoanRequestDTO(
            1,
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(5),
            List.of(1, 2, 3, 4, 5, 6)
        );

        // Act + Assert
        ConflictException ex = assertThrows(
            ConflictException.class,
            () -> loanService.create(request)
        );

        assertEquals("A loan cannot include more than 5 books", ex.getMessage());
    }
    
    /**
     * Verifies that creating a loan fails when the provided dates are in the past.
     *
     * @throws ConflictException if loan dates are invalid
     */
    @Test
    @DisplayName("Should throw exception when dates are in the past")
    void shouldThrowExceptionWhenDatesAreInThePast() {

        // Arrange
        LoanRequestDTO request = new LoanRequestDTO(
            1,
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(5),
            List.of(1)
        );

        // Act + Assert
        ConflictException ex = assertThrows(
            ConflictException.class,
            () -> loanService.create(request)
        );

        assertEquals("Dates cannot be in the past", ex.getMessage());
    }
    
    /**
     * Verifies that creating a loan fails when the specified user does not exist.
     *
     * @throws UserNotFoundException if the user is not found
     */
    @Test
    @DisplayName("Should throw exception when user does not exist")
    void shouldThrowExceptionWhenUserDoesNotExist() {

        // Arrange
        LoanRequestDTO request = new LoanRequestDTO(
            1,
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(5),
            List.of(1)
        );

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
            UserNotFoundException.class,
            () -> loanService.create(request)
        );
    }
    
    /**
     * Verifies that creating a loan fails when one or more requested books do not exist.
     *
     * @throws NotFoundException if any book is not found
     */
    @Test
    @DisplayName("Should throw exception when books are not found")
    void shouldThrowExceptionWhenBooksAreNotFound() {

        // Arrange
        LoanRequestDTO request = new LoanRequestDTO(
            1,
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(5),
            List.of(1, 2)
        );

        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(loanRepository.existsByUser_IdAndStatus(1, LoanStatus.ACTIVE)).thenReturn(false);
        when(bookRepository.findAllById(request.getBookIds())).thenReturn(List.of());

        // Act + Assert
        assertThrows(
            NotFoundException.class,
            () -> loanService.create(request)
        );
    }
    
    /**
     * Verifies that creating a loan fails when one or more books are already loaned.
     *
     * @throws LoanConflictException if any book is already part of an active loan
     */
    @Test
    @DisplayName("Should throw exception when book is already loaned")
    void shouldThrowExceptionWhenBookAlreadyLoaned() {

        // Arrange
        LoanRequestDTO request = new LoanRequestDTO(
            1,
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(5),
            List.of(1)
        );

        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(loanRepository.existsByUser_IdAndStatus(1, LoanStatus.ACTIVE)).thenReturn(false);
        when(bookRepository.findAllById(request.getBookIds())).thenReturn(List.of(new Book()));
        when(loanRepository.existsByStatusAndBooks_IdIn(LoanStatus.ACTIVE, request.getBookIds()))
            .thenReturn(true);

        // Act + Assert
        assertThrows(
            LoanConflictException.class,
            () -> loanService.create(request)
        );
    }
}