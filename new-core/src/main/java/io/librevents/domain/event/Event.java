package io.librevents.domain.event;

import java.util.Objects;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
