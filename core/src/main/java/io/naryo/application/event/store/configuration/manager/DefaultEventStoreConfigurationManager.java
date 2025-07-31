package io.naryo.application.event.store.configuration.manager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.naryo.application.configuration.manager.BaseCollectionConfigurationManager;
import io.naryo.application.configuration.source.model.event.DatabaseBlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.event.EventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.event.ServerBlockEventStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.mapper.EventStoreConfigurationMapperRegistry;
import io.naryo.application.event.store.configuration.provider.EventStoreSourceProvider;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;

public final class DefaultEventStoreConfigurationManager
        extends BaseCollectionConfigurationManager<
                EventStoreConfiguration, EventStoreConfigurationDescriptor, UUID>
        implements EventStoreConfigurationManager {

    private final EventStoreConfigurationMapperRegistry registry;

    public DefaultEventStoreConfigurationManager(
            List<EventStoreSourceProvider> sourceProviders,
            EventStoreConfigurationMapperRegistry registry) {
        super(sourceProviders);
        this.registry = registry;
    }

    @Override
    protected Collector<
                    EventStoreConfigurationDescriptor,
                    ?,
                    Map<UUID, EventStoreConfigurationDescriptor>>
            getCollector() {
        return Collectors.toMap(
                EventStoreConfigurationDescriptor::getNodeId,
                Function.identity(),
                EventStoreConfigurationDescriptor::merge,
                LinkedHashMap::new);
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
