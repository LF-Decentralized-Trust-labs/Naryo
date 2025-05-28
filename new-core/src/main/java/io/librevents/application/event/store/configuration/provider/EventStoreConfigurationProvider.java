package io.librevents.application.event.store.configuration.provider;

import io.librevents.application.configuration.provider.ConfigurationProvider;
import io.librevents.domain.configuration.eventstore.EventStoreConfiguration;

public interface EventStoreConfigurationProvider
        extends ConfigurationProvider<EventStoreConfiguration> {}
