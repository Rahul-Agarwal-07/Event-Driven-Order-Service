package com.rahulagarwal.orderservice.infrastructure.out.messaging.kafka;

import com.rahulagarwal.orderservice.application.port.EventPublisherPort;
import com.rahulagarwal.orderservice.domain.exception.EventPublishFailedException;
import com.rahulagarwal.orderservice.domain.model.shared.EventEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisherPort {

    private final KafkaTemplate<String, EventEnvelope> kafkaTemplate;

    @Value("${app.kafka.topics.order-events}")
    private String TOPIC;

    @Override
    public void publish(EventEnvelope event) {
        try
        {
            kafkaTemplate
                    .send(TOPIC, event.getAggregateId().getValue(), event)
                    .get();

        } catch (Exception e) {
            throw new EventPublishFailedException(
                    "Failed to publish event : " + event.getEventId(), e
            );
        }
    }
}
