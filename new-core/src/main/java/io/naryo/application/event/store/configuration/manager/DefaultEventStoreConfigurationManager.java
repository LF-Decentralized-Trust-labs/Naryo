package io.naryo.application.event.store.configuration.manager;

import java.util.List;
import java.util.function.BinaryOperator;

import io.naryo.application.configuration.manager.BaseConfigurationManager;
import io.naryo.application.configuration.provider.ConfigurationProvider;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;

public final class DefaultEventStoreConfigurationManager
        extends BaseConfigurationManager<EventStoreConfiguration>
        implements EventStoreConfigurationManager {

    DefaultEventStoreConfigurationManager(
            List<ConfigurationProvider<EventStoreConfiguration>> configurationProviders) {
        super(configurationProviders);
    }

    @Override
    protected BinaryOperator<EventStoreConfiguration> mergeFunction() {
        return EventStoreConfiguration::merge;
    }
}
