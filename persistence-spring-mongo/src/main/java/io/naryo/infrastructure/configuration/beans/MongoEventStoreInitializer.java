package io.naryo.infrastructure.configuration.beans;

import io.naryo.application.configuration.source.model.event.ActiveEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.event.store.configuration.mapper.ActiveEventStoreConfigurationMapperRegistry;
import io.naryo.domain.broadcaster.Destination;
import io.naryo.domain.configuration.eventstore.active.block.EventStoreTarget;
import io.naryo.domain.configuration.eventstore.active.block.MongoBlockEventStoreConfiguration;
import io.naryo.infrastructure.configuration.beans.env.EnvironmentInitializer;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
            ActiveEventStoreConfigurationDescriptor.class,
            properties -> {
                BlockEventStoreConfigurationDescriptor descriptor =
                    (BlockEventStoreConfigurationDescriptor) properties;
                return new MongoBlockEventStoreConfiguration(
                    descriptor.getNodeId(),
                    descriptor.getTargets().stream()
                        .map(
                            target ->
                                new EventStoreTarget(
                                    target.type(),
                                    new Destination(target.destination())))
                        .collect(Collectors.toSet())
                );
            });
    }
}
