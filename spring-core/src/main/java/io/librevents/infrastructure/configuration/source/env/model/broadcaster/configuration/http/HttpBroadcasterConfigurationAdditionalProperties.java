package io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.http;

import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationAdditionalProperties;
import io.librevents.infrastructure.configuration.source.env.model.common.ConnectionEndpointProperties;

public record HttpBroadcasterConfigurationAdditionalProperties(
        ConnectionEndpointProperties endpoint)
        implements BroadcasterConfigurationAdditionalProperties {}
