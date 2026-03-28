package com.rahulagarwal.orderservice.domain.exception;

public class CancellationNotAllowedException extends DomainException {
    public CancellationNotAllowedException() { super("Cancellation Not Allowed");}
    public CancellationNotAllowedException(String message) {
        super(message);
    }
}
