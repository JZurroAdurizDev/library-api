package com.jabierzurro.libraryapi.controller;

import com.jabierzurro.libraryapi.dto.LoanRequestDTO;
import com.jabierzurro.libraryapi.dto.LoanResponseDTO;
import com.jabierzurro.libraryapi.dto.PatchLoanRequestDTO;
import com.jabierzurro.libraryapi.dto.UpdateLoanRequestDTO;
import com.jabierzurro.libraryapi.entity.LoanStatus;
import com.jabierzurro.libraryapi.service.LoanService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * REST controller for managing loans.
 *
 * <p>This controller exposes HTTP endpoints for loan operations, including
 * retrieval, search, creation, update, partial update and deletion.
 *
 * <p>Request validation is applied to input DTOs, while business logic is
 * delegated to the service layer.
 *
 * <p>Access control rules:
 * <ul>
 *     <li>ADMIN users have full access to all loan operations</li>
 *     <li>Regular users can create loans and partially modify their own loans</li>
 *     <li>Loan ownership is validated through a custom security component</li>
 * </ul>
 *
 * @author Jabier Zurro Aduriz
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/loans")
public class LoanController {

    /**
     * Service layer dependency used to manage loans.
     */
    private final LoanService loanService;

    /**
     * Retrieves all loans.
     *
     * @return HTTP 200 response containing the list of loans
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<LoanResponseDTO>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    /**
     * Retrieves a loan by its identifier.
     *
     * @param id loan identifier
     * @return HTTP 200 response containing the requested loan
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> getLoanById(@PathVariable Integer id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }

    /**
     * Searches loans using optional filtering criteria.
     *
     * @param userId    identifier of the user associated with the loan
     * @param status    loan status
     * @param startDate start date of the loan
     * @param dueDate   due date of the loan
     * @return HTTP 200 response containing the matching loans
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<LoanResponseDTO>> searchLoans(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) LoanStatus status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate dueDate
    ) {
        return ResponseEntity.ok(loanService.search(userId, status, startDate, dueDate));
    }

    /**
     * Creates a new loan.
     *
     * @param request loan creation data
     * @return HTTP 201 response containing the created loan
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(
            @Valid @RequestBody LoanRequestDTO request
    ){
        return ResponseEntity.status(201).body(loanService.create(request));
    }

    /**
     * Fully updates an existing loan.
     *
     * @param id      loan identifier
     * @param request updated loan data
     * @return HTTP 200 response containing the updated loan
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> updateLoan(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateLoanRequestDTO request
    ) {
        return ResponseEntity.ok(loanService.update(id, request));
    }

    /**
     * Partially updates an existing loan.
     *
     * @param id      loan identifier
     * @param request partial update data
     * @return HTTP 200 response containing the updated loan
     */
    @PreAuthorize("hasRole('ADMIN') or @loanSecurity.isOwner(#id, principal.id)")
    @PatchMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> patchLoan(
            @PathVariable Integer id,
            @Valid @RequestBody PatchLoanRequestDTO request
    ){
        return ResponseEntity.ok(loanService.patch(id, request));
    }

    /**
     * Deletes a loan by its identifier.
     *
     * @param id loan identifier
     * @return HTTP 204 response with no content
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Integer id) {
        loanService.delete(id);
        return ResponseEntity.noContent().build();
    } 
}