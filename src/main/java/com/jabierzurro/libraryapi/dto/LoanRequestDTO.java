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
 *
 * @author Jabier Zurro Aduriz
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoanRequestDTO {
    
    @NotNull
    private Integer userId;
    
    @NotNull
    private LocalDate startDate;
    
    @NotNull
    private LocalDate dueDate;
    
    @NotEmpty
    @Size(max = 5)
    private List<Integer> bookIds;
    
}