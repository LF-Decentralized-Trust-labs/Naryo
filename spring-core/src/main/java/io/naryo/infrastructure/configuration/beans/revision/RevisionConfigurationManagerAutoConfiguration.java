package io.naryo.infrastructure.configuration.beans.revision;

import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationConfigurationManager;
import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationManager;
import io.naryo.application.broadcaster.configuration.revision.BroadcasterConfigurationConfigurationRevisionManager;
import io.naryo.application.broadcaster.configuration.revision.BroadcasterConfigurationRevisionFingerprinter;
import io.naryo.application.broadcaster.revision.BroadcasterConfigurationRevisionManager;
import io.naryo.application.broadcaster.revision.BroadcasterRevisionFingerprinter;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.filter.configuration.manager.FilterConfigurationManager;
import io.naryo.application.filter.revision.FilterConfigurationRevisionManager;
import io.naryo.application.filter.revision.FilterRevisionFingerprinter;
import io.naryo.application.node.configuration.manager.NodeConfigurationManager;
import io.naryo.application.node.revision.NodeConfigurationRevisionManager;
import io.naryo.application.node.revision.NodeRevisionFingerprinter;
import io.naryo.application.store.configuration.manager.StoreConfigurationManager;
import io.naryo.application.store.revision.StoreConfigurationRevisionManager;
import io.naryo.application.store.revision.StoreRevisionFingerprinter;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RevisionConfigurationManagerAutoConfiguration {

    @Bean
    public NodeConfigurationRevisionManager nodeConfigurationRevisionManager(
            NodeConfigurationManager configurationManager,
            NodeRevisionFingerprinter fingerprinter,
            LiveRegistry<Node> live) {
        return new NodeConfigurationRevisionManager(configurationManager, fingerprinter, live);
    }

    @Bean
    public BroadcasterConfigurationRevisionManager broadcasterConfigurationRevisionManager(
            BroadcasterConfigurationManager configurationManager,
            BroadcasterRevisionFingerprinter fingerprinter,
            LiveRegistry<Broadcaster> live) {
        return new BroadcasterConfigurationRevisionManager(
                configurationManager, fingerprinter, live);
    }

    @Bean
    public BroadcasterConfigurationConfigurationRevisionManager
            broadcasterConfigurationConfigurationRevisionManager(
                    BroadcasterConfigurationConfigurationManager configurationManager,
                    BroadcasterConfigurationRevisionFingerprinter fingerprinter,
                    LiveRegistry<BroadcasterConfiguration> live) {
        return new BroadcasterConfigurationConfigurationRevisionManager(
                configurationManager, fingerprinter, live);
    }

    @Bean
    public FilterConfigurationRevisionManager filterConfigurationRevisionManager(
            FilterConfigurationManager configurationManager,
            FilterRevisionFingerprinter fingerprinter,
            LiveRegistry<Filter> live) {
        return new FilterConfigurationRevisionManager(configurationManager, fingerprinter, live);
    }

    @Bean
    public StoreConfigurationRevisionManager storeConfigurationRevisionManager(
            StoreConfigurationManager configurationManager,
            StoreRevisionFingerprinter fingerprinter,
            LiveRegistry<StoreConfiguration> live) {
        return new StoreConfigurationRevisionManager(configurationManager, fingerprinter, live);
    }
}
