package io.naryo.api.filter.update;

import java.util.UUID;

import io.naryo.api.RequestBuilder;
import io.naryo.api.filter.update.model.UpdateFilterRequest;
import org.instancio.Instancio;

public abstract class UpdateFilterRequestBuilder<
                T extends UpdateFilterRequestBuilder<T, Y>, Y extends UpdateFilterRequest>
        implements RequestBuilder<T, Y> {

    private String name;
    private UUID nodeId;
    private String prevItemHash;

    public UpdateFilterRequestBuilder<T, Y> withName(String name) {
        this.name = name;
        return self();
    }

    public UpdateFilterRequestBuilder<T, Y> withNodeId(UUID nodeId) {
        this.nodeId = nodeId;
        return self();
    }

    public UpdateFilterRequestBuilder<T, Y> withPrevItemHash(String prevItemHash) {
        this.prevItemHash = prevItemHash;
        return self();
    }

    public String getName() {
        return this.name == null ? Instancio.create(String.class) : this.name;
    }

    public UUID getNodeId() {
        return this.nodeId == null ? Instancio.create(UUID.class) : this.nodeId;
    }

    public String getPrevItemHash() {
        return this.prevItemHash == null ? Instancio.create(String.class) : this.prevItemHash;
    }
}
