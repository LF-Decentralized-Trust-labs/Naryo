package io.naryo.infrastructure.configuration.beans.eventstore.configuration;

import java.util.List;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.mapper.StoreConfigurationDescriptorMapper;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.mapper.ActiveEventStoreConfigurationMapperRegistry;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.store.active.http.HttpStoreConfiguration;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

@Component
public final class EventStoreInitializer implements EnvironmentInitializer {

    private static final String EVENT_STORE_TYPE = "http";
    private static final String EVENT_STORE_SCHEMA_NAME = "event_store_" + EVENT_STORE_TYPE;

    private final ActiveEventStoreConfigurationMapperRegistry mapperRegistry;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public EventStoreInitializer(
            ActiveEventStoreConfigurationMapperRegistry mapperRegistry,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.mapperRegistry = mapperRegistry;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public void initialize() {
        mapperRegistry.register(
                EVENT_STORE_TYPE,
                ActiveStoreConfigurationDescriptor.class,
                properties ->
                        new HttpStoreConfiguration(
                                properties.getNodeId(),
                                StoreConfigurationDescriptorMapper.map(properties),
                                new ConnectionEndpoint(
                                        !properties.getAdditionalProperties().isEmpty()
                                                ? ((HttpBroadcasterEndpoint)
                                                                properties
                                                                        .getAdditionalProperties()
                                                                        .get("endpoint"))
                                                        .url()
                                                : null)));

        schemaRegistry.register(
                EVENT_STORE_SCHEMA_NAME,
                new ConfigurationSchema(
                        EVENT_STORE_TYPE,
                        List.of(
                                new FieldDefinition(
                                        "endpoint", HttpBroadcasterEndpoint.class, true, null))));
    }

    public record HttpBroadcasterEndpoint(String url) {}
}
