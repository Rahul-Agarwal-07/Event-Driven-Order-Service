package com.rahulagarwal.orderservice.application.usecase;

import com.rahulagarwal.orderservice.application.port.ConsumeEventUseCasePort;
import com.rahulagarwal.orderservice.application.port.ProcessedEventRepositoryPort;
import com.rahulagarwal.orderservice.domain.model.consumer.ProcessedEvent;
import com.rahulagarwal.orderservice.domain.model.shared.EventEnvelope;
import jakarta.transaction.Transactional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Transactional
public class ConsumeEventUseCase implements ConsumeEventUseCasePort {

    private final ProcessedEventRepositoryPort processedEventRepository;

    public ConsumeEventUseCase(ProcessedEventRepositoryPort processedEventRepository) {
        this.processedEventRepository = processedEventRepository;
    }

    @Override
    public void execute(EventEnvelope event) {

        if("FAIL".equals(event.getEventType()))
            throw new RuntimeException("Forcing Failure");

        try {
            processedEventRepository.save(
                    new ProcessedEvent(
                            event.getEventId(),
                            Instant.now()
                    )
            );
        }
        catch(DuplicateKeyException e)
        {
            // ignore
        }
    }
}
