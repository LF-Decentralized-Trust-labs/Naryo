package io.librevents.domain.filter;

public record FilterName(String value) {
    public FilterName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Filter name cannot be null or empty");
        }
    }
}
