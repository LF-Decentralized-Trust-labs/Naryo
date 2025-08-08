package io.naryo.infrastructure.configuration.beans.eventstore.configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.filter.FilterStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.mapper.ActiveEventStoreConfigurationMapperRegistry;
import io.naryo.domain.common.Destination;
import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.event.block.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.event.block.EventStoreTarget;
import io.naryo.domain.configuration.store.active.feature.filter.FilterStoreConfiguration;
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
                properties -> new HttpStoreConfiguration(
                        properties.getNodeId(),
                        createFeatures(properties),
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

    private Map<StoreFeatureType, StoreFeatureConfiguration> createFeatures(
            ActiveStoreConfigurationDescriptor descriptor) {
        return descriptor.getFeatures().entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                value ->
                                        switch (value.getKey()) {
                                            case EVENT -> {
                                                BlockEventStoreConfigurationDescriptor block =
                                                        (BlockEventStoreConfigurationDescriptor)
                                                                value.getValue();
                                                yield new BlockEventStoreConfiguration(
                                                        block.getTargets().stream()
                                                                .map(
                                                                        target ->
                                                                                new EventStoreTarget(
                                                                                        target
                                                                                                .type(),
                                                                                        new Destination(
                                                                                                target
                                                                                                        .destination())))
                                                                .collect(Collectors.toSet()));
                                            }
                                            case FILTER_SYNC -> {
                                                FilterStoreConfigurationDescriptor filter =
                                                        (FilterStoreConfigurationDescriptor)
                                                                value.getValue();
                                                yield new FilterStoreConfiguration(
                                                        filter.getDestination().isPresent()
                                                                ? new Destination(
                                                                        filter.getDestination()
                                                                                .get())
                                                                : null);
                                            }
                                        }));
    }

    public record HttpBroadcasterEndpoint(String url) {}
}
