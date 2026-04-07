package com.rahulagarwal.orderservice.infrastructure.out.messaging.kafka;

import com.rahulagarwal.orderservice.application.port.ConsumeEventUseCasePort;
import com.rahulagarwal.orderservice.domain.model.shared.EventEnvelope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventConsumer {

    private final ConsumeEventUseCasePort consumeEventUseCase;

    public KafkaEventConsumer(ConsumeEventUseCasePort consumeEventUseCase) {
        this.consumeEventUseCase = consumeEventUseCase;
    }

    @KafkaListener(topics = "${app.kafka.topics.order-events}")
    void consume(EventEnvelope event)
    {
        consumeEventUseCase.execute(event);
    }

}
