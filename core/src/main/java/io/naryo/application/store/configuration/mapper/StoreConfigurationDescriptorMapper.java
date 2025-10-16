package io.naryo.application.store.configuration.mapper;

import io.naryo.application.configuration.mapper.DescriptorToDomainMapper;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.InactiveStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.inactive.InactiveStoreConfiguration;

public class StoreConfigurationDescriptorMapper
        implements DescriptorToDomainMapper<StoreConfiguration, StoreConfigurationDescriptor> {

    private final ActiveStoreConfigurationMapperRegistry registry;

    public StoreConfigurationDescriptorMapper(ActiveStoreConfigurationMapperRegistry registry) {
        this.registry = registry;
    }

    @Override
    public StoreConfiguration map(StoreConfigurationDescriptor source) {
        return switch (source) {
            case InactiveStoreConfigurationDescriptor ignored ->
                    new InactiveStoreConfiguration(source.getNodeId());
            case ActiveStoreConfigurationDescriptor active ->
                    registry.map(active.getType().getName(), active);
            default -> throw new IllegalStateException("Unexpected value: " + source);
        };
    }
}
