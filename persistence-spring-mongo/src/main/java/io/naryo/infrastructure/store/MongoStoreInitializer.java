package io.naryo.infrastructure.store;

import java.util.List;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.mapper.StoreConfigurationDescriptorMapper;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.mapper.ActiveEventStoreConfigurationMapperRegistry;
import io.naryo.domain.configuration.store.MongoStoreConfiguration;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

import static io.naryo.domain.configuration.store.MongoStoreConfiguration.MONGO_STORE_TYPE;

@Component
public final class MongoStoreInitializer implements EnvironmentInitializer {

    private static final String EVENT_STORE_SCHEMA_NAME = "event_store_" + MONGO_STORE_TYPE;

    private final ActiveEventStoreConfigurationMapperRegistry mapperRegistry;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public MongoStoreInitializer(
            ActiveEventStoreConfigurationMapperRegistry mapperRegistry,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.mapperRegistry = mapperRegistry;
        this.schemaRegistry = schemaRegistry;
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

        schemaRegistry.register(
                EVENT_STORE_SCHEMA_NAME, new ConfigurationSchema(MONGO_STORE_TYPE, List.of()));
    }
}
