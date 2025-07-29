package io.naryo.infrastructure.configuration.beans.broadcaster.http;

import java.util.List;
import java.util.Map;

import io.naryo.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import io.naryo.domain.configuration.broadcaster.http.HttpBroadcasterConfiguration;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

@Component
public final class BroadcasterInitializer implements EnvironmentInitializer {

    private static final String HTTP_BROADCASTER_TYPE = "http";
    private static final String HTTP_BROADCASTER_SCHEMA_NAME =
            "broadcaster_" + HTTP_BROADCASTER_TYPE;

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
                    Map<String, Object> props = properties.getAdditionalProperties();
                    Object raw = props.isEmpty() ? null : props.get("endpoint");
                    HttpBroadcasterEndpoint endpoint;

                    if (raw instanceof HttpBroadcasterEndpoint typed) {
                        endpoint = typed;
                    } else if (raw instanceof Map<?, ?> map) {
                        endpoint = new HttpBroadcasterEndpoint((String) map.get("url"));
                    } else {
                        return null;
                    }

                    return new HttpBroadcasterConfiguration(
                            properties.getId(),
                            new BroadcasterCache(
                                    valueOrNull(properties.getCache()).getExpirationTime()),
                            new ConnectionEndpoint(endpoint.url));
                });

        schemaRegistry.register(
                HTTP_BROADCASTER_SCHEMA_NAME,
                new ConfigurationSchema(
                        HTTP_BROADCASTER_TYPE,
                        List.of(
                                new FieldDefinition(
                                        "endpoint", HttpBroadcasterEndpoint.class, true, null))));
    }

    public record HttpBroadcasterEndpoint(String url) {}
}
