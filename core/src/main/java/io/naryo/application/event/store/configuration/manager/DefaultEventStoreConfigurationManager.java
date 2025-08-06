package io.naryo.application.event.store.configuration.manager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.naryo.application.configuration.manager.BaseCollectionConfigurationManager;
import io.naryo.application.configuration.source.model.event.ActiveEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.event.EventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.event.InactiveEventStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.mapper.ActiveEventStoreConfigurationMapperRegistry;
import io.naryo.application.event.store.configuration.provider.EventStoreSourceProvider;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.inactive.InactiveStoreConfiguration;

public final class DefaultEventStoreConfigurationManager
        extends BaseCollectionConfigurationManager<
                StoreConfiguration, EventStoreConfigurationDescriptor, UUID>
        implements EventStoreConfigurationManager {

    private final ActiveEventStoreConfigurationMapperRegistry registry;

    public DefaultEventStoreConfigurationManager(
            List<EventStoreSourceProvider> sourceProviders,
            ActiveEventStoreConfigurationMapperRegistry registry) {
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
    protected StoreConfiguration map(EventStoreConfigurationDescriptor source) {
        return switch (source) {
            case InactiveEventStoreConfigurationDescriptor ignored ->
                    new InactiveStoreConfiguration(source.getNodeId());
            case ActiveEventStoreConfigurationDescriptor active ->
                    registry.map(active.getType().getName(), active);
            default -> throw new IllegalStateException("Unexpected value: " + source);
        };
    }
}
