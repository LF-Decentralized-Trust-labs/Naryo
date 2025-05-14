package io.librevents.infrastructure.configuration.source.env.model;

import java.util.List;

import io.librevents.infrastructure.configuration.source.env.model.broadcaster.BroadcastingProperties;
import io.librevents.infrastructure.configuration.source.env.model.http.HttpClientProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.NodeProperties;

public record EnvironmentProperties(
        HttpClientProperties httpClient,
        BroadcastingProperties broadcasting,
        List<NodeProperties> nodes) {}
