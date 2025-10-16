package io.naryo.api.node.common.request.connection;

import java.time.Duration;

import io.naryo.domain.node.connection.RetryConfiguration;

public record RetryConfigurationRequest(Integer times, Duration backoff) {
    public RetryConfiguration toDomain() {
        return new RetryConfiguration(times, backoff);
    }
}
