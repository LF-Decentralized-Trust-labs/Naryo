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

    private final BroadcasterConfigurationConfigurationManager bccm;
    private final BroadcasterConfigurationManager bcm;
    private final FilterConfigurationManager fcm;
    private final NodeConfigurationManager ncm;

    public ConfigurationFacade(
            BroadcasterConfigurationConfigurationManager bccm,
            BroadcasterConfigurationManager bcm,
            FilterConfigurationManager fcm,
            NodeConfigurationManager ncm) {
        this.bccm = bccm;
        this.bcm = bcm;
        this.fcm = fcm;
        this.ncm = ncm;
    }

    public List<BroadcasterConfiguration> getBroadcasterConfigurations() {
        return List.copyOf(bccm.load());
    }

    public List<Broadcaster> getBroadcasters() {
        return List.copyOf(bcm.load());
    }

    public List<Filter> getFilters() {
        return List.copyOf(fcm.load());
    }

    public List<Node> getNodes() {
        return List.copyOf(ncm.load());
    }
}
