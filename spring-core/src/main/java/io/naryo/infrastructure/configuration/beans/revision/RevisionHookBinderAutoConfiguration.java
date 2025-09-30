package io.naryo.infrastructure.configuration.beans.revision;

import io.naryo.application.broadcaster.configuration.revision.BroadcasterConfigurationConfigurationRevisionManager;
import io.naryo.application.broadcaster.revision.BroadcasterConfigurationRevisionManager;
import io.naryo.application.configuration.revision.hook.DefaultRevisionHookBinder;
import io.naryo.application.filter.revision.FilterConfigurationRevisionManager;
import io.naryo.application.node.revision.NodeConfigurationRevisionManager;
import io.naryo.application.store.revision.StoreConfigurationRevisionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RevisionHookBinderAutoConfiguration {

    @Bean
    public DefaultRevisionHookBinder defaultRevisionHookBinder(
            NodeConfigurationRevisionManager nodeConfigurationRevisionManager,
            BroadcasterConfigurationRevisionManager broadcasterConfigurationRevisionManager,
            BroadcasterConfigurationConfigurationRevisionManager
                    broadcasterConfigurationConfigurationRevisionManager,
            StoreConfigurationRevisionManager storeConfigurationRevisionManager,
            FilterConfigurationRevisionManager filterConfigurationRevisionManager) {
        return new DefaultRevisionHookBinder(
                nodeConfigurationRevisionManager,
                broadcasterConfigurationRevisionManager,
                broadcasterConfigurationConfigurationRevisionManager,
                storeConfigurationRevisionManager,
                filterConfigurationRevisionManager);
    }
}
