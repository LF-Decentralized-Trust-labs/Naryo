package io.librevents.infrastructure.configuration;

import java.util.List;

import io.librevents.infrastructure.configuration.broadcaster.BroadcastingProperties;
import io.librevents.infrastructure.configuration.http.HttpClientProperties;
import io.librevents.infrastructure.configuration.node.NodeProperties;

public record MainProperties(
        HttpClientProperties httpClient,
        BroadcastingProperties broadcasting,
        List<NodeProperties> nodes) {}
