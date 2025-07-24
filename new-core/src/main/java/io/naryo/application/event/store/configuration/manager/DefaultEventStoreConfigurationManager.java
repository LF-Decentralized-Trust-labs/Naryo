package io.naryo.application.event.store.configuration.manager;

import java.util.List;
import java.util.function.BinaryOperator;

import io.naryo.application.configuration.manager.BaseConfigurationManager;
import io.naryo.application.configuration.source.model.event.EventStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.provider.EventStoreSourceProvider;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;

public final class DefaultEventStoreConfigurationManager
        extends BaseConfigurationManager<EventStoreConfiguration, EventStoreConfigurationDescriptor>
        implements EventStoreConfigurationManager {

    public DefaultEventStoreConfigurationManager(List<EventStoreSourceProvider> sourceProviders) {
        super(sourceProviders);
    }

    @Override
    protected BinaryOperator<EventStoreConfigurationDescriptor> mergeFunction() {
        return EventStoreConfigurationDescriptor::merge;
    }

    @Override
    protected EventStoreConfiguration map(EventStoreConfigurationDescriptor source) {
        return null;
    }
}
