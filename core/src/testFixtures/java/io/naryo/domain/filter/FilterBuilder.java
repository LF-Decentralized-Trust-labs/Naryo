package io.naryo.domain.filter;

import java.util.UUID;

import io.naryo.domain.DomainBuilder;
import org.instancio.Instancio;

public abstract class FilterBuilder<T extends FilterBuilder<T, Y>, Y extends Filter>
        implements DomainBuilder<T, Y> {

    private UUID id;
    private FilterName name;
    private UUID nodeId;

    public abstract T self();

    public abstract Y build();

    public T withId(UUID id) {
        this.id = id;
        return self();
    }

    public T withName(FilterName name) {
        this.name = name;
        return self();
    }

    public T withNodeId(UUID nodeId) {
        this.nodeId = nodeId;
        return self();
    }

    protected UUID getId() {
        return this.id == null ? UUID.randomUUID() : this.id;
    }

    protected FilterName getName() {
        return this.name == null ? Instancio.create(FilterName.class) : this.name;
    }

    protected UUID getNodeId() {
        return this.nodeId == null ? UUID.randomUUID() : this.nodeId;
    }
}
