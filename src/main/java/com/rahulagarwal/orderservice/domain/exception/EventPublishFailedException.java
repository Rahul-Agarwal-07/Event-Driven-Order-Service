package com.rahulagarwal.orderservice.domain.exception;

public class EventPublishFailedException extends RuntimeException {
    public EventPublishFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
