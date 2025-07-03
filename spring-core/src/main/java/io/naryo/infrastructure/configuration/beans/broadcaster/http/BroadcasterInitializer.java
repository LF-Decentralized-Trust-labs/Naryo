package io.naryo.infrastructure.configuration.beans.broadcaster.http;

import io.naryo.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import io.naryo.infrastructure.broadcaster.http.configuration.HttpBroadcasterConfiguration;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.configuration.http.HttpBroadcasterConfigurationAdditionalProperties;
import io.naryo.infrastructure.configuration.source.env.registry.broadcaster.BroadcasterConfigurationPropertiesRegistry;
import org.springframework.stereotype.Component;

@Component
public final class BroadcasterInitializer implements EnvironmentInitializer {

    private final BroadcasterConfigurationPropertiesRegistry registry;
    private final BroadcasterConfigurationMapperRegistry mapperRegistry;

    public BroadcasterInitializer(
            BroadcasterConfigurationPropertiesRegistry registry,
            BroadcasterConfigurationMapperRegistry mapperRegistry) {
        this.registry = registry;
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public void initialize() {
        registry.register("http", HttpBroadcasterConfigurationAdditionalProperties.class);
        mapperRegistry.register(
                "http",
                BroadcasterConfigurationEntryProperties.class,
                properties ->
                        new HttpBroadcasterConfiguration(
                                properties.id(),
                                new BroadcasterCache(properties.cache().expirationTime()),
                                new ConnectionEndpoint(
                                        ((HttpBroadcasterConfigurationAdditionalProperties)
                                                        properties.configuration())
                                                .endpoint()
                                                .url())));
    }
}
