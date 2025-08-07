package io.naryo.infrastructure.configuration.source.env.model.store;

import java.util.UUID;

import io.naryo.application.configuration.source.model.store.InactiveStoreConfigurationDescriptor;

public final class InactiveStoreConfigurationProperties extends StoreConfigurationProperties
        implements InactiveStoreConfigurationDescriptor {

    public InactiveStoreConfigurationProperties(UUID nodeId) {
        super(nodeId);
    }
}
