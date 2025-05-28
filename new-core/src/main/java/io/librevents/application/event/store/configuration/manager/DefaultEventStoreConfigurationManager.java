package io.librevents.application.event.store.configuration.manager;

import java.util.List;
import java.util.function.BinaryOperator;

import io.librevents.application.configuration.manager.BaseConfigurationManager;
import io.librevents.application.configuration.provider.ConfigurationProvider;
import io.librevents.domain.configuration.eventstore.EventStoreConfiguration;

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
