package com.rahulagarwal.orderservice.domain.exception;

public class InvalidStateException extends DomainException {
    public InvalidStateException() { super("Invalid State");}
    public InvalidStateException(String message) {
        super(message);
    }
}
