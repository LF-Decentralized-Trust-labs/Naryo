package io.naryo.application.store.configuration.manager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.naryo.application.configuration.manager.BaseCollectionConfigurationManager;
import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import io.naryo.application.store.configuration.mapper.StoreConfigurationDescriptorMapper;
import io.naryo.application.store.configuration.provider.StoreSourceProvider;
import io.naryo.domain.configuration.store.StoreConfiguration;

public final class DefaultStoreConfigurationManager
        extends BaseCollectionConfigurationManager<
                StoreConfiguration, StoreConfigurationDescriptor, UUID>
        implements StoreConfigurationManager {

    private final StoreConfigurationDescriptorMapper descriptorToDomainMapper;

    public DefaultStoreConfigurationManager(
            List<StoreSourceProvider> sourceProviders,
            StoreConfigurationDescriptorMapper descriptorToDomainMapper) {
        super(sourceProviders);
        this.descriptorToDomainMapper = descriptorToDomainMapper;
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
        return descriptorToDomainMapper.map(source);
    }
}
