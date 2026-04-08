package com.rahulagarwal.orderservice.infrastructure.out.messaging.kafka;

import com.rahulagarwal.orderservice.domain.model.shared.EventEnvelope;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, EventEnvelope> producerFactory(KafkaProperties properties) {
        return new DefaultKafkaProducerFactory<>(properties.buildProducerProperties());
    }

    @Bean
    public KafkaTemplate<String, EventEnvelope> kafkaTemplate(ProducerFactory<String, EventEnvelope> producerFactory) {
        return new KafkaTemplate<String, EventEnvelope>(producerFactory);
    }

    @Bean
    public KafkaEventPublisher kafkaEventPublisher(KafkaTemplate<String, EventEnvelope> kafkaTemplate)
    {
        return new KafkaEventPublisher(kafkaTemplate);
    }
}
