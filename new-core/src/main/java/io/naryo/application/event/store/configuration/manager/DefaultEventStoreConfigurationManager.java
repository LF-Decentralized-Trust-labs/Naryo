package io.naryo.application.event.store.configuration.manager;

import java.util.List;
import java.util.function.BinaryOperator;

import io.naryo.application.configuration.manager.BaseConfigurationManager;
import io.naryo.application.configuration.source.model.event.DatabaseBlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.event.EventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.event.ServerBlockEventStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.mapper.EventStoreConfigurationMapperRegistry;
import io.naryo.application.event.store.configuration.provider.EventStoreSourceProvider;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;

public final class DefaultEventStoreConfigurationManager
        extends BaseConfigurationManager<EventStoreConfiguration, EventStoreConfigurationDescriptor>
        implements EventStoreConfigurationManager {

    private final EventStoreConfigurationMapperRegistry registry;

    public DefaultEventStoreConfigurationManager(
            List<EventStoreSourceProvider> sourceProviders,
            EventStoreConfigurationMapperRegistry registry) {
        super(sourceProviders);
        this.registry = registry;
    }

    @Override
    protected BinaryOperator<EventStoreConfigurationDescriptor> mergeFunction() {
        return EventStoreConfigurationDescriptor::merge;
    }

    @Override
    protected EventStoreConfiguration map(EventStoreConfigurationDescriptor source) {
        return switch (source) {
            case ServerBlockEventStoreConfigurationDescriptor server ->
                    registry.map(server.getServerType().getName(), server);
            case DatabaseBlockEventStoreConfigurationDescriptor database ->
                    registry.map(database.getEngine().getName(), database);
            default -> throw new IllegalStateException("Unexpected value: " + source);
        };
    }
}
