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
@RequestMapping("/loans")
public class LoanController {
    
    private final LoanService loanService;
    
    @GetMapping
    public ResponseEntity<List<LoanResponseDTO>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> getLoanById(@PathVariable Integer id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<LoanResponseDTO>> searchLoans(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) LoanStatus status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate dueDate
    ) {
        return ResponseEntity.ok(loanService.search(userId, status, startDate, dueDate));
    }
    
    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(
            @Valid @RequestBody LoanRequestDTO request
    ){
        return ResponseEntity.status(201).body(loanService.create(request));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> updateLoan(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateLoanRequestDTO request
    ) {
        return ResponseEntity.ok(loanService.update(id, request));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> patchLoan(
            @PathVariable Integer id,
            @Valid @RequestBody PatchLoanRequestDTO request
    ){
        return ResponseEntity.ok(loanService.patch(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Integer id) {
        loanService.delete(id);
        return ResponseEntity.noContent().build();
    } 
}
