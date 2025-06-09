package io.naryo.domain.node.connection.http;

public record MaxIdleConnections(int value) {
    public MaxIdleConnections {
        if (value < 0) throw new IllegalArgumentException("Must be >= 0");
    }
}
