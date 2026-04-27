package com.jabierzurro.libraryapi.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Jabier Zurro Aduriz
 */
@JsonPropertyOrder({"loanId", "userId", "startDate", "dueDate2", "closedAt", "status, books"})
public record LoanResponseDTO(
        Integer loanId,
        Integer userId,
        LocalDate startDate,
        LocalDate dueDate,
        LocalDateTime closedAt,
        String status,
        List<BookResponseDTO> books
) {}
