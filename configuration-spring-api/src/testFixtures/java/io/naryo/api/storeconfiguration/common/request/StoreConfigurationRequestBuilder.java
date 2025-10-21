package io.naryo.api.storeconfiguration.common.request;

import java.util.UUID;

import io.naryo.api.RequestBuilder;

public abstract class StoreConfigurationRequestBuilder<
                T extends StoreConfigurationRequestBuilder<T, Y>,
                Y extends StoreConfigurationRequest>
        implements RequestBuilder<T, Y> {

    private UUID nodeId;

    public StoreConfigurationRequestBuilder<T, Y> withNodeId(UUID nodeId) {
        this.nodeId = nodeId;
        return self();
    }

    public UUID getNodeId() {
        return this.nodeId == null ? UUID.randomUUID() : this.nodeId;
    }
}
