package io.naryo.infrastructure.configuration.beans.store.http;

import java.util.List;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.application.configuration.source.mapper.StoreConfigurationDescriptorMapper;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.store.configuration.mapper.ActiveStoreConfigurationMapperRegistry;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.store.active.http.HttpStoreConfiguration;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

import static io.naryo.domain.HttpConstants.HTTP_TYPE;

@Component
public final class HttpStoreInitializer implements EnvironmentInitializer {

    private final ActiveStoreConfigurationMapperRegistry mapperRegistry;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public HttpStoreInitializer(
            ActiveStoreConfigurationMapperRegistry mapperRegistry,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.mapperRegistry = mapperRegistry;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public void initialize() {
        mapperRegistry.register(
                HTTP_TYPE,
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
                ConfigurationSchemaType.STORE,
                HTTP_TYPE,
                new ConfigurationSchema(
                        HTTP_TYPE,
                        List.of(
                                new FieldDefinition(
                                        "endpoint", HttpBroadcasterEndpoint.class, true, null))));
    }

    public record HttpBroadcasterEndpoint(String url) {}
}
