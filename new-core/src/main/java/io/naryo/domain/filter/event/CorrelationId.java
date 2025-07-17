package io.naryo.domain.filter.event;

public record CorrelationId(Integer position) {
    public CorrelationId {
        if (position == null || position < 0) {
            throw new IllegalArgumentException("position must be greater than or equal to 0");
        }
    }
}
