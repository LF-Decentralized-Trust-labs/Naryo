package io.librevents.infrastructure.configuration.source.env.model.node.connection;

import io.librevents.domain.node.connection.NodeConnectionType;
import io.librevents.infrastructure.configuration.source.env.model.common.ConnectionEndpointProperties;

public record ConnectionProperties(
        NodeConnectionType type,
        RetryConfigurationProperties retry,
        ConnectionEndpointProperties endpoint,
        ConnectionConfigurationProperties configuration) {}
