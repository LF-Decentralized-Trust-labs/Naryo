package io.naryo.domain.configuration.store;

import java.util.UUID;

import io.naryo.domain.DomainBuilder;

public abstract class StoreConfigurationBuilder<
                T extends StoreConfigurationBuilder<T, Y>, Y extends StoreConfiguration>
        implements DomainBuilder<T, Y> {

    private UUID nodeId;

    public T withNodeId(UUID nodeId) {
        this.nodeId = nodeId;
        return self();
    }

    protected UUID getNodeId() {
        return this.nodeId == null ? UUID.randomUUID() : this.nodeId;
    }
}
