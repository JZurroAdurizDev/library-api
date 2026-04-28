package com.jabierzurro.libraryapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object used to create a new loan.
 *
 * <p>This DTO encapsulates the data required to register a loan request
 * from the client side.
 *
 * <p>Validation constraints ensure:
 * <ul>
 *   <li>A user is always specified</li>
 *   <li>Loan dates are provided</li>
 *   <li>At least one book is included</li>
 *   <li>No more than 5 books can be loaned at once</li>
 * </ul>
 *
 * @author Jabier Zurro Aduriz
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoanRequestDTO {
    
    /**
     * Identifier of the user requesting the loan.
     */
    @NotNull
    private Integer userId;
    
    /**
     * Start date of the loan.
     */
    @NotNull
    private LocalDate startDate;
    
    /**
     * Due date of the loan.
     */
    @NotNull
    private LocalDate dueDate;
    
    /**
     * List of book identifiers to be included in the loan.
     *
     * <p>Must contain at least one book and at most five.
     */
    @NotEmpty
    @Size(max = 5)
    private List<Integer> bookIds;
    
}