package io.naryo.api.storeconfiguration.fixtures;

import java.util.UUID;

import io.naryo.api.storeconfiguration.common.request.StoreConfigurationRequest;
import io.naryo.domain.DomainBuilder;

public abstract class StoreConfigurationRequestBuilder<
                T extends StoreConfigurationRequestBuilder<T, Y>,
                Y extends StoreConfigurationRequest>
        implements DomainBuilder<T, Y> {

    private UUID nodeId;

    public StoreConfigurationRequestBuilder<T, Y> withNodeId(UUID nodeId) {
        this.nodeId = nodeId;
        return self();
    }

    public UUID getNodeId() {
        return this.nodeId == null ? UUID.randomUUID() : this.nodeId;
    }
}
