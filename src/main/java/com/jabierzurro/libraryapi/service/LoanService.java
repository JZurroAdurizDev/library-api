package com.jabierzurro.libraryapi.service;

import com.jabierzurro.libraryapi.dto.LoanRequestDTO;
import com.jabierzurro.libraryapi.dto.LoanResponseDTO;
import com.jabierzurro.libraryapi.dto.PatchLoanRequestDTO;
import com.jabierzurro.libraryapi.dto.UpdateLoanRequestDTO;
import com.jabierzurro.libraryapi.entity.LoanStatus;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing loans.
 *
 * <p>This interface defines the contract for loan-related operations,
 * including retrieval, creation, update and deletion.
 *
 * <p>All methods operate with DTOs to ensure proper separation between
 * the service layer and persistence layer.
 *
 * @author Jabier Zurro Aduriz
 */
public interface LoanService {
    
    /**
     * Retrieves all loans.
     *
     * @return list of all loans as {@link LoanResponseDTO}
     */
    List<LoanResponseDTO> getAllLoans();

    /**
     * Retrieves a loan by its identifier.
     *
     * @param id loan identifier
     * @return the loan as {@link LoanResponseDTO}
     */
    LoanResponseDTO getLoanById(Integer id);

    /**
     * Searches loans based on optional filtering criteria.
     *
     * <p>Any parameter can be {@code null}, in which case it is ignored
     * during filtering.
     *
     * @param userId    identifier of the user associated with the loan
     * @param status    current status of the loan
     * @param startDate start date of the loan
     * @param dueDate   due date of the loan
     * @return list of loans matching the given criteria
     */
    List<LoanResponseDTO> search(Integer userId, LoanStatus status, LocalDate startDate, LocalDate dueDate);

    /**
     * Creates a new loan.
     *
     * @param request loan creation data
     * @return created loan as {@link LoanResponseDTO}
     */
    LoanResponseDTO create(LoanRequestDTO request);

    /**
     * Fully updates an existing loan.
     *
     * @param id      loan identifier
     * @param request updated loan data
     * @return updated loan as {@link LoanResponseDTO}
     */
    LoanResponseDTO update(Integer id, UpdateLoanRequestDTO request);

    /**
     * Partially updates an existing loan.
     *
     * <p>Only non-null fields in the request will be updated.
     *
     * @param id      loan identifier
     * @param request partial update data
     * @return updated loan as {@link LoanResponseDTO}
     */
    LoanResponseDTO patch(Integer id, PatchLoanRequestDTO request);

    /**
     * Deletes a loan by its identifier.
     *
     * @param id loan identifier
     */
    void delete(Integer id);
}