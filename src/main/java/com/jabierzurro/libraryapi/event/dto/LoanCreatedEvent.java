package com.jabierzurro.libraryapi.event.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Jabier Zurro Aduriz
 */
public record LoanCreatedEvent(
        Integer loanId, 
        Integer userId, 
        String userEmail, 
        List<String> bookTitles, 
        LocalDate startDate, 
        LocalDate dueDate, 
        LocalDateTime timestamp) {
}
