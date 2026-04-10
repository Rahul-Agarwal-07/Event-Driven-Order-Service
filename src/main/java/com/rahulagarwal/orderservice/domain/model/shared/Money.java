package com.rahulagarwal.orderservice.domain.model.shared;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Money {

    private final BigDecimal amount;
    private final Currency currency;

    @JsonCreator
    public Money(
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("currency") Currency currency
    ) {
        this.amount = amount;
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount, Currency.getInstance(currency));
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, Currency.getInstance(currency));
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }

        return new Money(
                this.amount.add(other.amount),
                this.currency
        );
    }

    public Money multiply(BigDecimal multiplicand) {
        return new Money(
                this.amount.multiply(multiplicand).setScale(2, RoundingMode.HALF_UP),
                this.currency
        );
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}