package io.naryo.api.node.getAll.model.connection;

import java.time.Duration;

import io.naryo.domain.node.connection.RetryConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Retry configuration")
public record RetryConfigurationResponse(Integer times, Duration backoff) {
    public static RetryConfigurationResponse fromDomain(RetryConfiguration retryConfiguration) {
        return new RetryConfigurationResponse(
                retryConfiguration.times(), retryConfiguration.backoff());
    }
}
