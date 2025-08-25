package io.naryo.infrastructure.configuration.beans;

import java.util.Map;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.filter.FilterStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.mapper.ActiveEventStoreConfigurationMapperRegistry;
import io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.event.block.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.event.block.EventStoreTarget;
import io.naryo.domain.configuration.store.active.feature.filter.FilterStoreConfiguration;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

@Component
public final class MongoEventStoreInitializer implements EnvironmentInitializer {

    private static final String EVENT_STORE_TYPE = "mongo";

    private final ActiveEventStoreConfigurationMapperRegistry mapperRegistry;

    public MongoEventStoreInitializer(ActiveEventStoreConfigurationMapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public void initialize() {
        mapperRegistry.register(
                EVENT_STORE_TYPE,
                ActiveStoreConfigurationDescriptor.class,
                properties ->
                        new MongoStoreConfiguration(
                                properties.getNodeId(), createFeatures(properties)));
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
                                                                                        new io.naryo
                                                                                                .domain
                                                                                                .common
                                                                                                .Destination(
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
                                                                ? new io.naryo.domain.common
                                                                        .Destination(
                                                                        filter.getDestination()
                                                                                .get())
                                                                : null);
                                            }
                                        }));
    }
}
