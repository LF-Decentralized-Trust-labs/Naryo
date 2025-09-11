package io.naryo.infrastructure.store;

import java.util.List;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.configuration.source.mapper.StoreConfigurationDescriptorMapper;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.store.configuration.mapper.ActiveStoreConfigurationMapperRegistry;
import io.naryo.domain.configuration.store.MongoStoreConfiguration;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

import static io.naryo.domain.MongoConstants.MONGO_TYPE;

@Component
public final class MongoStoreInitializer implements EnvironmentInitializer {

    private final ActiveStoreConfigurationMapperRegistry mapperRegistry;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public MongoStoreInitializer(
            ActiveStoreConfigurationMapperRegistry mapperRegistry,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.mapperRegistry = mapperRegistry;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public void initialize() {
        mapperRegistry.register(
                MONGO_TYPE,
                ActiveStoreConfigurationDescriptor.class,
                properties ->
                        new MongoStoreConfiguration(
                                properties.getNodeId(),
                                StoreConfigurationDescriptorMapper.map(properties)));

        schemaRegistry.register(
                ConfigurationSchemaType.STORE,
                MONGO_TYPE,
                new ConfigurationSchema(MONGO_TYPE, List.of()));
    }
}
