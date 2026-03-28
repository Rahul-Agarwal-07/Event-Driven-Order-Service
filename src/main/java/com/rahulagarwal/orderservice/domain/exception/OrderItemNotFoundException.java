package com.rahulagarwal.orderservice.domain.exception;

public class OrderItemNotFoundException extends DomainException {
    public OrderItemNotFoundException() { super("Order Item Not Found");}
    public OrderItemNotFoundException(String message) {
        super(message);
    }
}
