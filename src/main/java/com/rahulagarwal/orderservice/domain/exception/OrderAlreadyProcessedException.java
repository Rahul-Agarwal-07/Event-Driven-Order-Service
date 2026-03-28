package com.rahulagarwal.orderservice.domain.exception;

public class OrderAlreadyProcessedException extends DomainException {
    public OrderAlreadyProcessedException() { super("Order Already Processed");}
    public OrderAlreadyProcessedException(String message) {
        super(message);
    }
}
