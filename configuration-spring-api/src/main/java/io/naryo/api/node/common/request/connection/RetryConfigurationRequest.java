package io.naryo.api.node.common.request.connection;

import java.time.Duration;

import io.naryo.application.configuration.source.model.node.connection.factory.DefaultNodeConnectionFactory;
import io.naryo.domain.node.connection.RetryConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Retry configuration request")
public record RetryConfigurationRequest(
        @PositiveOrZero @Schema(defaultValue = "3") Integer times,
        @Schema(defaultValue = "PT30S", description = "ISO 8601 duration, must be positive") Duration backoff) {

    public RetryConfiguration toDomain() {
        int resolvedTimes = times != null ? times : DefaultNodeConnectionFactory.DEFAULT_RETRY_TIMES;
        Duration resolvedBackoff = backoff != null ? backoff : DefaultNodeConnectionFactory.DEFAULT_RETRY_BACKOFF;
        return new RetryConfiguration(resolvedTimes, resolvedBackoff);
    }
}
