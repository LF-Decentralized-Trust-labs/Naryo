package io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.http;

import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationAdditionalProperties;
import io.librevents.infrastructure.configuration.source.env.model.common.ConnectionEndpointProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record HttpBroadcasterConfigurationAdditionalProperties(
        @Valid @NotNull ConnectionEndpointProperties endpoint)
        implements BroadcasterConfigurationAdditionalProperties {}
