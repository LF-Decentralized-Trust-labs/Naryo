package io.naryo.infrastructure.configuration.source.env.model;

import java.util.List;

import io.naryo.infrastructure.configuration.source.env.model.broadcaster.BroadcastingProperties;
import io.naryo.infrastructure.configuration.source.env.model.event.store.EventStoreConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.FilterProperties;
import io.naryo.infrastructure.configuration.source.env.model.http.HttpClientProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.NodeProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record EnvironmentProperties(
        @Valid @NotNull HttpClientProperties httpClient,
        @Valid @NotNull BroadcastingProperties broadcasting,
        @Valid @NotNull List<NodeProperties> nodes,
        @Valid @NotNull List<FilterProperties> filters,
        @Valid @NotNull EventStoreConfigurationProperties eventStore) {

    public EnvironmentProperties(
            HttpClientProperties httpClient,
            BroadcastingProperties broadcasting,
            List<NodeProperties> nodes,
            List<FilterProperties> filters,
            EventStoreConfigurationProperties eventStore) {
        this.httpClient = httpClient != null ? httpClient : new HttpClientProperties();
        this.broadcasting =
                broadcasting != null
                        ? broadcasting
                        : new BroadcastingProperties(List.of(), List.of());
        this.nodes = nodes != null ? nodes : List.of();
        this.filters = filters != null ? filters : List.of();
        this.eventStore = eventStore;
    }
}
