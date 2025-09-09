package io.naryo.infrastructure.configuration.beans;

import io.naryo.application.configuration.source.mapper.StoreConfigurationDescriptorMapper;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.mapper.ActiveEventStoreConfigurationMapperRegistry;
import io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

import static io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration.MONGO_STORE_TYPE;

@Component
public final class MongoEventStoreInitializer implements EnvironmentInitializer {

    private final ActiveEventStoreConfigurationMapperRegistry mapperRegistry;

    public MongoEventStoreInitializer(ActiveEventStoreConfigurationMapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public void initialize() {
        mapperRegistry.register(
                MONGO_STORE_TYPE,
                ActiveStoreConfigurationDescriptor.class,
                properties ->
                        new MongoStoreConfiguration(
                                properties.getNodeId(),
                                StoreConfigurationDescriptorMapper.map(properties)));
    }
}
