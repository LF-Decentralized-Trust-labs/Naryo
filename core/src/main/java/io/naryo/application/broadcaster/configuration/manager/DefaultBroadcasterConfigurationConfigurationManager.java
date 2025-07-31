package io.naryo.application.broadcaster.configuration.manager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.naryo.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.naryo.application.configuration.manager.BaseCollectionConfigurationManager;
import io.naryo.application.configuration.provider.CollectionSourceProvider;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;

public final class DefaultBroadcasterConfigurationConfigurationManager
        extends BaseCollectionConfigurationManager<
                BroadcasterConfiguration, BroadcasterConfigurationDescriptor, UUID>
        implements BroadcasterConfigurationConfigurationManager {

    private final BroadcasterConfigurationMapperRegistry registry;

    public DefaultBroadcasterConfigurationConfigurationManager(
            List<? extends CollectionSourceProvider<BroadcasterConfigurationDescriptor>>
                    collectionConfigurationProviders,
            BroadcasterConfigurationMapperRegistry registry) {
        super(collectionConfigurationProviders);
        this.registry = registry;
    }

    @Override
    protected Collector<
                    BroadcasterConfigurationDescriptor,
                    ?,
                    Map<UUID, BroadcasterConfigurationDescriptor>>
            getCollector() {
        return Collectors.toMap(
                BroadcasterConfigurationDescriptor::getId,
                Function.identity(),
                BroadcasterConfigurationDescriptor::merge,
                LinkedHashMap::new);
    }

    @Override
    protected BroadcasterConfiguration map(BroadcasterConfigurationDescriptor source) {
        return registry.map(source.getType().getName(), source);
    }
}
