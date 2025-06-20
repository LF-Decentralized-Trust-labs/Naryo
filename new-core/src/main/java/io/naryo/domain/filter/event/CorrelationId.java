package io.naryo.domain.filter.event;

public record CorrelationId(int position) {
    public CorrelationId {
        if (position < 0) {
            throw new IllegalArgumentException("position must be greater than or equal to 0");
        }
    }
}
