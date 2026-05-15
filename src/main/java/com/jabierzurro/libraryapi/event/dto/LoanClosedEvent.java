package com.jabierzurro.libraryapi.event.dto;

import java.time.LocalDateTime;

/**
 * Event published when a loan is successfully closed.
 *
 * <p>This event contains the minimum information required by external
 * microservices to react asynchronously to loan closures without directly
 * depending on the main API database.
 *
 * <p>The event is serialized as JSON and published to a Kafka topic.
 *
 * @param loanId     loan identifier
 * @param userId     user identifier
 * @param userEmail  user email address
 * @param closedAt   loan closure timestamp
 * @param timestamp  event creation timestamp
 *
 * @author Jabier Zurro Aduriz
 */
public record LoanClosedEvent(
        Integer loanId,
        Integer userId,
        String userEmail,
        LocalDateTime closedAt,
        LocalDateTime timestamp
) {
}