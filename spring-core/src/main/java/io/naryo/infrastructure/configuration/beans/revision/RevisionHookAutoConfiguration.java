package io.naryo.infrastructure.configuration.beans.revision;

import java.util.Set;

import io.naryo.application.broadcaster.configuration.revision.BroadcasterConfigurationConfigurationRevisionManager;
import io.naryo.application.broadcaster.revision.BroadcasterConfigurationRevisionManager;
import io.naryo.application.configuration.resilence.ResilienceRegistry;
import io.naryo.application.configuration.revision.hook.*;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.filter.revision.FilterConfigurationRevisionManager;
import io.naryo.application.node.NodeInitializer;
import io.naryo.application.node.NodeLifecycle;
import io.naryo.application.node.revision.NodeConfigurationRevisionManager;
import io.naryo.application.store.Store;
import io.naryo.application.store.revision.StoreConfigurationRevisionManager;
import io.naryo.domain.configuration.store.StoreConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RevisionHookAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RevisionHookBinder revisionHookBinder(
            NodeConfigurationRevisionManager nodeConfigurationRevisionManager,
            BroadcasterConfigurationRevisionManager broadcasterConfigurationRevisionManager,
            BroadcasterConfigurationConfigurationRevisionManager
                    broadcasterConfigurationConfigurationRevisionManager,
            StoreConfigurationRevisionManager storeConfigurationRevisionManager,
            FilterConfigurationRevisionManager filterConfigurationRevisionManager,
            DefaultFilterRuntimeHook defaultFilterRuntimeHook,
            DefaultNodeRuntimeHook defaultNodeRuntimeHook,
            DefaultStoreConfigurationRuntimeHook defaultStoreConfigurationRuntimeHook) {
        return new DefaultRevisionHookBinder(
                nodeConfigurationRevisionManager,
                broadcasterConfigurationRevisionManager,
                broadcasterConfigurationConfigurationRevisionManager,
                storeConfigurationRevisionManager,
                filterConfigurationRevisionManager,
                defaultFilterRuntimeHook,
                defaultNodeRuntimeHook,
                defaultStoreConfigurationRuntimeHook);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultFilterRuntimeHook defaultFilterRuntimeHook(
            NodeLifecycle nodeLifecycle,
            ContractEventParameterDecoder decoder,
            ResilienceRegistry resilienceRegistry,
            LiveRegistry<StoreConfiguration> storeLiveRegistry,
            Set<Store<?, ?, ?>> stores) {
        return new DefaultFilterRuntimeHook(
                nodeLifecycle, decoder, resilienceRegistry, storeLiveRegistry, stores);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultNodeRuntimeHook defaultNodeRuntimeHook(
            NodeLifecycle nodeLifecycle, NodeInitializer nodeInitializer) {
        return new DefaultNodeRuntimeHook(nodeLifecycle, nodeInitializer);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultStoreConfigurationRuntimeHook defaultStoreConfigurationRuntimeHook(
            NodeLifecycle nodeLifecycle,
            NodeInitializer nodeInitializer,
            NodeConfigurationRevisionManager nodeConfigurationRevisionManager) {
        return new DefaultStoreConfigurationRuntimeHook(
                nodeLifecycle, nodeInitializer, nodeConfigurationRevisionManager);
    }
}
