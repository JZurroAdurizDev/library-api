package com.jabierzurro.libraryapi.dto;

import com.jabierzurro.libraryapi.entity.LoanStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object used to partially update an existing loan.
 *
 * <p>This DTO allows modifying one or more fields of a loan without
 * requiring a full update. All fields are optional and only non-null
 * values will be applied.
 *
 * <p>Business validations (such as date consistency or status transitions)
 * are handled in the service layer.
 *
 * @author Jabier Zurro Aduriz
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatchLoanRequestDTO {

    /**
     * New start date of the loan.
     * <p>If null, the current value is preserved.
     */
    private LocalDate startDate;

    /**
     * New due date of the loan.
     * <p>If null, the current value is preserved.
     */
    private LocalDate dueDate;

    /**
     * New status of the loan.
     * <p>If null, the current value is preserved.
     */
    private LoanStatus status;
}