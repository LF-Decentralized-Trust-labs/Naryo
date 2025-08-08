package io.naryo.application.event.store.configuration.manager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.naryo.application.configuration.manager.BaseCollectionConfigurationManager;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.InactiveStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.mapper.ActiveEventStoreConfigurationMapperRegistry;
import io.naryo.application.event.store.configuration.provider.EventStoreSourceProvider;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.inactive.InactiveStoreConfiguration;

public final class DefaultStoreConfigurationManager
        extends BaseCollectionConfigurationManager<
                StoreConfiguration, StoreConfigurationDescriptor, UUID>
        implements StoreConfigurationManager {

    private final ActiveEventStoreConfigurationMapperRegistry registry;

    public DefaultStoreConfigurationManager(
            List<EventStoreSourceProvider> sourceProviders,
            ActiveEventStoreConfigurationMapperRegistry registry) {
        super(sourceProviders);
        this.registry = registry;
    }

    @Override
    protected Collector<StoreConfigurationDescriptor, ?, Map<UUID, StoreConfigurationDescriptor>>
            getCollector() {
        return Collectors.toMap(
                StoreConfigurationDescriptor::getNodeId,
                Function.identity(),
                StoreConfigurationDescriptor::merge,
                LinkedHashMap::new);
    }

    @Override
    protected StoreConfiguration map(StoreConfigurationDescriptor source) {
        return switch (source) {
            case InactiveStoreConfigurationDescriptor ignored ->
                    new InactiveStoreConfiguration(source.getNodeId());
            case ActiveStoreConfigurationDescriptor active ->
                    registry.map(active.getType().getName(), active);
            default -> throw new IllegalStateException("Unexpected value: " + source);
        };
    }
}
