package com.jabierzurro.libraryapi.dto;

import com.jabierzurro.libraryapi.entity.LoanStatus;
import jakarta.validation.constraints.NotNull;
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
public class UpdateLoanRequestDTO {

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private LoanStatus status;
}