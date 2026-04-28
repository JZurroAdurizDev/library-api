package com.jabierzurro.libraryapi.dto;

import com.jabierzurro.libraryapi.entity.LoanStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object used to fully update an existing loan.
 *
 * <p>This DTO is intended for complete updates (PUT), meaning all fields
 * must be provided and will replace the current state of the loan.
 *
 * <p>Validation constraints ensure that no field is null. Business rules
 * such as date validation or status transitions are handled in the service layer.
 *
 * @author Jabier Zurro Aduriz
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateLoanRequestDTO {

    /**
     * New start date of the loan.
     */
    @NotNull
    private LocalDate startDate;

    /**
     * New due date of the loan.
     */
    @NotNull
    private LocalDate dueDate;

    /**
     * New status of the loan.
     */
    @NotNull
    private LoanStatus status;
}