package com.rahulagarwal.orderservice.application.port;

import com.rahulagarwal.orderservice.domain.model.shared.EventEnvelope;

public interface ConsumeEventUseCasePort {

    void execute(EventEnvelope event);

}
