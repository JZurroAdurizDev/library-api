package com.jabierzurro.libraryapi.service;

import com.jabierzurro.libraryapi.dto.LoanRequestDTO;
import com.jabierzurro.libraryapi.dto.LoanResponseDTO;
import com.jabierzurro.libraryapi.dto.PatchLoanRequestDTO;
import com.jabierzurro.libraryapi.dto.UpdateLoanRequestDTO;
import com.jabierzurro.libraryapi.entity.LoanStatus;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public interface LoanService {
    
    List<LoanResponseDTO> getAllLoans();
    LoanResponseDTO getLoanById(Integer id);
    List<LoanResponseDTO> search(Integer userId, LoanStatus status, LocalDate startDate, LocalDate dueDate);
    LoanResponseDTO create(LoanRequestDTO request);
    LoanResponseDTO update(Integer id, UpdateLoanRequestDTO request);
    LoanResponseDTO patch(Integer id, PatchLoanRequestDTO request);
    void delete(Integer id);
}
