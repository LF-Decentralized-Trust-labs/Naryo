package io.naryo.infrastructure.configuration;

import java.util.List;

import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationConfigurationManager;
import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationManager;
import io.naryo.application.filter.configuration.manager.FilterConfigurationManager;
import io.naryo.application.node.configuration.manager.NodeConfigurationManager;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;
import org.springframework.stereotype.Component;

@Component
public final class ConfigurationFacade {

    private final BroadcasterConfigurationConfigurationManager
            broadcasterConfigurationConfigurationManager;
    private final BroadcasterConfigurationManager broadcasterConfigurationManager;
    private final FilterConfigurationManager filterConfigurationManager;
    private final NodeConfigurationManager nodeConfigurationManager;

    public ConfigurationFacade(
            BroadcasterConfigurationConfigurationManager
                    broadcasterConfigurationConfigurationManager,
            BroadcasterConfigurationManager broadcasterConfigurationManager,
            FilterConfigurationManager filterConfigurationManager,
            NodeConfigurationManager nodeConfigurationManager) {
        this.broadcasterConfigurationConfigurationManager =
                broadcasterConfigurationConfigurationManager;
        this.broadcasterConfigurationManager = broadcasterConfigurationManager;
        this.filterConfigurationManager = filterConfigurationManager;
        this.nodeConfigurationManager = nodeConfigurationManager;
    }

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
}
