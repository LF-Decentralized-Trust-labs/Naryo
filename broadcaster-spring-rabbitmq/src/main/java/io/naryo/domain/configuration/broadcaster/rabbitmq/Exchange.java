package io.naryo.domain.configuration.broadcaster.rabbitmq;

import java.util.Objects;

public record Exchange(String value) {

    public Exchange {
        Objects.requireNonNull(value, "Exchange must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Exchange cannot be blank");
        }
    }
}
