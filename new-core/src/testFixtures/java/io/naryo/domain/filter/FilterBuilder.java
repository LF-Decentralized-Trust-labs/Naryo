package io.naryo.domain.filter;

import org.instancio.Instancio;
import org.instancio.InstancioApi;

import java.util.UUID;

import static org.instancio.Select.field;

public abstract class FilterBuilder<T, Y extends Filter> {

    private String id;
    private String name;
    private String nodeId;

    public abstract T self();

    public abstract Y build();

    public T withId(UUID id) {
        this.id = id.toString();
        return self();
    }

    public T withName(String name) {
        this.name = name;
        return self();
    }

    public T withNodeId(String nodeId) {
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

    private String getId() {
        return this.id == null
            ? UUID.randomUUID().toString()
            : this.id;
    }

    private String getName() {
        return this.name == null
            ? Instancio.create(String.class)
            : this.name;
    }

    private String getNodeId() {
        return this.nodeId == null
            ? UUID.randomUUID().toString()
            : this.nodeId;
    }
}
