package io.librevents.infrastructure.configuration.broadcaster.configuration.http;

import io.librevents.infrastructure.configuration.broadcaster.configuration.BroadcasterConfigurationAdditionalProperties;
import io.librevents.infrastructure.configuration.common.ConnectionEndpointProperties;

public record HttpBroadcasterConfigurationAdditionalProperties(
        ConnectionEndpointProperties endpoint)
        implements BroadcasterConfigurationAdditionalProperties {}
