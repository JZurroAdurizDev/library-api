package com.jabierzurro.libraryapi.event.producer;

import com.jabierzurro.libraryapi.event.dto.LoanCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka producer responsible for publishing loan-related domain events.
 *
 * <p>This service sends {@link LoanCreatedEvent} messages to the configured
 * Kafka topic so external microservices can consume them asynchronously.
 *
 * <p>The loan identifier is used as the Kafka message key to improve message
 * ordering consistency for the same loan.
 *
 * @author Jabier Zurro Aduriz
 */
@Service
@RequiredArgsConstructor
public class LoanEventProducer {

    /**
     * Kafka topic used for loan-related events.
     */
    private static final String TOPIC = "loan-events";

    /**
     * Kafka template used to publish loan events.
     */
    private final KafkaTemplate<String, LoanCreatedEvent> kafkaTemplate;

    /**
     * Publishes a loan created event to Kafka.
     *
     * @param event loan creation event
     */
    public void publishLoanCreatedEvent(LoanCreatedEvent event) {
        kafkaTemplate.send(
                TOPIC,
                event.loanId().toString(),
                event
        );
    }
}