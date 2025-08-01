package io.naryo.infrastructure.configuration.beans.eventstore.configuration;

import java.util.List;

import io.naryo.application.event.store.configuration.manager.DefaultEventStoreConfigurationManager;
import io.naryo.application.event.store.configuration.manager.EventStoreConfigurationManager;
import io.naryo.application.event.store.configuration.mapper.ActiveEventStoreConfigurationMapperRegistry;
import io.naryo.application.event.store.configuration.provider.EventStoreSourceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(EventStoreConfigurationManager.class)
    public EventStoreConfigurationManager eventStoreConfigurationManager(
            List<EventStoreSourceProvider> providers,
            ActiveEventStoreConfigurationMapperRegistry registry) {
        return new DefaultEventStoreConfigurationManager(providers, registry);
    }

    @Bean
    @ConditionalOnMissingBean(ActiveEventStoreConfigurationMapperRegistry.class)
    public ActiveEventStoreConfigurationMapperRegistry eventStoreConfigurationMapperRegistry() {
        return new ActiveEventStoreConfigurationMapperRegistry();
    }
}
