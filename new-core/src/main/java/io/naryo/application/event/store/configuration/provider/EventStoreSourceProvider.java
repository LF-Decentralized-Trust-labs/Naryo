package io.naryo.application.event.store.configuration.provider;

import io.naryo.application.configuration.provider.CollectionSourceProvider;
import io.naryo.application.configuration.source.model.event.EventStoreConfigurationDescriptor;

public interface EventStoreSourceProvider
        extends CollectionSourceProvider<EventStoreConfigurationDescriptor> {}
