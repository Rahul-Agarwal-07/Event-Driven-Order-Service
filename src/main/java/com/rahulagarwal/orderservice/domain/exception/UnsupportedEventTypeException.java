package com.rahulagarwal.orderservice.domain.exception;

public class UnsupportedEventTypeException extends RuntimeException {
    public UnsupportedEventTypeException() { super("Unsupported Event");}
    public UnsupportedEventTypeException(String message) {
        super(message);
    }
}
