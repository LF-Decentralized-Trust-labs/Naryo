package io.librevents.infrastructure.configuration;

import java.util.List;

import io.librevents.application.broadcaster.configuration.manager.BroadcasterConfigurationConfigurationManager;
import io.librevents.application.broadcaster.configuration.manager.BroadcasterConfigurationManager;
import io.librevents.application.filter.configuration.manager.FilterConfigurationManager;
import io.librevents.application.node.configuration.manager.NodeConfigurationManager;
import io.librevents.domain.broadcaster.Broadcaster;
import io.librevents.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.librevents.domain.filter.Filter;
import io.librevents.domain.node.Node;
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
