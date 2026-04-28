package com.jabierzurro.libraryapi.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object representing a loan response.
 *
 * <p>This DTO is used to expose loan information to clients, including
 * associated books and relevant dates.
 *
 * <p>It contains only the data required by the presentation layer and
 * avoids exposing internal entity details.
 *
 * @param loanId   unique identifier of the loan
 * @param userId   identifier of the user associated with the loan
 * @param startDate start date of the loan
 * @param dueDate   due date of the loan
 * @param closedAt  date and time when the loan was closed (nullable)
 * @param status    current status of the loan (e.g., ACTIVE, CLOSED)
 * @param books     list of books included in the loan
 *
 * @author Jabier Zurro Aduriz
 */
@JsonPropertyOrder({"loanId", "userId", "startDate", "dueDate", "closedAt", "status", "books"})
public record LoanResponseDTO(
        Integer loanId,
        Integer userId,
        LocalDate startDate,
        LocalDate dueDate,
        LocalDateTime closedAt,
        String status,
        List<BookResponseDTO> books
) {}