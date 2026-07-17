package com.jabierzurro.libraryapi.service;

import com.jabierzurro.libraryapi.dto.BookResponseDTO;
import com.jabierzurro.libraryapi.dto.LoanRequestDTO;
import com.jabierzurro.libraryapi.dto.LoanResponseDTO;
import com.jabierzurro.libraryapi.dto.PatchLoanRequestDTO;
import com.jabierzurro.libraryapi.dto.UpdateLoanRequestDTO;
import com.jabierzurro.libraryapi.entity.Book;
import com.jabierzurro.libraryapi.entity.Loan;
import com.jabierzurro.libraryapi.entity.LoanStatus;
import com.jabierzurro.libraryapi.entity.User;
import com.jabierzurro.libraryapi.event.dto.LoanClosedEvent;
import com.jabierzurro.libraryapi.event.dto.LoanCreatedEvent;
import com.jabierzurro.libraryapi.event.dto.LoanUpdatedEvent;
import com.jabierzurro.libraryapi.event.producer.LoanEventProducer;
import com.jabierzurro.libraryapi.exception.base.ConflictException;
import com.jabierzurro.libraryapi.exception.base.NotFoundException;
import com.jabierzurro.libraryapi.exception.loan.LoanConflictException;
import com.jabierzurro.libraryapi.exception.loan.LoanNotFoundException;
import com.jabierzurro.libraryapi.exception.user.UserNotFoundException;
import com.jabierzurro.libraryapi.repository.BookRepository;
import com.jabierzurro.libraryapi.repository.LoanRepository;
import com.jabierzurro.libraryapi.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing loans.
 *
 * <p>This class contains the business logic related to loans, including
 * validation rules, conflict detection and interaction with persistence layers.
 *
 * <p>It ensures consistency between users, books and loans, enforcing
 * constraints such as:
 * <ul>
 *   <li>A user cannot have more than one active loan</li>
 *   <li>A book cannot be loaned if it is already part of an active loan</li>
 *   <li>Loan dates must be valid and not overlap improperly</li>
 * </ul>
 *
 * <p>This implementation maps entities to DTOs to decouple the service layer
 * from the persistence layer.
 *
 * @author Jabier Zurro Aduriz
 */
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    
    /**
     * Repository for loan persistence operations.
     */
    private final LoanRepository loanRepository;

    /**
     * Repository for user persistence operations.
     */
    private final UserRepository userRepository;

    /**
     * Repository for book persistence operations.
     */
    private final BookRepository bookRepository;
    
    /**
     * Producer responsible for publishing loan-related domain events to Kafka.
     */
    private final LoanEventProducer loanEventProducer;
    
    /**
     * Retrieves all loans.
     *
     * @return list of loans as {@link LoanResponseDTO};
     *         an empty list if no loans exist
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDTO> getAllLoans() {
        return this.loanRepository.findAll()
                .stream()
                .map(LoanServiceImpl::toResponseDTO)
                .toList();
    }

    /**
     * Retrieves a loan by its identifier.
     *
     * @param id loan identifier
     * @return loan as {@link LoanResponseDTO}
     * @throws LoanNotFoundException if the loan does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public LoanResponseDTO getLoanById(Integer id) {
        Loan loan = this.loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));
                
        return toResponseDTO(loan);
    }

    /**
     * Searches loans based on optional filters.
     *
     * @param userId    user identifier
     * @param status    loan status
     * @param startDate start date
     * @param dueDate   due date
     * @return list of matching loans
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDTO> search(Integer userId, LoanStatus status, LocalDate startDate, LocalDate dueDate) {
        String statusValue = status != null ? status.name() : null;

        List<Loan> loans = this.loanRepository.searchLoans(userId, statusValue, startDate, dueDate);

        return loans.stream()
                .map(LoanServiceImpl::toResponseDTO)
                .toList();
    }

    /**
     * Creates a new loan.
     *
     * <p>Validates:
     * <ul>
     *   <li>Loan dates</li>
     *   <li>User existence</li>
     *   <li>User does not already have an active loan</li>
     *   <li>Books existence and availability</li>
     *   <li>Maximum number of books per loan</li>
     * </ul>
     *
     * <p>After the loan is successfully persisted, this method publishes a
     * {@link LoanCreatedEvent} to Kafka so external services can react
     * asynchronously to the loan creation.
     *
     * @param request loan creation data
     * @return created loan as {@link LoanResponseDTO}
     * @throws UserNotFoundException if the user does not exist
     * @throws NotFoundException if any book does not exist
     * @throws ConflictException if business rules are violated
     */
    @Override
    @Transactional
    public LoanResponseDTO create(LoanRequestDTO request) {

        validateLoanDates(request.getStartDate(), request.getDueDate());
        validateBookLimit(request.getBookIds());
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        boolean userHasActiveLoan = loanRepository.existsByUser_IdAndStatus(
                user.getId(),
                LoanStatus.ACTIVE
        );

        if (userHasActiveLoan) {
            throw LoanConflictException.userHasActiveLoan(user.getId());
        }

        List<Book> books = bookRepository.findAllById(request.getBookIds());

        if (books.size() != request.getBookIds().size()) {
            throw new NotFoundException("One or more books were not found.") {};
        }

        boolean anyBookAlreadyLoaned = loanRepository.existsByStatusAndBooks_IdIn(
                LoanStatus.ACTIVE,
                request.getBookIds()
        );

        if (anyBookAlreadyLoaned) {
            throw LoanConflictException.oneOrMoreBooksNotAvailable();
        }

        Loan loan = new Loan(
            user,
            request.getStartDate(),
            request.getDueDate(),
            LoanStatus.ACTIVE
        );

        loan.getBooks().addAll(books);
        Loan createdLoan = loanRepository.save(loan);
        
        LoanCreatedEvent event = new LoanCreatedEvent(
            createdLoan.getId(),
            user.getId(),
            user.getEmail(),
            books.stream()
                    .map(Book::getTitle)
                    .toList(),
            createdLoan.getStartDate(),
            createdLoan.getDueDate(),
            LocalDateTime.now()
        );
        
        loanEventProducer.publishLoanEvent(createdLoan.getId(), event);
        
        return LoanServiceImpl.toResponseDTO(createdLoan);
    }

    /**
     * Fully updates an existing loan.
     *
     * <p>Validates loan dates and checks for book conflicts with other active loans.
     *
     * @param id loan identifier
     * @param request update data
     * @return updated loan as {@link LoanResponseDTO}
     * @throws LoanNotFoundException if the loan does not exist
     * @throws ConflictException if validation fails
     */
    @Override
    @Transactional
    public LoanResponseDTO update(Integer id, UpdateLoanRequestDTO request) {

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));

        validateLoanDates(request.getStartDate(), request.getDueDate());
        validateNoBookConflict(
                loan,
                loanRepository.findAll(),
                request.getStartDate(),
                request.getDueDate()
        );

        loan.setStartDate(request.getStartDate());
        loan.setDueDate(request.getDueDate());
        updateLoanStatus(loan, request.getStatus());

        Loan updatedLoan = loanRepository.save(loan);

        return LoanServiceImpl.toResponseDTO(updatedLoan);
    }

    /**
     * Partially updates an existing loan.
     *
     * <p>Only non-null fields are updated. Dates are recalculated before validation.
     *
     * <p>After the loan is successfully updated, this method publishes a
     * {@link LoanUpdatedEvent} to Kafka so external microservices can react
     * asynchronously to the loan modification.
     *
     * @param id loan identifier
     * @param request partial update data
     * @return updated loan as {@link LoanResponseDTO}
     * @throws LoanNotFoundException if the loan does not exist
     * @throws ConflictException if validation fails
     */
    @Override
    @Transactional
    public LoanResponseDTO patch(Integer id, PatchLoanRequestDTO request) {

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));

        if (loan.getStatus() == LoanStatus.CLOSED) {
            throw new ConflictException("Closed loans cannot be modified") {};
        }

        LocalDate previousStartDate = loan.getStartDate();
        LocalDate previousDueDate = loan.getDueDate();
        LoanStatus previousStatus = loan.getStatus();

        boolean datesAreBeingUpdated =
                request.getStartDate() != null || request.getDueDate() != null;

        if (datesAreBeingUpdated) {
            LocalDate newStartDate = request.getStartDate() != null
                    ? request.getStartDate()
                    : loan.getStartDate();

            LocalDate newDueDate = request.getDueDate() != null
                    ? request.getDueDate()
                    : loan.getDueDate();

            validateLoanDates(newStartDate, newDueDate);
            validateNoBookConflict(
                    loan,
                    loanRepository.findAll(),
                    newStartDate,
                    newDueDate
            );

            loan.setStartDate(newStartDate);
            loan.setDueDate(newDueDate);
        }

        updateLoanStatus(loan, request.getStatus());

        Loan updatedLoan = loanRepository.save(loan);

        boolean datesChanged =
                !previousStartDate.equals(updatedLoan.getStartDate())
                || !previousDueDate.equals(updatedLoan.getDueDate());

        boolean loanWasClosed =
                previousStatus != LoanStatus.CLOSED
                && updatedLoan.getStatus() == LoanStatus.CLOSED;

        if (datesChanged) {
            LoanUpdatedEvent event = new LoanUpdatedEvent(
                    updatedLoan.getId(),
                    updatedLoan.getUser().getId(),
                    updatedLoan.getUser().getEmail(),
                    previousStartDate,
                    previousDueDate,
                    updatedLoan.getStartDate(),
                    updatedLoan.getDueDate(),
                    LocalDateTime.now()
            );

            loanEventProducer.publishLoanEvent(updatedLoan.getId(), event);
        }

        if (loanWasClosed) {
            LoanClosedEvent event = new LoanClosedEvent(
                    updatedLoan.getId(),
                    updatedLoan.getUser().getId(),
                    updatedLoan.getUser().getEmail(),
                    updatedLoan.getClosedAt(),
                    LocalDateTime.now()
            );

            loanEventProducer.publishLoanEvent(updatedLoan.getId(), event);
        }

        return LoanServiceImpl.toResponseDTO(updatedLoan);
    }

    /**
     * Deletes a loan by its identifier.
     *
     * @param id loan identifier
     * @throws LoanNotFoundException if the loan does not exist
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        Loan loan = this.loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));
        this.loanRepository.delete(loan);
    }
    
    
    /**
    * Validates the maximum number of books allowed in a loan.
    *
    * @param bookIds book identifiers included in the loan request
    * @throws ConflictException if more than five books are requested
    */
    private static void validateBookLimit(List<Integer> bookIds) {
        if (bookIds == null || bookIds.isEmpty()) {
            throw new ConflictException("A loan must include at least one book") {};
        }

        if (bookIds.size() > 5) {
            throw new ConflictException("A loan cannot include more than 5 books") {};
        }
    }
        
    /**
     * Validates loan dates.
     *
     * <p>Ensures:
     * <ul>
     *   <li>Dates are not in the past</li>
     *   <li>Start date is not after due date</li>
     * </ul>
     *
     * @param startDate start date
     * @param dueDate   due date
     * @throws ConflictException if validation fails
     */
    private static void validateLoanDates(LocalDate startDate, LocalDate dueDate) {
        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today) || dueDate.isBefore(today)) {
            throw new ConflictException("Dates cannot be in the past") {};
        }

        if (startDate.isAfter(dueDate)) {
            throw new ConflictException("Start date cannot be after due date") {};
        }
    }

    /**
     * Validates that no book in the loan conflicts with other active loans.
     *
     * @param currentLoan loan being validated
     * @param loans       list of existing loans
     * @param startDate   start date to validate
     * @param dueDate     due date to validate
     * @throws LoanConflictException if a conflict is detected
     */
    private static void validateNoBookConflict(
            Loan currentLoan,
            List<Loan> loans,
            LocalDate startDate,
            LocalDate dueDate
    ) {
        for (Loan searched : loans) {

            if (searched.getId().equals(currentLoan.getId())) {
                continue;
            }

            if (searched.getStatus() != LoanStatus.ACTIVE) {
                continue;
            }

            boolean datesOverlap =
                    !searched.getDueDate().isBefore(startDate)
                    && !searched.getStartDate().isAfter(dueDate);

            boolean sharesBook = searched.getBooks().stream()
                    .anyMatch(book -> currentLoan.getBooks().contains(book));

            if (datesOverlap && sharesBook) {
                throw LoanConflictException.oneOrMoreBooksNotAvailable();
            }
        }
    }

    /**
     * Updates loan status following business rules.
     *
     * @param loan            loan to update
     * @param requestedStatus requested status
     * @throws ConflictException if invalid transition is attempted
     */
    private static void updateLoanStatus(Loan loan, LoanStatus requestedStatus) {
        if (requestedStatus == null) {
            return;
        }

        if (loan.getStatus() == LoanStatus.CLOSED && requestedStatus == LoanStatus.ACTIVE) {
            throw new ConflictException("Closed loans cannot be reopened") {};
        }

        if (requestedStatus == LoanStatus.CLOSED && loan.getStatus() != LoanStatus.CLOSED) {
            loan.setClosedAt(LocalDateTime.now());
        }

        loan.setStatus(requestedStatus);
    }
    
    /**
     * Maps a {@link Loan} entity to {@link LoanResponseDTO}.
     *
     * @param loan loan entity
     * @return mapped response DTO
     */
    private static LoanResponseDTO toResponseDTO(Loan loan) {
        return new LoanResponseDTO(
                loan.getId(),
                loan.getUser().getId(),
                loan.getStartDate(),
                loan.getDueDate(),
                loan.getClosedAt(),
                loan.getStatus().name(),
                loan.getBooks().stream()
                        .map(book -> new BookResponseDTO(
                                book.getId(),
                                book.getTitle(),
                                book.getAuthor(),
                                book.getIsbn(),
                                book.getPublishedYear(),
                                book.getPages()
                        ))
                        .toList()
        );
    }
}