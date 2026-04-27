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

/**
 *
 * @author Jabier Zurro Aduriz
 */
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    
    @Override
    public List<LoanResponseDTO> getAllLoans() {
        List<Loan> loans = this.loanRepository.findAll();
        if (loans.isEmpty()) {
            throw new NotFoundException("No loans found in the database.") {};
        }
        return loans.stream()
                .map(LoanServiceImpl::toResponseDTO)
                .toList();
    }

    @Override
    public LoanResponseDTO getLoanById(Integer id) {
        Loan loan = this.loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));
                
        return toResponseDTO(loan);
    }

    @Override
    public List<LoanResponseDTO> search(Integer userId, LoanStatus status, LocalDate startDate, LocalDate dueDate) {
        String statusValue = status != null ? status.name() : null;

        List<Loan> loans = this.loanRepository.searchLoans(userId, statusValue, startDate, dueDate);

        return loans.stream()
                .map(LoanServiceImpl::toResponseDTO)
                .toList();
    }

    @Override
    public LoanResponseDTO create(LoanRequestDTO request) {

        validateLoanDates(request.getStartDate(), request.getDueDate());

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
        return LoanServiceImpl.toResponseDTO(createdLoan);
    }

    @Override
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

    @Override
    public LoanResponseDTO patch(Integer id, PatchLoanRequestDTO request) {

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));

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

        if (request.getStartDate() != null) {
            loan.setStartDate(request.getStartDate());
        }

        if (request.getDueDate() != null) {
            loan.setDueDate(request.getDueDate());
        }

        updateLoanStatus(loan, request.getStatus());

        Loan updatedLoan = loanRepository.save(loan);
        return LoanServiceImpl.toResponseDTO(updatedLoan);
    }

    @Override
    public void delete(Integer id) {
        Loan loan = this.loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));
        this.loanRepository.delete(loan);
    }
    
    private static void validateLoanDates(LocalDate startDate, LocalDate dueDate) {
        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today) || dueDate.isBefore(today)) {
            throw new ConflictException("Dates cannot be in the past") {};
        }

        if (startDate.isAfter(dueDate)) {
            throw new ConflictException("Start date cannot be after due date") {};
        }
    }

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