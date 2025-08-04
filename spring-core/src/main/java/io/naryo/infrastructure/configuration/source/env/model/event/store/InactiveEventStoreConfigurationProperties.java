package io.naryo.infrastructure.configuration.source.env.model.event.store;

import java.util.UUID;

import io.naryo.application.configuration.source.model.event.InactiveEventStoreConfigurationDescriptor;

public final class InactiveEventStoreConfigurationProperties
        extends EventStoreConfigurationProperties
        implements InactiveEventStoreConfigurationDescriptor {

    public InactiveEventStoreConfigurationProperties(UUID nodeId) {
        super(nodeId);
    }
}
