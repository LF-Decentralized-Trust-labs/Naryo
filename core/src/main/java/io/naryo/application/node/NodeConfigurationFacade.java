package io.naryo.application.node;

import java.util.List;

import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationConfigurationManager;
import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationManager;
import io.naryo.application.filter.configuration.manager.FilterConfigurationManager;
import io.naryo.application.node.configuration.manager.NodeConfigurationManager;
import io.naryo.application.store.configuration.manager.StoreConfigurationManager;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;

public record NodeConfigurationFacade(
        BroadcasterConfigurationConfigurationManager broadcasterConfigurationConfigurationManager,
        BroadcasterConfigurationManager broadcasterConfigurationManager,
        FilterConfigurationManager filterConfigurationManager,
        NodeConfigurationManager nodeConfigurationManager,
        StoreConfigurationManager storeConfigurationManager) {

    public List<BroadcasterConfiguration> getBroadcasterConfigurations() {
        return List.copyOf(broadcasterConfigurationConfigurationManager.load());
    }

    public List<Broadcaster> getBroadcasters() {
        return List.copyOf(broadcasterConfigurationManager.load());
    }

    public List<Filter> getFilters() {
        return List.copyOf(filterConfigurationManager.load());
    }

    public List<Node> getNodes() {
        return List.copyOf(nodeConfigurationManager.load());
    }

    public List<StoreConfiguration> getStoreConfigurations() {
        return List.copyOf(storeConfigurationManager.load());
    }
}
