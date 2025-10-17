package io.naryo.api.filter.create;

import java.util.UUID;

import io.naryo.api.RequestBuilder;
import io.naryo.api.filter.create.model.CreateFilterRequest;
import org.instancio.Instancio;

public abstract class CreateFilterRequestBuilder<
                T extends CreateFilterRequestBuilder<T, Y>, Y extends CreateFilterRequest>
        implements RequestBuilder<T, Y> {

    private String name;
    private UUID nodeId;

    public CreateFilterRequestBuilder<T, Y> withName(String name) {
        this.name = name;
        return self();
    }

    public String getName() {
        return this.name == null ? Instancio.create(String.class) : this.name;
    }

    public CreateFilterRequestBuilder<T, Y> withNodeId(UUID nodeId) {
        this.nodeId = nodeId;
        return self();
    }

    public UUID getNodeId() {
        return this.nodeId == null ? Instancio.create(UUID.class) : this.nodeId;
    }
}
