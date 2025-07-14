package io.naryo.application.event.store.configuration.manager;

import java.util.List;
import java.util.function.BinaryOperator;

import io.naryo.application.configuration.manager.BaseConfigurationManager;
import io.naryo.application.configuration.provider.SourceProvider;
import io.naryo.application.configuration.source.model.event.EventStoreDescriptor;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;

public final class DefaultEventStoreConfigurationManager
        extends BaseConfigurationManager<EventStoreConfiguration, EventStoreDescriptor>
        implements EventStoreConfigurationManager {

    DefaultEventStoreConfigurationManager(
            List<SourceProvider<EventStoreDescriptor>> sourceProviders) {
        super(sourceProviders);
    }

    @Override
    protected BinaryOperator<EventStoreDescriptor> mergeFunction() {
        return EventStoreDescriptor::merge;
    }

    @Override
    protected EventStoreConfiguration map(EventStoreDescriptor source) {
        return null;
    }
}
