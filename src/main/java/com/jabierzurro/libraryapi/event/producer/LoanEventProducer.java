package com.jabierzurro.libraryapi.event.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka producer responsible for publishing loan-related domain events.
 *
 * <p>This service publishes loan-related domain events to the configured
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
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publishes a loan-related event to Kafka.
     *
     * @param loanId loan identifier used as Kafka message key
     * @param event domain event payload
     */
    public void publishLoanEvent(Integer loanId, Object event) {

        kafkaTemplate.send(
                TOPIC,
                loanId.toString(),
                event
        );
    }
}