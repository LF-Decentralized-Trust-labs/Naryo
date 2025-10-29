package io.naryo.api.node.common.request.connection;

import java.time.Duration;

import io.naryo.domain.node.connection.RetryConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Retry configuration request")
public record RetryConfigurationRequest(Integer times, Duration backoff) {
    public RetryConfiguration toDomain() {
        return new RetryConfiguration(times, backoff);
    }
}
