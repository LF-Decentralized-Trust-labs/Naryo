package io.naryo.application.configuration.revision.impl;

import java.util.Map;
import java.util.Objects;

import io.naryo.application.configuration.revision.LiveRegistries;
import io.naryo.application.configuration.revision.LiveRegistry;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.common.http.HttpClient;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;

public class DefaultLiveRegistries implements LiveRegistries {

    private final LiveRegistry<BroadcasterConfiguration> broadcasterConfigurations;
    private final LiveRegistry<Broadcaster> broadcasters;
    private final LiveRegistry<Filter> filters;
    private final LiveRegistry<Node> nodes;
    private final LiveRegistry<StoreConfiguration> storeConfigurations;
    private final LiveRegistry<HttpClient> httpClient;

    private final Map<Class<?>, LiveRegistry<?>> byType;

    public DefaultLiveRegistries(
            LiveRegistry<BroadcasterConfiguration> broadcasterConfigurations,
            LiveRegistry<Broadcaster> broadcasters,
            LiveRegistry<Filter> filters,
            LiveRegistry<Node> nodes,
            LiveRegistry<StoreConfiguration> storeConfigurations,
            LiveRegistry<HttpClient> httpClient) {
        this.broadcasterConfigurations =
                Objects.requireNonNull(
                        broadcasterConfigurations,
                        "broadcasterConfigurations registry must not be null");
        this.nodes = Objects.requireNonNull(nodes, "nodes registry must not be null");
        this.filters = Objects.requireNonNull(filters, "filters registry must not be null");
        this.broadcasters =
                Objects.requireNonNull(broadcasters, "broadcasters registry must not be null");
        this.storeConfigurations =
                Objects.requireNonNull(storeConfigurations, "stores registry must not be null");
        this.httpClient =
                Objects.requireNonNull(httpClient, "http client registry must not be null");

        this.byType =
                Map.of(
                        BroadcasterConfiguration.class,
                        broadcasterConfigurations,
                        Broadcaster.class,
                        broadcasters,
                        Filter.class,
                        filters,
                        Node.class,
                        nodes,
                        StoreConfiguration.class,
                        storeConfigurations,
                        HttpClient.class,
                        httpClient);
    }

    @Override
    public LiveRegistry<BroadcasterConfiguration> broadcasterConfigurations() {
        return this.broadcasterConfigurations;
    }

    @Override
    public LiveRegistry<Broadcaster> broadcasters() {
        return this.broadcasters;
    }

    @Override
    public LiveRegistry<Filter> filters() {
        return this.filters;
    }

    @Override
    public LiveRegistry<Node> nodes() {
        return this.nodes;
    }

    @Override
    public LiveRegistry<StoreConfiguration> storeConfigurations() {
        return this.storeConfigurations;
    }

    @Override
    public LiveRegistry<HttpClient> httpClient() {
        return this.httpClient;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> LiveRegistry<T> get(Class<T> domainClass) {
        LiveRegistry<?> registry = byType.get(domainClass);
        if (registry == null) {
            throw new IllegalArgumentException(
                    "No LiveRegistry registered for domain class: " + domainClass.getName());
        }
        return (LiveRegistry<T>) registry;
    }
}
