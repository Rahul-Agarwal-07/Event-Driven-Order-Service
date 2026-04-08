package com.rahulagarwal.orderservice.domain.model.outbox;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AggregateId {

    private final String value;

    @JsonCreator
    public AggregateId(@JsonProperty("value") String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}