package com.jabierzurro.libraryapi.event.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Event published when a new loan is successfully created.
 *
 * <p>This event contains the minimum information required by external
 * microservices to react asynchronously to loan creation without directly
 * depending on the main API database.
 *
 * <p>The event is serialized as JSON and published to a Kafka topic.
 *
 * @param loanId     loan identifier
 * @param userId     user identifier
 * @param userEmail  user email address
 * @param bookTitles titles of books included in the loan
 * @param startDate  loan start date
 * @param dueDate    loan due date
 * @param timestamp  event creation timestamp
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
        LocalDateTime timestamp
) {
}