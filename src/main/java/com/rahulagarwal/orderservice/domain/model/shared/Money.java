package com.rahulagarwal.orderservice.domain.model.shared;

import java.math.BigDecimal;
import java.util.Currency;

public class Money {
    BigDecimal amount;
    Currency currency;

    private Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Money multiply(BigDecimal multiplicand)
    {
        return new Money(
                amount.multiply(multiplicand),
                currency
        );
    }

    public Money add(Money augend)
    {
        return new Money(
                amount.add(augend.getAmount()),
                currency
        );
    }

    public static Money zero(String currency)
    {
        return new Money(
                BigDecimal.ZERO,
                Currency.getInstance(currency)
        );
    }

}
