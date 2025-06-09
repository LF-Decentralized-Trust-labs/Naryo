package io.naryo.application.event.store.configuration.provider;

import io.naryo.application.configuration.provider.ConfigurationProvider;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;

public interface EventStoreConfigurationProvider
        extends ConfigurationProvider<EventStoreConfiguration> {}
