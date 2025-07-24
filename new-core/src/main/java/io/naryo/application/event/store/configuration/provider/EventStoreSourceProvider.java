package io.naryo.application.event.store.configuration.provider;

import io.naryo.application.configuration.provider.SourceProvider;
import io.naryo.application.configuration.source.model.event.EventStoreConfigurationDescriptor;

public interface EventStoreSourceProvider
        extends SourceProvider<EventStoreConfigurationDescriptor> {}
