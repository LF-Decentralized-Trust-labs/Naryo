package io.naryo.domain.filter;

import org.instancio.Instancio;
import org.instancio.InstancioApi;

import java.util.UUID;

import static org.instancio.Select.field;

public abstract class FilterBuilder<T, Y extends Filter> {

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

    protected InstancioApi<Y> buildBase(InstancioApi<Y> builder, FilterType type) {
        return builder
            .set(field(Filter::getId), this.getId())
            .set(field(Filter::getName), this.getName())
            .set(field(Filter::getNodeId), this.getNodeId())
            .set(field(Filter::getType), type);
    }

    protected UUID getId() {
        return this.id == null
            ? UUID.randomUUID()
            : this.id;
    }

    protected FilterName getName() {
        return this.name == null
            ? Instancio.create(FilterName.class)
            : this.name;
    }

    protected UUID getNodeId() {
        return this.nodeId == null
            ? UUID.randomUUID()
            : this.nodeId;
    }
}
