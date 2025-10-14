package io.naryo.api.node.common.response.connection;

import java.time.Duration;

import io.naryo.domain.node.connection.RetryConfiguration;

public record RetryConfigurationResponse(int times, Duration backoff) {
    public static RetryConfigurationResponse fromDomain(RetryConfiguration retryConfiguration) {
        return new RetryConfigurationResponse(
                retryConfiguration.times(), retryConfiguration.backoff());
    }
}
