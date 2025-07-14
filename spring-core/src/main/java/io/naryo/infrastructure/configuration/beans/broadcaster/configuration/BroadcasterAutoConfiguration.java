package io.naryo.infrastructure.configuration.beans.broadcaster.configuration;

import java.util.List;

import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationConfigurationManager;
import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationManager;
import io.naryo.application.broadcaster.configuration.manager.DefaultBroadcasterConfigurationConfigurationManager;
import io.naryo.application.broadcaster.configuration.manager.DefaultBroadcasterConfigurationManager;
import io.naryo.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.provider.broadcaster.BroadcasterConfigurationSourceProvider;
import io.naryo.application.configuration.source.provider.broadcaster.BroadcasterSourceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BroadcasterAutoConfiguration {

    @Bean
    public BroadcasterConfigurationMapperRegistry broadcasterConfigurationMapperRegistry() {
        return new BroadcasterConfigurationMapperRegistry();
    }

    @Bean
    public ConfigurationSchemaRegistry configurationSchemaRegistry() {
        return new ConfigurationSchemaRegistry();
    }

    @Bean
    @ConditionalOnMissingBean(BroadcasterConfigurationManager.class)
    public BroadcasterConfigurationManager broadcasterConfigurationManager(
            List<BroadcasterSourceProvider> providers) {
        return new DefaultBroadcasterConfigurationManager(providers);
    }

    @Bean
    @ConditionalOnMissingBean(BroadcasterConfigurationConfigurationManager.class)
    public BroadcasterConfigurationConfigurationManager
            broadcasterConfigurationConfigurationManager(
                    List<BroadcasterConfigurationSourceProvider> providers,
                    BroadcasterConfigurationMapperRegistry registry) {
        return new DefaultBroadcasterConfigurationConfigurationManager(providers, registry);
    }
}
