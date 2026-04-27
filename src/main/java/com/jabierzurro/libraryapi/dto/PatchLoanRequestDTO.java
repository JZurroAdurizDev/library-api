package com.jabierzurro.libraryapi.dto;

import com.jabierzurro.libraryapi.entity.LoanStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Jabier Zurro Aduriz
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatchLoanRequestDTO {

    private LocalDate startDate;
    private LocalDate dueDate;
    private LoanStatus status;
}