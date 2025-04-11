package io.librevents.domain.filter;

import java.util.Objects;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class Filter {

    protected final UUID id;
    protected final FilterName name;
    protected final FilterType type;
    protected final UUID nodeId;

    protected Filter(UUID id, FilterName name, FilterType type, UUID nodeId) {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(type, "Type cannot be null");
        Objects.requireNonNull(nodeId, "NodeId cannot be null");
        this.id = id;
        this.name = name;
        this.type = type;
        this.nodeId = nodeId;
    }
}
