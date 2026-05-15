package com.jabierzurro.libraryapi.event.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * Kafka producer configuration.
 *
 * <p>Provides a KafkaTemplate bean capable of publishing different
 * loan-related domain events serialized as JSON.
 *
 * @author Jabier Zurro Aduriz
 */
@Configuration
public class KafkaProducerConfig {

    /**
     * Kafka bootstrap servers address.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Creates the KafkaTemplate used to publish domain events.
     *
     * @return configured KafkaTemplate instance
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {

        Map<String, Object> config = new HashMap<>();

        config.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );

        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );

        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );

        return new KafkaTemplate<>(
                new DefaultKafkaProducerFactory<>(config)
        );
    }
}