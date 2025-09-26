package io.naryo.infrastructure.configuration.beans.liveConfiguration;

import io.naryo.application.configuration.revision.registry.LiveRegistries;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.configuration.revision.registry.DefaultLiveRegistries;
import io.naryo.application.configuration.revision.registry.DefaultLiveRegistry;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.common.http.HttpClient;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RevisionAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(name = "broadcasterConfigurationsLiveRegistry")
    public LiveRegistry<BroadcasterConfiguration> broadcasterConfigurationsLiveRegistry() {
        return new DefaultLiveRegistry<>();
    }

    @Bean
    @ConditionalOnMissingBean(name = "broadcastersLiveRegistry")
    public LiveRegistry<Broadcaster> broadcastersLiveRegistry() {
        return new DefaultLiveRegistry<>();
    }

    @Bean
    @ConditionalOnMissingBean(name = "filtersLiveRegistry")
    public LiveRegistry<Filter> filtersLiveRegistry() {
        return new DefaultLiveRegistry<>();
    }

    @Bean
    @ConditionalOnMissingBean(name = "nodesLiveRegistry")
    public LiveRegistry<Node> nodesLiveRegistry() {
        return new DefaultLiveRegistry<>();
    }

    @Bean
    @ConditionalOnMissingBean(name = "storeConfigurationsLiveRegistry")
    public LiveRegistry<StoreConfiguration> storeConfigurationsLiveRegistry() {
        return new DefaultLiveRegistry<>();
    }

    @Bean
    @ConditionalOnMissingBean(name = "httpClientLiveRegistry")
    public LiveRegistry<HttpClient> httpClientLiveRegistry() {
        return new DefaultLiveRegistry<>();
    }

    @Bean
    @ConditionalOnMissingBean
    public LiveRegistries liveRegistries(
            LiveRegistry<BroadcasterConfiguration> broadcasterConfigurationsLiveRegistry,
            LiveRegistry<Broadcaster> broadcastersLiveRegistry,
            LiveRegistry<Filter> filtersLiveRegistry,
            LiveRegistry<Node> nodesLiveRegistry,
            LiveRegistry<StoreConfiguration> storeConfigurationsLiveRegistry,
            LiveRegistry<HttpClient> httpClientLiveRegistry) {
        return new DefaultLiveRegistries(
                broadcasterConfigurationsLiveRegistry,
                broadcastersLiveRegistry,
                filtersLiveRegistry,
                nodesLiveRegistry,
                storeConfigurationsLiveRegistry,
                httpClientLiveRegistry);
    }
}
