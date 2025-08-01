package io.naryo.infrastructure.configuration.beans.eventstore.configuration;

import java.util.List;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.model.event.ActiveEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.mapper.ActiveEventStoreConfigurationMapperRegistry;
import io.naryo.domain.broadcaster.Destination;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.eventstore.active.block.EventStoreTarget;
import io.naryo.domain.configuration.eventstore.active.block.http.HttpBlockEventStoreConfiguration;
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
                ActiveEventStoreConfigurationDescriptor.class,
                properties -> {
                    BlockEventStoreConfigurationDescriptor descriptor =
                            (BlockEventStoreConfigurationDescriptor) properties;
                    return new HttpBlockEventStoreConfiguration(
                            descriptor.getNodeId(),
                            descriptor.getTargets().stream()
                                    .map(
                                            target ->
                                                    new EventStoreTarget(
                                                            target.type(),
                                                            new Destination(target.destination())))
                                    .collect(Collectors.toSet()),
                            new ConnectionEndpoint(
                                    !descriptor.getAdditionalProperties().isEmpty()
                                            ? ((HttpBroadcasterEndpoint)
                                                            descriptor
                                                                    .getAdditionalProperties()
                                                                    .get("endpoint"))
                                                    .url()
                                            : null));
                });

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
