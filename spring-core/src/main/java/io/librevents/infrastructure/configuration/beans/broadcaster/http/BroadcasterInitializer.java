package io.librevents.infrastructure.configuration.beans.broadcaster.http;

import io.librevents.application.broadcaster.configuration.mapper.BroadcasterConfigurationMapperRegistry;
import io.librevents.domain.common.connection.endpoint.ConnectionEndpoint;
import io.librevents.domain.configuration.broadcaster.BroadcasterCache;
import io.librevents.infrastructure.broadcaster.http.configuration.HttpBroadcasterConfiguration;
import io.librevents.infrastructure.configuration.beans.env.EnvironmentInitializer;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.http.HttpBroadcasterConfigurationAdditionalProperties;
import io.librevents.infrastructure.configuration.source.env.registry.broadcaster.BroadcasterConfigurationPropertiesRegistry;
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
