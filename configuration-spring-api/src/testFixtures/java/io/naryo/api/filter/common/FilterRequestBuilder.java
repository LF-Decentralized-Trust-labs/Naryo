package io.naryo.api.filter.common;

import java.util.UUID;

import io.naryo.api.RequestBuilder;
import io.naryo.api.filter.common.request.FilterRequest;
import org.instancio.Instancio;

public abstract class FilterRequestBuilder<
                T extends FilterRequestBuilder<T, Y>, Y extends FilterRequest>
        implements RequestBuilder<T, Y> {

    private String name;
    private UUID nodeId;

    public FilterRequestBuilder<T, Y> withName(String name) {
        this.name = name;
        return self();
    }

    public String getName() {
        return this.name == null ? Instancio.create(String.class) : this.name;
    }

    public FilterRequestBuilder<T, Y> withNodeId(UUID nodeId) {
        this.nodeId = nodeId;
        return self();
    }

    public UUID getNodeId() {
        return this.nodeId == null ? Instancio.create(UUID.class) : this.nodeId;
    }
}
