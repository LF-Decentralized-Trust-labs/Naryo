package io.naryo.infrastructure.configuration.beans.broadcaster.http;

import java.util.List;

import io.naryo.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import io.naryo.infrastructure.broadcaster.http.configuration.HttpBroadcasterConfiguration;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

@Component
public final class BroadcasterInitializer implements EnvironmentInitializer {

    private static final String HTTP_BROADCASTER_TYPE = "http";

    private final BroadcasterConfigurationMapperRegistry mapperRegistry;
    private final ConfigurationSchemaRegistry schemaRegistry;

    public BroadcasterInitializer(
            BroadcasterConfigurationMapperRegistry mapperRegistry,
            ConfigurationSchemaRegistry schemaRegistry) {
        this.mapperRegistry = mapperRegistry;
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public void initialize() {
        mapperRegistry.register(
                HTTP_BROADCASTER_TYPE,
                BroadcasterConfigurationDescriptor.class,
                properties -> {
                    HttpBroadcasterEndpoint endpoint =
                            (HttpBroadcasterEndpoint)
                                    properties.getAdditionalProperties().get("endpoint");
                    return new HttpBroadcasterConfiguration(
                            properties.getId(),
                            new BroadcasterCache(properties.getCache().getExpirationTime()),
                            new ConnectionEndpoint(endpoint.url));
                });

        schemaRegistry.register(
                HTTP_BROADCASTER_TYPE,
                new ConfigurationSchema(
                        HTTP_BROADCASTER_TYPE,
                        List.of(
                                new FieldDefinition(
                                        "endpoint", HttpBroadcasterEndpoint.class, true, null))));
    }

    public record HttpBroadcasterEndpoint(String url) {}
}
