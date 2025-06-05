package io.librevents.infrastructure.configuration.source.env.model;

import java.util.List;

import io.librevents.infrastructure.configuration.source.env.model.broadcaster.BroadcastingProperties;
import io.librevents.infrastructure.configuration.source.env.model.filter.FilterProperties;
import io.librevents.infrastructure.configuration.source.env.model.http.HttpClientProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.NodeProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record EnvironmentProperties(
        @Valid @NotNull HttpClientProperties httpClient,
        @Valid @NotNull BroadcastingProperties broadcasting,
        @Valid @NotNull List<NodeProperties> nodes,
        @Valid @NotNull List<FilterProperties> filters) {

    public EnvironmentProperties(
            HttpClientProperties httpClient,
            BroadcastingProperties broadcasting,
            List<NodeProperties> nodes,
            List<FilterProperties> filters) {
        this.httpClient = httpClient != null ? httpClient : new HttpClientProperties();
        this.broadcasting =
                broadcasting != null
                        ? broadcasting
                        : new BroadcastingProperties(List.of(), List.of());
        this.nodes = nodes != null ? nodes : List.of();
        this.filters = filters != null ? filters : List.of();
    }
}
