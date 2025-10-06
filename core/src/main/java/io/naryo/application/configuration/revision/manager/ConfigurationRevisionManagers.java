package io.naryo.application.configuration.revision.manager;

import java.util.List;

import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;

public interface ConfigurationRevisionManagers {

    void initialize();

    ConfigurationRevisionManager<Node> nodes();

    ConfigurationRevisionManager<Filter> filters();

    ConfigurationRevisionManager<Broadcaster> broadcasters();

    ConfigurationRevisionManager<BroadcasterConfiguration> broadcasterConfigurations();

    ConfigurationRevisionManager<StoreConfiguration> storeConfigurations();

    <T> ConfigurationRevisionManager<T> get(Class<T> domainClass);

    List<ConfigurationRevisionManager<?>> all();
}
