package io.librevents.domain.common.event;

public record EventName(String value) {
    public EventName {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be null or empty");
        }
    }
}
