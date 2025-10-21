package io.naryo.api.storeconfiguration.common.response;

import java.util.UUID;

import io.naryo.api.RequestBuilder;
import org.instancio.Instancio;

public abstract class StoreConfigurationResponseBuilder<
                T extends StoreConfigurationResponseBuilder<T, Y>,
                Y extends StoreConfigurationResponse>
        implements RequestBuilder<T, Y> {

    private UUID nodeId;
    private String currentItemHash;

    public StoreConfigurationResponseBuilder<T, Y> withNodeId(UUID nodeId) {
        this.nodeId = nodeId;
        return self();
    }

    public UUID getNodeId() {
        return this.nodeId == null ? UUID.randomUUID() : this.nodeId;
    }

    public StoreConfigurationResponseBuilder<T, Y> withCurrentItemHash(String currentItemHash) {
        this.currentItemHash = currentItemHash;
        return self();
    }

    public String getCurrentItemHash() {
        return this.currentItemHash == null ? Instancio.create(String.class) : this.currentItemHash;
    }
}
