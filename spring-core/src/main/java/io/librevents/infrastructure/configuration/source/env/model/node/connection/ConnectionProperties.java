package io.librevents.infrastructure.configuration.source.env.model.node.connection;

import io.librevents.domain.node.connection.NodeConnectionType;
import io.librevents.infrastructure.configuration.source.env.model.common.ConnectionEndpointProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ConnectionProperties(
        @NotNull NodeConnectionType type,
        @Valid @NotNull RetryConfigurationProperties retry,
        @Valid @NotNull ConnectionEndpointProperties endpoint,
        @Valid @NotNull ConnectionConfigurationProperties configuration) {}
