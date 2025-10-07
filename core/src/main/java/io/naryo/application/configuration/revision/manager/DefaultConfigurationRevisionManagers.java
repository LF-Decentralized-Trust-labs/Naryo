package io.naryo.application.configuration.revision.manager;

import java.util.*;

import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;

public final class DefaultConfigurationRevisionManagers implements ConfigurationRevisionManagers {

    private final ConfigurationRevisionManager<Node> nodes;
    private final ConfigurationRevisionManager<Filter> filters;
    private final ConfigurationRevisionManager<Broadcaster> broadcasters;
    private final ConfigurationRevisionManager<BroadcasterConfiguration> broadcasterConfigurations;
    private final ConfigurationRevisionManager<StoreConfiguration> stores;

    public DefaultConfigurationRevisionManagers(
            ConfigurationRevisionManager<Node> nodes,
            ConfigurationRevisionManager<Filter> filters,
            ConfigurationRevisionManager<Broadcaster> broadcasters,
            ConfigurationRevisionManager<BroadcasterConfiguration> broadcasterConfigurations,
            ConfigurationRevisionManager<StoreConfiguration> stores) {
        this.nodes = nodes;
        this.filters = filters;
        this.broadcasters = broadcasters;
        this.broadcasterConfigurations = broadcasterConfigurations;
        this.stores = stores;
    }

    @Override
    public void initialize() {
        nodes.initialize();
        filters.initialize();
        broadcasters.initialize();
        broadcasterConfigurations.initialize();
        stores.initialize();
    }

    @Override
    public ConfigurationRevisionManager<Node> nodes() {
        return nodes;
    }

    @Override
    public ConfigurationRevisionManager<Filter> filters() {
        return filters;
    }

    @Override
    public ConfigurationRevisionManager<Broadcaster> broadcasters() {
        return broadcasters;
    }

    @Override
    public ConfigurationRevisionManager<BroadcasterConfiguration> broadcasterConfigurations() {
        return broadcasterConfigurations;
    }

    @Override
    public ConfigurationRevisionManager<StoreConfiguration> storeConfigurations() {
        return stores;
    }

    @Override
    public <T> ConfigurationRevisionManager<T> get(Class<T> domainClass) {
        if (Node.class.isAssignableFrom(domainClass)) {
            return (ConfigurationRevisionManager<T>) nodes();
        } else if (Filter.class.isAssignableFrom(domainClass)) {
            return (ConfigurationRevisionManager<T>) filters();
        } else if (Broadcaster.class.isAssignableFrom(domainClass)) {
            return (ConfigurationRevisionManager<T>) broadcasters();
        } else if (BroadcasterConfiguration.class.isAssignableFrom(domainClass)) {
            return (ConfigurationRevisionManager<T>) broadcasterConfigurations();
        } else if (StoreConfiguration.class.isAssignableFrom(domainClass)) {
            return (ConfigurationRevisionManager<T>) storeConfigurations();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported class for configuration revision" + domainClass);
        }
    }

    @Override
    public List<ConfigurationRevisionManager<?>> all() {
        return List.of(nodes, filters, broadcasters, broadcasterConfigurations, stores);
    }
}
