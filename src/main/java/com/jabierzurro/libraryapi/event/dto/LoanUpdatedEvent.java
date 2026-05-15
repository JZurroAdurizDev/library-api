
package com.jabierzurro.libraryapi.event.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Event published when an existing loan is partially updated.
 *
 * <p>This event contains the previous and new loan dates so external
 * microservices can react asynchronously to loan modifications.
 *
 * @param loanId            loan identifier
 * @param userId            user identifier
 * @param userEmail         user email address
 * @param previousStartDate previous loan start date
 * @param previousDueDate   previous loan due date
 * @param newStartDate      new loan start date
 * @param newDueDate        new loan due date
 * @param timestamp         event creation timestamp
 *
 * @author Jabier Zurro Aduriz
 */
public record LoanUpdatedEvent(
        Integer loanId,
        Integer userId,
        String userEmail,
        LocalDate previousStartDate,
        LocalDate previousDueDate,
        LocalDate newStartDate,
        LocalDate newDueDate,
        LocalDateTime timestamp
) {
}