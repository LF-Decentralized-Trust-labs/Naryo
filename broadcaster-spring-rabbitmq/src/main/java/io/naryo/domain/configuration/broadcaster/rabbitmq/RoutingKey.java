package io.naryo.domain.configuration.broadcaster.rabbitmq;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public record RoutingKey(String value) {

    private static final String TOPIC_PATTERN = "\\A[A-Za-z0-9_-]+(?:\\.[A-Za-z0-9_-]+)*\\z";

    public RoutingKey {
        Objects.requireNonNull(value, "RoutingKey must not be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException("RoutingKey cannot be blank");
        }

        if (value.getBytes(StandardCharsets.UTF_8).length > 255) {
            throw new IllegalArgumentException("RoutingKey cannot be longer than 255 bytes");
        }

        if (!value.matches(TOPIC_PATTERN)) {
            throw new IllegalArgumentException(
                    "RoutingKey must be dot-separated words containing letters, digits, '_' or '-'");
        }
    }
}
