package io.librevents.domain.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public abstract class Event {

    private final EventType eventType;
    private final UUID nodeId;

    protected Event(EventType eventType, UUID nodeId) {
        Objects.requireNonNull(eventType, "eventType cannot be null");
        Objects.requireNonNull(nodeId, "nodeId cannot be null");
        this.eventType = eventType;
        this.nodeId = nodeId;
    }
}
