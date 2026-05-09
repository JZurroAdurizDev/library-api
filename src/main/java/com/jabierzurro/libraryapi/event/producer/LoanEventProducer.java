package com.jabierzurro.libraryapi.event.producer;

import com.jabierzurro.libraryapi.event.dto.LoanCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jabier Zurro Aduriz
 */
@Service
@RequiredArgsConstructor
public class LoanEventProducer {

    private static final String TOPIC = "loan-events";

    private final KafkaTemplate<String, LoanCreatedEvent> kafkaTemplate;

    public void publishLoanCreatedEvent(LoanCreatedEvent event) {
        kafkaTemplate.send(
                TOPIC,
                event.loanId().toString(),
                event
        );
    }
}
